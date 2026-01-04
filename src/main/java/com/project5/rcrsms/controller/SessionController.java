package com.project5.rcrsms.controller;

import com.project5.rcrsms.Entity.Session;
import com.project5.rcrsms.Entity.Session.SessionStatus;
import com.project5.rcrsms.Entity.UserEntity;
import com.project5.rcrsms.Repository.ConferenceRepository;
import com.project5.rcrsms.Repository.RoomRepository;
import com.project5.rcrsms.Repository.SessionRepository;
import com.project5.rcrsms.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/sessions")
public class SessionController {

    @Autowired
    private SessionRepository sessionRepo;
    
    @Autowired
    private ConferenceRepository conferenceRepo;
    
    @Autowired
    private RoomRepository roomRepo;
    
    @Autowired
    private UserRepository userRepo;

    // --- PUBLIC: View Session Catalog ---
    @GetMapping("")
    public String listSessions(Model model) {
        // Filter to show ONLY Approved sessions
        // This ensures users don't see "Pending" submissions in the main catalog
        var approvedSessions = sessionRepo.findAll().stream()
            .filter(s -> s.getStatus() == SessionStatus.APPROVED)
            .toList();
            
        model.addAttribute("sessions", approvedSessions);
        return "session/list"; 
    }

    // --- RESEARCHER: Show Submission Form ---
    @GetMapping("/submit")
    public String showSubmitForm(Model model) {
        // We create a new empty session for the form
        model.addAttribute("session", new Session());
        return "session/submit"; // Uses templates/session/submit.html
    }

    // --- RESEARCHER: Process Submission ---
    @PostMapping("/submit")
    public String submitSession(@RequestParam("title") String title,
                                @RequestParam("abstract") String abstractText,
                                Principal principal) {
        
        Session session = new Session();
        session.setTitle(title);
        session.setProposalAbstract(abstractText);
        session.setStatus(SessionStatus.PENDING); // Default to PENDING for Chair review
        
        // Assign the currently logged-in user as the "Chair" (Author/Submitter in this context)
        // Note: In a real app, you might have separate 'author' and 'chair' fields.
        // For this project, we'll use the logged-in user.
        if (principal != null) {
            UserEntity user = userRepo.findByUsername(principal.getName()).orElse(null);
            session.setChair(user); 
        }

        sessionRepo.save(session);
        return "redirect:/sessions?success=Abstract Submitted Successfully";
    }

    // --- ADMIN: Show Schedule Form ---
    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateForm(Model model) {
        model.addAttribute("session", new Session());
        model.addAttribute("conferences", conferenceRepo.findAll());
        model.addAttribute("rooms", roomRepo.findAll());
        // Filter users to only show those with CHAIR role (if you want)
        model.addAttribute("chairs", userRepo.findAll()); 
        return "session/create"; // Uses templates/session/create.html
    }

    // --- ADMIN: Save Schedule ---
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveSession(@ModelAttribute("session") Session session) {
        // If it's an admin creating it, auto-approve it
        if (session.getStatus() == null) {
            session.setStatus(SessionStatus.APPROVED);
        }
        sessionRepo.save(session);
        return "redirect:/admin/dashboard?success=Session Scheduled";
    }
    
    // --- ADMIN: Edit Session ---
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editSession(@PathVariable Long id, Model model) {
        Session session = sessionRepo.findById(id).orElseThrow();
        model.addAttribute("session", session);
        model.addAttribute("conferences", conferenceRepo.findAll());
        model.addAttribute("rooms", roomRepo.findAll());
        model.addAttribute("chairs", userRepo.findAll());
        return "session/create"; // Reuses the create form
    }

    // --- ADMIN: Delete Session ---
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteSession(@PathVariable Long id) {
        sessionRepo.deleteById(id);
        return "redirect:/admin/schedule";
    }
    
    // --- DETAILS: View Single Session ---
    @GetMapping("/{id}")
    public String viewSession(@PathVariable Long id, Model model) {
        Session session = sessionRepo.findById(id).orElseThrow();
        model.addAttribute("session", session);
        return "session/view"; // You might need to create this file if it doesn't exist
    }
}