package com.project5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "Welcome to ConfSys");
        return "index"; 
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login"; 
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "error/403"; 
    }

    
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("title", "Register New User");
        return "auth/register"; 
    }


}