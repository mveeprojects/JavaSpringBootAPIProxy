package org.mveeprojects.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HTMLController {

    @RequestMapping("/")
    public String indexController(Model model) {
        model.addAttribute("title", "indexController");
        model.addAttribute("content", "Hello from the index controller!");
        return "index";
    }
}
