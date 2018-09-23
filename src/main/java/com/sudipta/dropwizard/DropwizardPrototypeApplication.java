package com.sudipta.dropwizard;

import com.sudipta.dropwizard.client.JsonToTree;
import com.sudipta.dropwizard.config.DropwizardPrototypeConfiguration;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class DropwizardPrototypeApplication extends Application<DropwizardPrototypeConfiguration> {

    public static void main(String[] args) throws Exception {
        JsonToTree jsonToTree = new JsonToTree();
        jsonToTree.getJsonTreePaths();
        //new DropwizardPrototypeApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<DropwizardPrototypeConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(DropwizardPrototypeConfiguration dropwizardPrototypeConfiguration, Environment environment) throws Exception {

    }
}
