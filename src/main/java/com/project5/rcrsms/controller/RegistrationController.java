package com.project5.rcrsms.controller;

import com.project5.rcrsms.Entity.Registration;
import com.project5.rcrsms.Service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest; // Import this

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/registrations")
public class RegistrationController {

    @Autowired private RegistrationService registrationService;

    // --- 1. JOIN SESSION ---
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/join/{sessionId}")
    public String joinSession(@PathVariable Long sessionId, 
                              Principal principal, 
                              RedirectAttributes ra,
                              HttpServletRequest request) { // Add Request
        try {
            registrationService.registerUser(principal.getName(), sessionId);
            ra.addFlashAttribute("successMessage", "Registration successful! You are now attending this session.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        // FIX: Redirect back to the page the user came from (Catalog or Details)
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/conferences"); 
    }

    // --- 2. CANCEL REGISTRATION ---
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/cancel/{regId}")
    public String cancelRegistration(@PathVariable Long regId, Principal principal, RedirectAttributes ra) {
        try {
            registrationService.cancelRegistration(principal.getName(), regId);
            ra.addFlashAttribute("successMessage", "Registration cancelled.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/registrations/my-schedule";
    }

    // --- 3. VIEW MY SCHEDULE ---
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my-schedule")
    public String viewMySchedule(Model model, Principal principal) {
        List<Registration> myRegs = registrationService.getMyRegistrations(principal.getName());
        model.addAttribute("registrations", myRegs);
        return "participant/my_schedule";
    }

    // --- 4. ADMIN/CHAIR REMOVE PARTICIPANT ---
    @PreAuthorize("hasAnyRole('ADMIN', 'CHAIR')")
    @PostMapping("/remove/{regId}")
    public String removeParticipant(@PathVariable Long regId, HttpServletRequest request, RedirectAttributes ra) {
        try {
            registrationService.deleteRegistration(regId);
            ra.addFlashAttribute("successMessage", "Participant removed from session.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        // Redirect back to where they clicked (Dashboard or List)
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}