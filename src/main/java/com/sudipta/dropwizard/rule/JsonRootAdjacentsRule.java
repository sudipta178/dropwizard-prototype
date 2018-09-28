package com.sudipta.dropwizard.rule;

import static com.sudipta.dropwizard.client.JsonToDAG.ROOT_ADJACENTS_SET;
import static com.sudipta.dropwizard.client.JsonToDAG.VERTEX_ID;

import java.util.Set;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

@Rule(name = "jsonRootAdjacentsRule", description = "Check for adjacent to root node of a given vertex")
public class JsonRootAdjacentsRule {

    @Condition
    public boolean checkForAdjacentToRootNode(@Fact(ROOT_ADJACENTS_SET) Set<String> rootAdjacentsSet,
                                              @Fact(VERTEX_ID) String vertexId) {
        if (null != rootAdjacentsSet && !rootAdjacentsSet.contains(vertexId)) {
            return true;
        }
        return false;
    }

    @Action
    public void rule_002(@Fact(VERTEX_ID) String vertexId) throws Exception {
        System.out.println("jsonRootAdjacentsRule is successful for vertexId : " + vertexId);
    }

    @Priority
    public int getPriority() {
        return 2;
    }
}
