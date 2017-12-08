package com.godeltech.camel;

import org.apache.camel.Body;

public class LoggerBean {

    private final String message;

    public LoggerBean(String message) {
        this.message = message;
    }

    public void process(final @Body String body) {
        System.out.println("******************************");
        System.out.println("* " + body + " *");
        System.out.println("******************************");
    }
}
