package com.sudipta.dropwizard.rule;

import static com.sudipta.dropwizard.client.JsonToDAG.PATH_BUILDER;
import static com.sudipta.dropwizard.client.JsonToDAG.VERTEX_DISPLAY_NAME;

import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.support.UnitRuleGroup;

public class NonJsonToDAGRule extends UnitRuleGroup {

    public NonJsonToDAGRule(Object... rules) {
        for (Object rule : rules) {
            addRule(rule);
        }
    }

    @Override
    public boolean evaluate(Facts facts) {
        return !super.evaluate(facts);
    }

    @Override
    public void execute(Facts facts) throws Exception {
        super.execute(facts);

        String vertexDisplayName = facts.get(VERTEX_DISPLAY_NAME);
        StringBuilder pathBuilder = facts.get(PATH_BUILDER);

        if (null != vertexDisplayName && null != pathBuilder) {
            pathBuilder.append(vertexDisplayName).append(" -> ");
        }
    }

    @Priority
    public int getPriority() {
        return 3;
    }
}
