package com.project5.rcrsms.controller;

import com.project5.rcrsms.Entity.Room;
import com.project5.rcrsms.Repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/rooms")
@PreAuthorize("hasRole('ADMIN')") // Security: Only Admins can access
public class RoomController {

    @Autowired
    private RoomRepository roomRepo;

    // 1. List all rooms & Show "Add" form
    @GetMapping
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomRepo.findAll());
        model.addAttribute("newRoom", new Room()); // Empty object for the form
        return "admin/rooms";
    }

    // 2. Handle the "Add Room" form submission
    @PostMapping("/save")
    public String saveRoom(@ModelAttribute Room room) {
        roomRepo.save(room);
        return "redirect:/admin/rooms";
    }

    // 3. Delete a room
    @GetMapping("/delete/{id}")
    public String deleteRoom(@PathVariable Long id) {
        roomRepo.deleteById(id);
        return "redirect:/admin/rooms";
    }
}