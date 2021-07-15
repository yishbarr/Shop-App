package com.example.shopmanager.constants;

public enum ServerResponses {
    SUCCESS(0),
    DATABASE_FAILED(1),
    ID_EXISTS(2);

    private final int response;

    ServerResponses(int response) {
        this.response = response;
    }

    public int getResponse() {
        return response;
    }
}
