package com.sudipta.dropwizard.client;

import com.sudipta.dropwizard.rule.JsonMoreThanOneAdjacentNodeRule;
import com.sudipta.dropwizard.rule.JsonRootAdjacentsRule;
import com.sudipta.dropwizard.rule.JsonToDAGRule;
import com.sudipta.dropwizard.rule.NonJsonToDAGRule;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RulesEngineParameters;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonToDAG {
    private static final Logger logger = LoggerFactory.getLogger(JsonToDAG.class);

    private InputStream inputJson;
    private JsonReader jsonReader;
    private JsonObject jsonRoot;

    private Graph<String, DefaultEdge> dag;
    private AllDirectedPaths<String, DefaultEdge> dagPaths;

    private Map<String, JsonObject> vertexMap;
    private Set<String> edgeSet;

    private Rules rules;
    private RulesEngine rulesEngine;

    public static final String JSON_NODE_ID = "id";
    public static final String JSON_NODE_NAME = "name";
    public static final String JSON_NODE_DISPLAY_NAME = "display_name";
    public static final String JSON_NODE_ADJACENTS = "adjacents";
    public static final String JSON_NODE_ADJACENTS_ID = "id";
    public static final String JSON_ADJACENTS_ARRAY = "jsonAdjacentsArray";
    public static final String ROOT_ADJACENTS_SET = "rootAdjacentsSet";
    public static final String VERTEX_ID = "vertexId";
    public static final String VERTEX_DISPLAY_NAME = "vertexDisplayName";
    public static final String PATH_BUILDER = "pathBuilder";
    public static final String PATH_SET = "pathSet";

    public JsonToDAG() {
        this(0);
    }

    public JsonToDAG(int rootId) {
        vertexMap = new HashMap<>();
        edgeSet = new HashSet<>();

        dag = new DirectedAcyclicGraph<>(DefaultEdge.class);
        try {
            initRules();
            buildGraphFromJsonRoot(rootId);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (null != jsonReader) {
                    jsonReader.close();
                }
                if (null != inputJson) {
                    inputJson.close();
                }
            } catch (Throwable t) {
                logger.error(t.getMessage());
            }
        }
        dagPaths = new AllDirectedPaths(dag);
    }

    public Set<String> getJsonDAGPaths() {
        Set<String> pathSet = new HashSet<>();
        try {
            int rootId = 0;
            Set<String> rootAdjacentsSet = new HashSet<>();
            if (null != this.jsonRoot) {
                rootId = jsonRoot.getInt(JSON_NODE_ID);
                JsonArray jsonAdjacentsArray = this.jsonRoot.getJsonArray(JSON_NODE_ADJACENTS);
                if (null != jsonAdjacentsArray && !jsonAdjacentsArray.isEmpty()) {
                    for (int i = 0; i < jsonAdjacentsArray.size(); i++) {
                        JsonObject adjacentJsonObject = jsonAdjacentsArray.getJsonObject(i);
                        rootAdjacentsSet.add(adjacentJsonObject.getString(JSON_NODE_ADJACENTS_ID));
                    }
                }
            }

            dagPaths.getAllPaths(findRootNode(rootId), findLeafNodes(), false, Integer.MAX_VALUE).forEach(graphPath -> {
                StringBuilder pathBuilder = new StringBuilder();
                graphPath.getVertexList().forEach(vertex -> {
                    if (vertexMap.containsKey(vertex) && null != vertexMap.get(vertex) && !"".equals(vertexMap.get(vertex).getString(JSON_NODE_DISPLAY_NAME))) {
                        JsonObject jsonObject = vertexMap.get(vertex);
                        String vertexDisplayName = jsonObject.getString(JSON_NODE_DISPLAY_NAME);
                        JsonArray jsonAdjacentsArray = jsonObject.getJsonArray(JSON_NODE_ADJACENTS);

                        // Define rule facts
                        Facts facts = new Facts();
                        facts.put(JSON_ADJACENTS_ARRAY, jsonAdjacentsArray);
                        facts.put(ROOT_ADJACENTS_SET, rootAdjacentsSet);
                        facts.put(VERTEX_ID, vertex);
                        facts.put(VERTEX_DISPLAY_NAME, vertexDisplayName);
                        facts.put(PATH_BUILDER, pathBuilder);
                        facts.put(PATH_SET, pathSet);

                        this.rulesEngine.fire(this.rules, facts);
                    }
                });
                if (!"".equals(pathBuilder.toString())) {
                    pathSet.add(pathBuilder.toString().substring(0, pathBuilder.toString().lastIndexOf(" -> ")));
                }
            });

            logger.info("Total Paths: " + pathSet.size());
            logger.info("All paths: " + pathSet);
            logger.info("All edges: " + edgeSet);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }
        return pathSet;
    }

    public JsonObject getJsonRoot(int rootId) throws Exception {
        if (null == this.jsonRoot) {
            buildGraphFromJsonRoot(rootId);
        }
        return this.jsonRoot;
    }

    public Map<String, JsonObject> getVertexMap() {
        return (null != vertexMap ? Collections.unmodifiableMap(vertexMap) : new HashMap<>());
    }

    public Set<String> getEdgeSet() {
        return (null != edgeSet ? Collections.unmodifiableSet(edgeSet) : new HashSet<>());
    }

    public Set<String> findRootNode(int rootId) {
        Set<String> rootNodes = new HashSet<>();
        if (null != vertexMap && !vertexMap.isEmpty()) {
            String rootIdStr = String.valueOf(rootId);
            if (vertexMap.containsKey(rootIdStr) && null != vertexMap.get(rootIdStr)) {
                rootNodes.add(rootIdStr);
            }
        }
        return rootNodes;
    }

    public Set<String> findLeafNodes() {
        Set<String> leafNodes = new HashSet<>();
        if (null != vertexMap && !vertexMap.isEmpty()) {
            vertexMap.forEach((k, v) -> {
                if (null != v) {
                    JsonArray jsonAdjacentsArray = v.getJsonArray(JSON_NODE_ADJACENTS);
                    if (null == jsonAdjacentsArray || (null != jsonAdjacentsArray && jsonAdjacentsArray.isEmpty())) {
                        leafNodes.add(String.valueOf(v.getInt(JSON_NODE_ADJACENTS_ID)));
                    }
                }
            });
        }
        return leafNodes;
    }

    private void initRules() {
        //Create a rules engine and initialize with optional parameters
        RulesEngineParameters parameters = new RulesEngineParameters().skipOnFirstAppliedRule(true);
        this.rulesEngine = new DefaultRulesEngine(parameters);

        // Initialize Rules
        JsonMoreThanOneAdjacentNodeRule rule1 = new JsonMoreThanOneAdjacentNodeRule();
        JsonRootAdjacentsRule rule2 = new JsonRootAdjacentsRule();
        JsonToDAGRule jsonToDAGRule = new JsonToDAGRule(rule1, rule2);
        NonJsonToDAGRule nonJsonToDAGRule = new NonJsonToDAGRule(rule1, rule2);

        this.rules = new Rules();
        this.rules.register(jsonToDAGRule);
        this.rules.register(nonJsonToDAGRule);
    }

    private Graph<String, DefaultEdge> buildGraphFromJsonRoot(int rootId) {
        // Define DAG from given JsonRoot
        JsonStructure jsonStructure = readJsonFromFile();
        JsonArray jsonNodesArray = ((JsonObject) jsonStructure).getJsonArray("nodes");
        if (null != jsonNodesArray && !jsonNodesArray.isEmpty()) {
            for (int i = 0; i < jsonNodesArray.size(); i++) {
                JsonObject jsonObject = jsonNodesArray.getJsonObject(i);
                int id = jsonObject.getInt(JSON_NODE_ID);
                String name = jsonObject.getString(JSON_NODE_NAME);
                if (id == rootId) {
                    jsonRoot = jsonObject;
                }
                String vertexId = String.valueOf(id);
                dag.addVertex(vertexId);
                vertexMap.put(vertexId, jsonObject);
            }
            logger.info("All Vertex(s) added to DAG");
            logger.info("Vertax Map Build");

            for (int i = 0; i < jsonNodesArray.size(); i++) {
                JsonObject jsonObject = jsonNodesArray.getJsonObject(i);
                String vertexId = String.valueOf(jsonObject.getInt(JSON_NODE_ID));
                String vertexName = jsonObject.getString(JSON_NODE_NAME);
                JsonArray jsonAdjacentsArray = jsonObject.getJsonArray(JSON_NODE_ADJACENTS);
                if (null != jsonAdjacentsArray && !jsonAdjacentsArray.isEmpty()) {
                    for (int j = 0; j < jsonAdjacentsArray.size(); j++) {
                        JsonObject adjacentJsonObject = jsonAdjacentsArray.getJsonObject(j);
                        String targetId = adjacentJsonObject.getString(JSON_NODE_ADJACENTS_ID);
                        String targetName = vertexMap.containsKey(targetId) ? vertexMap.get(targetId).getString(JSON_NODE_NAME) : targetId;

                        dag.addEdge(vertexId, targetId);
                        edgeSet.add("{" + vertexName + "," + targetName + "}");
                    }
                }
            }
            logger.info("All Edge(s) added to DAG");
        }

        logger.info("DAG from JSON Build");
        // Note: undirected edges are printed as: {<v1>,<v2>}
        logger.info(dag.toString());

        return dag;
    }

    private JsonStructure readJsonFromFile() {
        // Create DAG from JSON
        inputJson = JsonToDAG.class.getClassLoader().getResourceAsStream("assets/json/InputJsonCorrected.json");
        jsonReader = Json.createReader(inputJson);
        logger.info("Input JSON Read");

        return jsonReader.read();
    }
}
