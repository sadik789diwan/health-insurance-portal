package com.insurance.hip.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DemoController {

    @GetMapping("/public")
    public String publicEndpoint() { return "Public info"; }

    @GetMapping("/user/profile")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
    public String userProfile() { return "User profile"; }

    @GetMapping("/admin/reports")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminReports() { return "Admin reports"; }

    @GetMapping("/underwriter/tasks")
    @PreAuthorize("hasRole('ROLE_UNDERWRITER')")
    public String underwriting() { return "Underwriter tasks"; }

    @GetMapping("/audit/logs")
    @PreAuthorize("hasRole('ROLE_AUDITOR') or hasRole('ROLE_ADMIN')")
    public String auditLogs() { return "Audit logs"; }
}
