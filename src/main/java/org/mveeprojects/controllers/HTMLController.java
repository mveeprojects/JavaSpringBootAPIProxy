package org.mveeprojects.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mveeprojects.services.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HTMLController {

    @Autowired
    private ProxyService proxyService;

    @RequestMapping("customer/summary/{id}")
    public String indexController(@PathVariable String id, Model model) {
        ObjectMapper mapper = new ObjectMapper();
        model.addAttribute("title", "indexController");
        model.addAttribute("content_heading", "Library API results for id #" + id);

        JsonNode customerInfo = proxyService.apiResponse("customer/info", id);
        JsonNode customerHistory = proxyService.apiResponse("customer/history", id);

        model.addAttribute("customerInfo", customerInfo.toPrettyString());
        model.addAttribute("customerHistory", customerHistory.toPrettyString());
        return "index";
    }
}
