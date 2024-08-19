package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.example.demo.controller.AccessController;
import com.example.demo.dto.AddAccessRequest;
import com.example.demo.dto.ResultResp;
import com.example.demo.model.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@SpringBootTest
class DemoApplicationTests {

	@Resource
	private AccessController accessController;

	@Test
	void contextLoads() {
	}

	@Test
	void add() {
		AddAccessRequest addAccessRequest = new AddAccessRequest();
		addAccessRequest.setUserId(123456);
		List<String> endpoint = new ArrayList<>();
		endpoint.add("resource A");
		endpoint.add("resource B");
		addAccessRequest.setEndpoint(endpoint);
		User user = new User();
		user.setUserId(123);
		user.setAccountName("管理员");
		user.setRole("admin");
		String s = Base64.getEncoder().encodeToString(JSON.toJSONString(user).getBytes());
		ResultResp resultResp = accessController.add(addAccessRequest, s);
		assert resultResp.isSuccessful();
	}

	@Test
	void addAppend() {
		AddAccessRequest addAccessRequest = new AddAccessRequest();
		addAccessRequest.setUserId(1234567);
		List<String> endpoint = new ArrayList<>();
		endpoint.add("resource C");
		endpoint.add("resource D");
		addAccessRequest.setEndpoint(endpoint);
		User user = new User();
		user.setUserId(123);
		user.setAccountName("管理员");
		user.setRole("admin");
		String s = Base64.getEncoder().encodeToString(JSON.toJSONString(user).getBytes());
		ResultResp resultResp = accessController.add(addAccessRequest, s);
		assert resultResp.isSuccessful();
	}


	@Test
	void addByNoAdmin() {
		AddAccessRequest addAccessRequest = new AddAccessRequest();
		addAccessRequest.setUserId(123456);
		List<String> endpoint = new ArrayList<>();
		endpoint.add("resource C");
		endpoint.add("resource D");
		addAccessRequest.setEndpoint(endpoint);
		User user = new User();
		user.setUserId(1234);
		user.setAccountName("普通用户");
		user.setRole("user");
		String s = Base64.getEncoder().encodeToString(JSON.toJSONString(user).getBytes());
		ResultResp resultResp = accessController.add(addAccessRequest, s);
		assert !resultResp.isSuccessful();
	}

	@Test
	void getResourceT() {
		/*AddAccessRequest addAccessRequest = new AddAccessRequest();
		addAccessRequest.setUserId(123456);
		List<String> endpoint = new ArrayList<>();
		endpoint.add("resource C");
		endpoint.add("resource D");
		addAccessRequest.setEndpoint(endpoint);*/
		User user = new User();
		user.setUserId(123456);
		user.setAccountName("普通用户");
		user.setRole("user");
		String s = Base64.getEncoder().encodeToString(JSON.toJSONString(user).getBytes());
		ResultResp resultResp = accessController.getResource("resource A", s);
		assert resultResp.isSuccessful();
	}

	@Test
	void getResourceF() {
		/*AddAccessRequest addAccessRequest = new AddAccessRequest();
		addAccessRequest.setUserId(123456);
		List<String> endpoint = new ArrayList<>();
		endpoint.add("resource C");
		endpoint.add("resource D");
		addAccessRequest.setEndpoint(endpoint);*/
		User user = new User();
		user.setUserId(123456);
		user.setAccountName("普通用户");
		user.setRole("user");
		String s = Base64.getEncoder().encodeToString(JSON.toJSONString(user).getBytes());
		ResultResp resultResp = accessController.getResource("resource C", s);
		assert !resultResp.isSuccessful();
	}

}
