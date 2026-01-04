package com.project5.rcrsms.controller;

import com.project5.rcrsms.Entity.Session;
import com.project5.rcrsms.Entity.Session.SessionStatus;
import com.project5.rcrsms.Repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/approvals")
@PreAuthorize("hasAnyRole('ADMIN', 'CHAIR')")
public class ApprovalController {

    @Autowired
    private SessionRepository sessionRepo;

    // 1. Show all PENDING sessions
    @GetMapping
    public String showApprovals(Model model) {
        // We assume you added the 'status' field from Fix #1
        // If query fails, ensure you updated SessionRepository to include findByStatus(SessionStatus status)
        // For now, let's filter manually if repo isn't updated, or use findAll
        model.addAttribute("pendingSessions", sessionRepo.findAll().stream()
            .filter(s -> s.getStatus() == SessionStatus.PENDING)
            .toList());
        return "admin/approvals";
    }

    // 2. Approve a session
    @PostMapping("/{id}/approve")
    public String approveSession(@PathVariable Long id) {
        Session session = sessionRepo.findById(id).orElseThrow();
        session.setStatus(SessionStatus.APPROVED);
        sessionRepo.save(session);
        return "redirect:/admin/approvals";
    }

    // 3. Reject a session
    @PostMapping("/{id}/reject")
    public String rejectSession(@PathVariable Long id) {
        Session session = sessionRepo.findById(id).orElseThrow();
        session.setStatus(SessionStatus.REJECTED);
        sessionRepo.save(session);
        return "redirect:/admin/approvals";
    }
}