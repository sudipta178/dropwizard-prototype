#!/bin/bash -x

mvn clean install
java -jar target/dropwizard-prototype-1.0.1.jar server


