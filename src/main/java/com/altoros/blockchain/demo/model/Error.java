package com.altoros.blockchain.demo.model;

/**
 * @author Nikita Gorbachevski
 */
public class Error {

    private final String message;

    public Error(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
