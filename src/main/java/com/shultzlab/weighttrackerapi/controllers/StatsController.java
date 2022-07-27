package com.shultzlab.weighttrackerapi.controllers;

import com.shultzlab.weighttrackerapi.exceptions.ResourceNotFoundException;
import com.shultzlab.weighttrackerapi.models.User;
import com.shultzlab.weighttrackerapi.models.WeightEntry;
import com.shultzlab.weighttrackerapi.repositories.UserRepository;
import com.shultzlab.weighttrackerapi.repositories.WeightEntryRepository;
import com.shultzlab.weighttrackerapi.services.StatsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/stats")
public class StatsController {
    final WeightEntryRepository weightEntryRepository;
    final UserRepository userRepository;

    public StatsController(WeightEntryRepository weightEntryRepository, UserRepository userRepository){
        this.weightEntryRepository = weightEntryRepository;
        this.userRepository = userRepository;
    }

    // https://www.k-state.edu/paccats/Contents/PA/PDF/Physical%20Activity%20and%20Controlling%20Weight.pdf

    @GetMapping("/bmr/{id}")
    public Map<String, Double> getBmrByUserId(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        // TODO verify user matches token
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        WeightEntry entry = this.weightEntryRepository.findFirstByUser(user);

        Double bmr = StatsService.calculateBMR(user, entry.getWeight());

        Map<String, Double> response = new HashMap<>();
        response.put("BMR", bmr);

        return response;
    }

    @GetMapping("/tdee/{id}")
    public Map<String, Double> getTdeeByUserId(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        // TODO verify user matches token
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        WeightEntry entry = this.weightEntryRepository.findFirstByUser(user);

        Double tdee = StatsService.calculateTDEE(user, entry.getWeight());
        Map<String, Double> response = new HashMap<>();
        response.put("TDEE", tdee);

        return response;

    }

}
