package com.project5.rcrsms.controller;

import com.project5.rcrsms.Entity.Session;
import com.project5.rcrsms.Entity.Session.SessionStatus;
import com.project5.rcrsms.Repository.ConferenceRepository;
import com.project5.rcrsms.Repository.RoomRepository;
import com.project5.rcrsms.Repository.SessionRepository;
import com.project5.rcrsms.Repository.UserRepository;
import com.project5.rcrsms.Service.SessionService; // Import Service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')") 
public class AdminController {

    @Autowired private SessionRepository sessionRepo;
    @Autowired private SessionService sessionService; 
    @Autowired private ConferenceRepository conferenceRepo;
    @Autowired private RoomRepository roomRepo;
    @Autowired private UserRepository userRepo;

    // --- 1. DASHBOARD ---
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalSessions", sessionRepo.count());
        model.addAttribute("totalConferences", conferenceRepo.count());
        model.addAttribute("totalUsers", userRepo.count());
        
        // Count upcoming sessions properly
        long upcomingCount = sessionRepo.findAll().stream()
                .filter(s -> s.getSessionTime().isAfter(java.time.LocalDateTime.now()))
                .count();
                
        model.addAttribute("upcomingSessions", upcomingCount); 
        model.addAttribute("recentSessions", sessionRepo.findAll());
        return "admin/dashboard";
    }

    // --- 2. MANAGE SCHEDULE 
    @GetMapping("/schedule")
    public String schedule(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        List<Session> sessions;

        // Check if keyword is provided and not empty
        if (keyword != null && !keyword.isEmpty()) {
            sessions = sessionService.searchSessionsByTitle(keyword);
        } else {
            sessions = sessionService.getAllSessions();
        }
        
        model.addAttribute("sessions", sessions);
        model.addAttribute("keyword", keyword); 
        return "admin/schedule"; 
    }

    // --- 3. MANAGE USERS ---
    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "admin/users";
    }

    // Delete User Logic
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes ra) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
            ra.addFlashAttribute("successMessage", "User deleted successfully.");
        } else {
            ra.addFlashAttribute("errorMessage", "User not found.");
        }
        return "redirect:/admin/users";
    }

    // --- 4. SAVE SESSION ---
    @PostMapping("/sessions/save")
    public String saveSession(@ModelAttribute Session session) {
        session.setStatus(SessionStatus.APPROVED);
        sessionRepo.save(session);
        return "redirect:/admin/dashboard?success=Session Created";
    }
}