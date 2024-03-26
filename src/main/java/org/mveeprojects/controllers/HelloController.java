package org.mveeprojects.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.mveeprojects.services.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private ProxyService proxyService;

    @GetMapping("/proxy/{name}")
    public JsonNode proxy(@PathVariable String name) {
        return proxyService.apiResponses(name);
    }
}
