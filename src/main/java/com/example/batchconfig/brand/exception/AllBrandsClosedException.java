package com.example.batchconfig.brand.exception;

public class AllBrandsClosedException extends RuntimeException {

    public AllBrandsClosedException() {
        super("Cannot Do Transaction Because All Brands are Closed");
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
