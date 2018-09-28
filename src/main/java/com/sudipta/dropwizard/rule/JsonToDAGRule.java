package com.sudipta.dropwizard.rule;

import static com.sudipta.dropwizard.client.JsonToDAG.PATH_BUILDER;
import static com.sudipta.dropwizard.client.JsonToDAG.PATH_SET;
import static com.sudipta.dropwizard.client.JsonToDAG.VERTEX_DISPLAY_NAME;

import java.util.Set;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.support.UnitRuleGroup;

public class JsonToDAGRule extends UnitRuleGroup {

    public JsonToDAGRule(Object... rules) {
        for (Object rule : rules) {
            addRule(rule);
        }
    }

    @Override
    public void execute(Facts facts) throws Exception {
        super.execute(facts);

        String vertexDisplayName = facts.get(VERTEX_DISPLAY_NAME);
        StringBuilder pathBuilder = facts.get(PATH_BUILDER);
        Set<String> pathSet = facts.get(PATH_SET);

        if (null != vertexDisplayName && null != pathBuilder && null != pathSet) {
            pathBuilder.append(vertexDisplayName);
            pathSet.add(pathBuilder.toString());
            pathBuilder.delete(0, pathBuilder.toString().length());

            pathBuilder.append(vertexDisplayName).append(" -> ");
        }
    }

    @Override
    public int getPriority() {
        return 0;
    }

}
