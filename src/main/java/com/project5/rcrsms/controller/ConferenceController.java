package com.project5.rcrsms.controller;

import com.project5.rcrsms.Entity.Conference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.project5.rcrsms.Service.ConferenceService;
import com.project5.rcrsms.Service.SessionService;

@Controller
@RequestMapping("/conferences")
public class ConferenceController {

    @Autowired
    private ConferenceService conferenceService;
    @Autowired
    private SessionService sessionService;

    // 1. List All Conferences
    @GetMapping("") 
    public String listConferences(Model model) {
        model.addAttribute("conferences", conferenceService.getAllConferences());
        return "conference/list"; // You'll need to create this file later if you want a public list
    }

    // 2. Show Create Form
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("conference", new Conference());
        return "conference/create";
    }

    // 3. Save Conference
    @PostMapping("/save")
    public String saveConference(@ModelAttribute("conference") Conference conference) {
        conferenceService.createConference(conference);
        return "redirect:/admin/dashboard"; 
    }
    
    // 4. NEW: View Conference Details (Fixes view.html)
    @GetMapping("/{id}")
    public String viewConference(@PathVariable Long id, Model model) {
        Conference conference = conferenceService.getConferenceById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid conference Id:" + id));
            
        model.addAttribute("conference", conference);
        // Load sessions for this conference to display in the view
        model.addAttribute("sessions", sessionService.getSessionsByConferenceId(id));
        
        return "conference/view";
    }
}