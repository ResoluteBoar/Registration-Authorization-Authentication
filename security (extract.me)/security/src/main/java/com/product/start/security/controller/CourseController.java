package com.product.start.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1.0/courses")
public class CourseController {

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminStuff() {
        return "Admin stuff";
    }
    @GetMapping("/user")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public String userStuff() {
        return "User stuff";
    }

}
