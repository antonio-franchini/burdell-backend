package com.antosoft.burdellbackend.controller;

import com.antosoft.burdellbackend.dto.Profile;
import com.antosoft.burdellbackend.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ProfileController {

    @Autowired
    private ProfileRepository profileRepository;

    @PostMapping("/login")
    public Profile login(@RequestBody Profile profile) {
        return profileRepository.login(profile);
    }

}
