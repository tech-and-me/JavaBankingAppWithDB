package com.wileyedge.exceptions;

public class CustomerAlreadyHasBankAccountLinkedException extends Exception {
    public CustomerAlreadyHasBankAccountLinkedException(String message) {
        super(message);
    }
}
