package com.mphasis.cqrs.os.query.exceptions;

public class PreconditionFailed extends Exception {
    public PreconditionFailed(String msg) {
        super(msg);
    }
}
