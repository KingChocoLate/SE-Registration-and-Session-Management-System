package com.project5.rcrsms.controller;

import com.project5.rcrsms.Entity.Conference;
import com.project5.rcrsms.Repository.ConferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/conferences")
public class ConferenceController {

    @Autowired
    private ConferenceRepository conferenceRepo;

    // 1. List All Conferences
    @GetMapping("") // Matches /conferences
    public String listConferences(Model model) {
        model.addAttribute("conferences", conferenceRepo.findAll());
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
        conferenceRepo.save(conference);
        return "redirect:/admin/dashboard"; // Redirect to dashboard after saving
    }
}