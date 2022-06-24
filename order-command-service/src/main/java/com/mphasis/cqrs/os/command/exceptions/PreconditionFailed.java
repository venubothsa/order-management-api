package com.mphasis.cqrs.os.command.exceptions;

public class PreconditionFailed extends Exception {
    public PreconditionFailed(String msg) {
        super(msg);
    }
}
