package com.project5.rcrsms.controller;

import com.project5.rcrsms.Entity.UserEntity;
import com.project5.rcrsms.Repository.SessionRepository;
import com.project5.rcrsms.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/chair")
@PreAuthorize("hasRole('CHAIR')")
public class ChairController {

    @Autowired private SessionRepository sessionRepo;
    @Autowired private UserRepository userRepo;

    @GetMapping("/my-sessions")
    public String mySessions(Model model, Principal principal) {
        UserEntity chair = userRepo.findByUsername(principal.getName()).orElseThrow();
        // Find sessions assigned to this specific chair
        model.addAttribute("sessions", sessionRepo.findByChair(chair));
        return "chair/my_sessions"; // Points to templates/chair/my_sessions.html
    }
}