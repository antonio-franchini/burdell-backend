package com.antosoft.burdellbackend.service;

import com.antosoft.burdellbackend.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private VehicleRepository profileRepository;

//    public Profile createProfile(Profile profile) {
//        return profileRepository.save(profile);
//    }
//
//    public List<Profile> getProfiles() {
//        List<Profile> profileList = new ArrayList<>();
//        profileRepository.findAll().forEach(profile -> profileList.add(profile));
//        return profileList;
//    }

}
