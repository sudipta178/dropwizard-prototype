package com.sudipta.dropwizard;

import com.sudipta.dropwizard.client.JsonToTree;
import com.sudipta.dropwizard.config.DropwizardPrototypeConfiguration;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DropwizardPrototypeApplication extends Application<DropwizardPrototypeConfiguration> {
    private static final Logger logger = LoggerFactory.getLogger(DropwizardPrototypeApplication.class);

    public static void main(String[] args) throws Exception {
        logger.info("Starting application ...");
        JsonToTree jsonToTree = new JsonToTree();
        jsonToTree.getJsonTreePaths();
        //new DropwizardPrototypeApplication().run(args);
    }

    @Override
    public String getName() {
        return "dropwizard-prototype";
    }

    @Override
    public void initialize(Bootstrap<DropwizardPrototypeConfiguration> bootstrap) {
    }

    @Override
    public void run(DropwizardPrototypeConfiguration dropwizardPrototypeConfiguration, Environment environment) throws Exception {

    }
}
