package com.project5.rcrsms.controller;

import com.project5.rcrsms.Entity.Session;
import com.project5.rcrsms.Repository.ConferenceRepository;
import com.project5.rcrsms.Repository.SessionRepository;
import com.project5.rcrsms.Repository.UserRepository;
import com.project5.rcrsms.Repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;


import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/sessions")
public class SessionController {

    @Autowired
    private SessionRepository sessionRepo;

    @Autowired
    private ConferenceRepository conferenceRepo;

    @Autowired
    private UserRepository userRepo;

    // 2. NEW: Inject the RoomRepository
    @Autowired
    private RoomRepository roomRepo; 
    
    // 1. List all sessions (Public View)
    @GetMapping("/list")
    public String listSessions(Model model) {
        model.addAttribute("sessions", sessionRepo.findAll());
        return "session/list";
    }

    @GetMapping("/submit")
    public String showSubmitForm(Model model) {
        model.addAttribute("session", new Session());
        return "session/create";
    }
    @PostMapping("/submit")
    // public String handleSubmission(@RequestParam String title,
    //                                @RequestParam String abstractText) {
        
    //     // Simulating a database save (Member 2 will do the real logic later)
    //     System.out.println("New Session Submitted:");
    //     System.out.println("Title: " + title);
    //     System.out.println("Abstract: " + abstractText);

    //     // Redirect back to the list page after success
    //     return "redirect:/sessions";
    // }
    public String handleSubmission(@Valid @ModelAttribute("session") Session session, 
                               BindingResult result, 
                               RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "session/create"; 
        }
        // Save the session to the database
        sessionRepo.save(session);
        // 3. Add a success message to show on the sessions list page
        ra.addFlashAttribute("success", "Session '" + session.getTitle() + "' has been submitted successfully!");
        return "redirect:/sessions";
    }
    
    // 2. Show Create Form/
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("session", new Session());
        model.addAttribute("conferences", conferenceRepo.findAll());
        model.addAttribute("chairs", userRepo.findAll());
        
        // 3. NEW: Add Rooms to the model so the dropdown works
        model.addAttribute("rooms", roomRepo.findAll()); 
        
        return "session/create";
    }

    // 3. Handle Create/Update Submission
    @PostMapping("/save")
    public String saveSession(@ModelAttribute("session") Session session) {
        if (session.getSessionTime() == null) {
            session.setSessionTime(LocalDateTime.now());
        }
        
        sessionRepo.save(session);
        return "redirect:/admin/schedule";
    }
    
}