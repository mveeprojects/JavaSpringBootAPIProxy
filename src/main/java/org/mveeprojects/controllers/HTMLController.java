package org.mveeprojects.controllers;

import org.mveeprojects.model.HttpResult;
import org.mveeprojects.services.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HTMLController {

    @Autowired
    private ProxyService proxyService;

    @RequestMapping("/")
    public String indexController(Model model) {
        model.addAttribute("title", "Library Customer Summary");
        return "index_with_search";
    }

    @RequestMapping("/customer/summary")
    public String customerSummaryController(@RequestParam String search, Model model) {
        HttpResult customerInfo = proxyService.apiResponse("customer/info", search);
        HttpResult customerHistory = proxyService.apiResponse("customer/history", search);
        List<HttpResult> results = new ArrayList<>();
        results.add(customerInfo);
        results.add(customerHistory);
        model.addAttribute("search", results);
        return "index_with_search";
    }
}
