package com.sudipta.dropwizard.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

public class JsonToTree {
    //Create an ObjectMapper instance
    private static  ObjectMapper mapper = new ObjectMapper();
    private Stack<JsonNode> nodeStack = new Stack<>();
    private Stack<String> pathStack = new Stack<>();

    public Set<String> getJsonTreePaths() {
        Set<String> pathSet = new HashSet<>();
        String [] paths = new String[100];
        try {
            //Create tree from JSON
            InputStream jsonInput = JsonToTree.class.getClassLoader().getResourceAsStream("assets/json/correctedJson.json");
            JsonNode rootNode = mapper.readTree(jsonInput);
            System.out.println("Input JSON Read");

            parseNode(rootNode, paths, 0, pathSet);
            System.out.println("JSON Tree Parse Done");

            System.out.println("Total Paths: " + pathSet.size());
            for (String path : pathSet) {
                System.out.println(path);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return pathSet;
    }

    private void parseNode(JsonNode rootNode, String [] paths, int pathLen, Set<String> pathSet) {
        if (null != rootNode) {
            /* append this node to the path array */
            String value =  rootNode.findValue("name").textValue();
            if (null == value || "".equals(value)) {
                value = String.valueOf(rootNode.findValue("id").intValue());
            }
            if (null != value && !"".equals(value)) {
                paths[pathLen] = value.trim();
                pathLen++;
            }

            /* it's a leaf, so print the path that led to here  */
            JsonNode childNodes = rootNode.findValue("children");
            if (null == childNodes) {
                printPath(paths, pathLen, pathSet);
            }
            else {
                Iterator<JsonNode> nodeIterator = childNodes.elements();
                while (nodeIterator.hasNext()) {
                    JsonNode childNode = nodeIterator.next();
                    parseNode(childNode, paths, pathLen, pathSet);
                }
            }
        }
    }

    private void printPath(String [] paths, int pathLen, Set<String> pathSet) {
        StringBuilder pathBuilder = new StringBuilder();
        for(int i = pathLen - 1; i >= 0; i--) {
            pathBuilder.append(paths[i]);
            if (i != 0) {
                pathBuilder.append(" -> ");
            }
        }
        if (!"".equals(pathBuilder.toString())) {
            pathSet.add(pathBuilder.toString());
        }
    }

}
