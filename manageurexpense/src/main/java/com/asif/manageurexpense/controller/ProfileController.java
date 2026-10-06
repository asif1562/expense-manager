package com.asif.manageurexpense.controller;

import com.asif.manageurexpense.dto.AuthDto;
import com.asif.manageurexpense.dto.ProfileDto;
import com.asif.manageurexpense.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/register")
    public ResponseEntity<ProfileDto> registerProfile(@RequestBody ProfileDto profileDto){
        ProfileDto registerProfile = profileService.registerProfile(profileDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerProfile);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateProfile(@RequestParam String token){
        boolean isActive = profileService.activateProfile(token);
        if(isActive){
            return ResponseEntity.ok("profile created successfully");
        } else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation token not found or already used");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String , Object>> login(@RequestBody AuthDto authDto){
        try {
            if (!profileService.isAccountActive(authDto.getEmail())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                        "message","Account is not active.Please check your email and activate your account first."
                ));
            }
            Map<String , Object> response = profileService.authenticateAndGenerate(authDto);
            return ResponseEntity.ok(response);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message",e.getMessage()
            ));
        }
    }


    @GetMapping("/profile")
    public ResponseEntity<ProfileDto> getPublicProfile() {
        ProfileDto profileDto = profileService.getPublicProfile(null);
        return ResponseEntity.ok(profileDto);
    }
    @GetMapping("/test")
    public String test(){
        return "Test successful";
    }
}
