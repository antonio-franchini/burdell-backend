package com.antosoft.burdellbackend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BurdellBackendController {

    @GetMapping("/burdellbackend/{name}")
    public String burdellbackend(@PathVariable String name) {
        return "Hello " + name + ", welcome to Burdell's Ramblin' Wrecks";
    }
}


