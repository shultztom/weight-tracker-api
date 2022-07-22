package com.shultzlab.weighttrackerapi.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/entry")
public class WeightEntryController {

    @GetMapping()
    public String getAllWeightEntries(){
        return "YOYO";
    }
}

