package ru.venidiktov.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AminController {

    @GetMapping("/admin")
    public String adminPage() {
        return "admin/admin";
    }
}
