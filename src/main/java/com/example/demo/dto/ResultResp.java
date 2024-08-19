package com.example.demo.dto;

import java.io.Serializable;

public class ResultResp<T> implements Serializable {
    private boolean isSuccessful;
    private String message;
    private T data;

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
