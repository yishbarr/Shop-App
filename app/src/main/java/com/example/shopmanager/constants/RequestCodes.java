package com.example.shopmanager.constants;

public enum RequestCodes {
    register(10);

    private final int request;

    RequestCodes(int i) {
        request = i;
    }

    public int getCode() {
        return this.request;
    }
}
