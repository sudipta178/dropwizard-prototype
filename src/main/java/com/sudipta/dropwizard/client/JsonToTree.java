package com.sudipta.dropwizard.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonToTree {
    private static final Logger logger = LoggerFactory.getLogger(JsonToTree.class);

    //Create an ObjectMapper instance
    private static  ObjectMapper mapper = new ObjectMapper();
    private Set<String> nodeSet = new HashSet<>();

    public static final String JSON_PATH = "assets/json/sampleProducerforKafkaPush.json";

    public Set<String> getJsonTreePaths() {
        Set<String> pathSet = new HashSet<>();
        List<String> paths = new ArrayList<>();
        try {
            JsonNode rootNode = readJson();
            parseNode(rootNode, paths, pathSet);
            System.out.println("JSON Tree Parse Done");

            System.out.println("Total Paths: " + pathSet.size());
            pathSet.forEach(path -> System.out.println(path));

        } catch(Exception e) {
            e.printStackTrace();
        }
        return Collections.unmodifiableSet(pathSet);
    }

    public List<String> findNodes(String... nodePaths) {
        List<String> nodevalues = new ArrayList<>();
        try {
            JsonNode rootNode = readJson();
            if (null != rootNode) {
                for (String nodePath : nodePaths) {
                    StringTokenizer st = new StringTokenizer(nodePath, "/", false);
                    JsonNode tempNode = rootNode.deepCopy();
                    while (st.hasMoreTokens()) {
                        JsonNode childNode = tempNode.findValue(st.nextToken());
                        if (null != childNode) {
                            if (null != childNode.textValue()) {
                                nodevalues.add(childNode.textValue());
                            }
                            tempNode = childNode;
                        }
                    }
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return Collections.unmodifiableList(nodevalues);
    }

    private JsonNode readJson() throws Exception {
        //Create tree from JSON
        InputStream jsonInput = JsonToTree.class.getClassLoader().getResourceAsStream(JSON_PATH);
        JsonNode rootNode = mapper.readTree(jsonInput);
        System.out.println("Input JSON Read");

        return rootNode;
    }

    private void parseNode(JsonNode rootNode, List<String> paths, Set<String> pathSet) {
        if (null != rootNode) {
            /* append this node to the path array */
            String value =  rootNode.findValue("name").textValue();
            if (null == value || "".equals(value)) {
                value = String.valueOf(rootNode.findValue("id").intValue());
            }
            if (null != value && !"".equals(value)) {
                paths.add(value.trim());
            }
            nodeSet.add(value);

            /* it's a leaf, so print the path that led to here  */
            JsonNode childNodes = rootNode.findValue("children");
            if (null == childNodes) {
                printPath(paths, pathSet);
            }
            else {
                int size = 0;
                Iterator<JsonNode> nodeIterator = childNodes.elements();
                while (nodeIterator.hasNext()) {
                    nodeIterator.next();
                    size++;
                }
                if (size > 1) {
                    printPath(paths, pathSet, false);
                }
                nodeIterator = childNodes.elements();
                while (nodeIterator.hasNext()) {
                    JsonNode childNode = nodeIterator.next();
                    parseNode(childNode, paths, pathSet);
                }
            }
        }
    }

    private void printPath(List<String> paths, Set<String> pathSet) {
        StringBuilder pathBuilder = new StringBuilder();
        Stack<String> patStack = new Stack<>();
        int index = 0;
        Iterator<String> pathIterator = paths.iterator();
        while (pathIterator.hasNext()) {
            index++;
            patStack.add(pathIterator.next());
            pathIterator.remove();
        }
        while (!patStack.empty()) {
            pathBuilder.append(patStack.pop()).append(" -> ");
        }
        if (!"".equals(pathBuilder.toString())) {
            pathSet.add(pathBuilder.toString().substring(0, pathBuilder.toString().lastIndexOf(" -> ")));
        }
    }

    private void printPath(List<String> paths, Set<String> pathSet, boolean deleteNode) {
        StringBuilder pathBuilder = new StringBuilder();
        Stack<String> patStack = new Stack<>();
        int index = 0;
        Iterator<String> pathIterator = paths.iterator();
        while (pathIterator.hasNext()) {
            index++;
            patStack.add(pathIterator.next());
            if (index != paths.size()) {
                pathIterator.remove();
            }
        }
        while (!patStack.empty()) {
            pathBuilder.append(patStack.pop()).append(" -> ");
        }
        if (!"".equals(pathBuilder.toString())) {
            pathSet.add(pathBuilder.toString().substring(0, pathBuilder.toString().lastIndexOf(" -> ")));
        }
    }

    public Set<String> getNodeSet() {
        return nodeSet;
    }
}
