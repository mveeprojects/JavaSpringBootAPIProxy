package org.mveeprojects.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mveeprojects.services.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
public class HTMLController {

    @Autowired
    private ProxyService proxyService;

    @RequestMapping("/info/{name}")
    public String indexController(@PathVariable String name, Model model) {
        ObjectMapper mapper = new ObjectMapper();
        model.addAttribute("title", "indexController");
        model.addAttribute("content_heading", "API results for: " + name);

        JsonNode employeeJsonResponse = proxyService.apiResponse("employee", name);
        JsonNode addressesJsonResponse = proxyService.apiResponse("addresses", name);

        model.addAttribute("apiresponse1", employeeJsonResponse.toPrettyString());
        model.addAttribute("apiresponse2", addressesJsonResponse.toPrettyString());
        return "index";
    }
}
