package com.project5.rcrsms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("title", "Sign Up");
        return "auth/register";
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "error/403";
    }

    // --- MOCK DATA FOR MEMBER 4 TESTING ---
    @GetMapping("/sessions")
    public String listSessions(Model model) {
        List<Map<String, Object>> mockSessions = new ArrayList<>();
        
        mockSessions.add(Map.of(
            "id", 1,
            "title", "The Future of AI in Healthcare",
            "status", "APPROVED",
            "submitter", Map.of("username", "Dr. Alan Turing")
        ));

        mockSessions.add(Map.of(
            "id", 2,
            "title", "Sustainable Energy Grid Systems",
            "status", "PENDING",
            "submitter", Map.of("username", "Tesla_Fan_99")
        ));

        model.addAttribute("sessions", mockSessions);
        return "session/list";
    }
    
    @GetMapping("/sessions/edit/{id}")
    public String editSession() {
        return "index"; 
    }

    @GetMapping("/sessions/submit")
    public String showSubmitForm(Model model) {
        return "session/create";

    @org.springframework.web.bind.annotation.PostMapping("/sessions/submit")
    public String handleSubmission(@org.springframework.web.bind.annotation.RequestParam String title,
                                   @org.springframework.web.bind.annotation.RequestParam String abstractText) {
        
        // Simulating a database save (Member 2 will do the real logic later)
        System.out.println("New Session Submitted:");
        System.out.println("Title: " + title);
        System.out.println("Abstract: " + abstractText);

        // Redirect back to the list page after success
        return "redirect:/sessions";
    }

    // *** NEW: ADMIN DASHBOARD ***
    @GetMapping("/admin/schedule")
    public String adminSchedule() {
        return "admin/schedule";
    }
}