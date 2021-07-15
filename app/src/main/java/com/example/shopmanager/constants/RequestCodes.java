package com.example.shopmanager.constants;

public enum RequestCodes {
    REGISTER(10);
    private final int request;

    RequestCodes(int i) {
        request = i;
    }

    public int getCode() {
        return this.request;
    }
}
