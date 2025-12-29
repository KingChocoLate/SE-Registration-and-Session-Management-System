package com.project5.rcrsms.controller;

import com.project5.rcrsms.Service.SessionService;
import com.project5.rcrsms.Repository.ConferenceRepository;
import com.project5.rcrsms.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private SessionService sessionService;
    
    // Inject repositories directly for simple stats (or use Services if you have them)
    @Autowired
    private ConferenceRepository conferenceRepository;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // 1. Fetch Stats for the Cards
        model.addAttribute("totalSessions", sessionService.countSessions());
        model.addAttribute("totalConferences", conferenceRepository.count());
        model.addAttribute("totalUsers", userRepository.count());
        
        // 2. Fetch Recent Sessions (Just getting all for now, you can limit this later)
        model.addAttribute("recentSessions", sessionService.getAllSessions());
        
        return "admin/dashboard";
    }

    @GetMapping("/schedule")
    public String schedule(Model model) {
        // Fetch all sessions to display in the table
        model.addAttribute("sessions", sessionService.getAllSessions());
        return "admin/schedule";
    }
}