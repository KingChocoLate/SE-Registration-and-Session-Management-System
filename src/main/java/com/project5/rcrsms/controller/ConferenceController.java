package com.project5.rcrsms.controller;

import com.project5.rcrsms.Entity.Conference;
import com.project5.rcrsms.Repository.ConferenceRepository;
import com.project5.rcrsms.Repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/conferences")
public class ConferenceController {

    @Autowired
    private ConferenceRepository conferenceRepo;
    
    // NEW: Inject SessionRepository so we can show sessions on the conference view
    @Autowired
    private SessionRepository sessionRepo;

    // 1. List All Conferences
    @GetMapping("") 
    public String listConferences(Model model) {
        model.addAttribute("conferences", conferenceRepo.findAll());
        return "conference/list"; 
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
        conferenceRepo.save(conference);
        return "redirect:/admin/dashboard"; 
    }
    
    // 4. NEW: View Conference Details (Fixes view.html)
    @GetMapping("/{id}")
    public String viewConference(@PathVariable Long id, Model model) {
        Conference conference = conferenceRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid conference Id:" + id));
            
        model.addAttribute("conference", conference);
        // Load sessions for this conference to display in the view
        model.addAttribute("sessions", sessionRepo.findByConferenceConferenceId(id));
        
        return "conference/view";
    }
}