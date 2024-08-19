package com.example.demo.controller;

import ch.qos.logback.core.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dto.AddAccessRequest;
import com.example.demo.dto.ResultResp;
import com.example.demo.model.User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


@RestController
public class AccessController {

    @PostMapping(value = "/admin/addUser")
    public ResultResp<String> add(@RequestBody AddAccessRequest addAccessRequest, @RequestHeader String userStr) {
        ResultResp<String> resultResp = new ResultResp<>();
        User user = getUser(userStr);
        if (user == null) {
            resultResp.setSuccessful(false);
            resultResp.setMessage("请求用户信息为空");
            return resultResp;
        }
        if (!"admin".equals(user.getRole())) {
            resultResp.setSuccessful(false);
            resultResp.setMessage("用户权限不足");
            return resultResp;
        }
        // 获取原来的权限数据
        String accessStr;
        try {
            accessStr = new String(Files.readAllBytes(Paths.get("accessData.txt")));
        } catch (IOException e) {
            e.printStackTrace();
            resultResp.setSuccessful(false);
            resultResp.setMessage("数据读取异常");
            return resultResp;
        }
        // 修改权限数据
        Map<Integer, List<String>> accessMap;
        if (StringUtil.isNullOrEmpty(accessStr)) {
            accessMap = new HashMap<>();
            accessMap.put(addAccessRequest.getUserId(), new ArrayList<>(addAccessRequest.getEndpoint()));
        } else {
            accessMap = JSONObject.parseObject(accessStr, Map.class);
            if (accessMap.get(addAccessRequest.getUserId()) == null) {
                accessMap.put(addAccessRequest.getUserId(), new ArrayList<>(addAccessRequest.getEndpoint()));
            } else {
                List list = accessMap.get(addAccessRequest.getUserId());
                list.addAll(addAccessRequest.getEndpoint());
                list = list.stream().distinct().toList();
                accessMap.put(addAccessRequest.getUserId(), list);
            }
        }
        //  写入新的权限数据
        try {
            Files.writeString(Paths.get("accessData.txt"), JSON.toJSONString(accessMap));
        } catch (IOException e) {
            e.printStackTrace();
            resultResp.setSuccessful(false);
            resultResp.setMessage("数据写入异常");
            return resultResp;
        }
        resultResp.setSuccessful(true);
        resultResp.setMessage("success");
        return resultResp;
    }

    private User getUser(String userStr) {
        if (StringUtil.isNullOrEmpty(userStr)){
            return null;
        }
        byte[] decode = Base64.getDecoder().decode(userStr);
        String userS = new String(decode);
        return JSON.parseObject(userS, User.class);
    }

    @PostMapping(value = "/user/{resource}")
    public ResultResp<String> getResource(@PathVariable("resource") String resource, @RequestHeader String userStr) {
        ResultResp<String> resultResp = new ResultResp<>();

        User user = getUser(userStr);
        if (user == null) {
            resultResp.setSuccessful(false);
            resultResp.setMessage("请求用户信息为空");
            return resultResp;
        }

        String accessStr;
        try {
            accessStr = new String(Files.readAllBytes(Paths.get("accessData.txt")));
        } catch (IOException e) {
            e.printStackTrace();
            resultResp.setSuccessful(false);
            resultResp.setMessage("数据读取异常");
            return resultResp;
        }

        if (StringUtil.isNullOrEmpty(accessStr)) {
            resultResp.setSuccessful(false);
            resultResp.setMessage("用户无该资源权限");
            return resultResp;
        }
        Map<Integer, List<String>> accessMap = JSONObject.parseObject(accessStr, Map.class);
        if (accessMap.get(user.getUserId()) == null){
            resultResp.setSuccessful(false);
            resultResp.setMessage("用户无该资源权限");
            return resultResp;
        }
        List<String> accessList = accessMap.get(user.getUserId());
        boolean b = accessList.stream().anyMatch(it -> it.equals(resource));
        resultResp.setSuccessful(b);
        if (b) {resultResp.setMessage("该用户拥有此权限");}
        else {
            resultResp.setMessage("用户无该资源权限");
        }
        return resultResp;

    }
}
