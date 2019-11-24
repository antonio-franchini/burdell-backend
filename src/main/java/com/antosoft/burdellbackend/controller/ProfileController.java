package com.antosoft.burdellbackend.controller;

import com.antosoft.burdellbackend.dto.Profile;
import com.antosoft.burdellbackend.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProfileController {

    @Autowired
    private ProfileRepository profileRepository;

    @PostMapping("/login")
    public Profile login(@RequestBody Profile profile) {
        return profileRepository.login(profile);
    }

    @GetMapping("/getSaleperson/{vin}")
    public Profile getSaleperson(@PathVariable String vin) {
        return profileRepository.getSaleperson(vin);
    }

    @GetMapping("/getInventoryClerk/{vin}")
    public Profile getInventoryClerk(@PathVariable String vin) {
        return profileRepository.getInventoryClerk(vin);
    }

}
