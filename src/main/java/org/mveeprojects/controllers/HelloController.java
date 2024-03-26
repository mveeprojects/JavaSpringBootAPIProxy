package org.mveeprojects.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mveeprojects.services.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;

@RestController
public class HelloController {

    @Autowired
    private ProxyService proxyService;

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/bob")
    public String bob() {
        return "Greetings from Bob!";
    }

    @GetMapping("/proxy/{name}")
    public List<String> proxy(@PathVariable String name) throws URISyntaxException {
        return proxyService.apiResponses(name);
    }
}
