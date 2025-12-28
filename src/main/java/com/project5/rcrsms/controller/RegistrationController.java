package com.project5.rcrsms.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project5.rcrsms.Entity.Registration;
import com.project5.rcrsms.Repository.RegistrationRepository;
import com.project5.rcrsms.Repository.SessionRepository;
import com.project5.rcrsms.Entity.UserEntity;
import com.project5.rcrsms.Repository.UserRepository;

import java.security.Principal;
@Controller
@RequestMapping("/registrations")
public class RegistrationController {
    @Autowired private RegistrationRepository registrationRepo;
    @Autowired private SessionRepository sessionRepo;
    @Autowired private UserRepository userRepo;

    @PostMapping("/add")
    // public String register(@RequestParam Long sessionId, Principal principal, RedirectAttributes ra) {
    public String register(@RequestParam("sessionId") Long sessionId, Principal principal, RedirectAttributes ra){
        UserEntity user = userRepo.findByUsername(principal.getName()).orElseThrow(() -> new IllegalStateException("Authenticated user not found: " + principal.getName()));;

        // LOGIC: Prevent duplicate
        if (registrationRepo.existsByUser_UserIdAndSession_SessionId(user.getUserId(), sessionId)) {
            ra.addFlashAttribute("error", "You are already registered for this session.");
            return "redirect:/sessions";
        }

        Registration reg = new Registration();
        reg.setUser(user);
        reg.setSession(sessionRepo.findById(sessionId).orElse(null));
        registrationRepo.save(reg);

        ra.addFlashAttribute("success", "Registration successful!");
        return "redirect:/sessions";
    }

    @GetMapping("/view/{id}")
    public String viewParticipants(@PathVariable("id") Long id, Model model) {
        model.addAttribute("participants", registrationRepo.findBySession_SessionId(id));
        return "registration-view";
    }
}