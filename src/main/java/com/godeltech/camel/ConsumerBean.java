package com.godeltech.camel;

import org.apache.camel.Body;

public class ConsumerBean {

    public void process(final @Body String body) {
        System.out.println("******************************");
        System.out.println("* " + body + " *");
        System.out.println("******************************");
    }
}
