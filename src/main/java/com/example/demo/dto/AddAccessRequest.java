package com.example.demo.dto;

import java.io.Serializable;
import java.util.List;

public class AddAccessRequest implements Serializable {

    private Integer userId;
    private List<String> endpoint;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<String> getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(List<String> endpoint) {
        this.endpoint = endpoint;
    }
}
