package com.project5.rcrsms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.project5.rcrsms.SessionRepository;

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
}