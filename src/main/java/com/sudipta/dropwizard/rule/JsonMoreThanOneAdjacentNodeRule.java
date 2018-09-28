package com.sudipta.dropwizard.rule;

import static com.sudipta.dropwizard.client.JsonToDAG.JSON_ADJACENTS_ARRAY;

import javax.json.JsonArray;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

@Rule(name = "jsonMoreThanOneAdjacentNodeRule", description = "Check for more than one adjacent node in a given vertex")
public class JsonMoreThanOneAdjacentNodeRule {

    @Condition
    public boolean checkForMoreThanOneAdjacentNode(@Fact(JSON_ADJACENTS_ARRAY) JsonArray jsonAdjacentsArray) {
        if (null != jsonAdjacentsArray && jsonAdjacentsArray.size() > 1) {
            return true;
        }
        return false;
    }

    @Action
    public void rule_001() throws Exception {
        System.out.println("jsonMoreThanOneAdjacentNodeRule is successful");
    }

    @Priority
    public int getPriority() {
        return 1;
    }

}
