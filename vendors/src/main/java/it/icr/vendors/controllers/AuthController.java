package it.icr.vendors.controllers;

import it.icr.vendors.entities.Vendor;
import it.icr.vendors.requests.LoginRequest;
import it.icr.vendors.requests.auth.ResetPasswordRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
public interface AuthController {
    @PostMapping("/login")
    ResponseEntity<Object> login(@RequestBody LoginRequest request);

    @PostMapping("/reset-password")
    ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest request);

    @GetMapping
    ResponseEntity<Void> testToken();

    @PostMapping("/token/refresh")
    ResponseEntity<Object> refreshToken(@RequestParam String old_token);

    @PostMapping("/register")
    ResponseEntity<Object> register(@RequestBody String email);
}
