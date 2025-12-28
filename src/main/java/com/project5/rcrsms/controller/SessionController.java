package com.project5.rcrsms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project5.rcrsms.Repository.SessionRepository;

@Controller
public class SessionController {

    @Autowired
    private SessionRepository sessionRepo;

    @GetMapping("/sessions")
    public String listSessions(Model model) {
        // Fetches all sessions from DB to display in the list
        model.addAttribute("sessions", sessionRepo.findAll());
        return "sessions";
    }

    @GetMapping("/sessions/edit/{id}")
    public String editSession() {
        return "index"; 
    }

    @GetMapping("/sessions/submit")
    public String showSubmitForm(Model model) {
        return "session/create";
    }
    @PostMapping("/sessions/submit")
    public String handleSubmission(@RequestParam String title,
                                   @RequestParam String abstractText) {
        
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