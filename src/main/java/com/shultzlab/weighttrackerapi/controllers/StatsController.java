package com.shultzlab.weighttrackerapi.controllers;

import com.shultzlab.weighttrackerapi.exceptions.ResourceNotFoundException;
import com.shultzlab.weighttrackerapi.models.User;
import com.shultzlab.weighttrackerapi.models.WeightEntry;
import com.shultzlab.weighttrackerapi.repositories.UserRepository;
import com.shultzlab.weighttrackerapi.repositories.WeightEntryRepository;
import com.shultzlab.weighttrackerapi.services.StatsService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @GetMapping("/bmr/{username}")
    public Map<String, Double> getBmrByUsername(@PathVariable(value = "username") String username) throws ResourceNotFoundException {
        // TODO verify user matches token
        User user = this.userRepository.findDistinctTopByUsername(username);
        if(user == null){
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        WeightEntry entry = this.weightEntryRepository.findDistinctFirstByUserOrderByEntryDateDesc(user);

        Double bmr = StatsService.calculateBMR(user, entry.getWeight());

        Map<String, Double> response = new HashMap<>();
        response.put("BMR", bmr);

        return response;
    }

    @GetMapping("/tdee/{username}")
    public Map<String, Double> getTdeeByUsername(@PathVariable(value = "username") String username) throws ResourceNotFoundException {
        // TODO verify user matches token
        User user = this.userRepository.findDistinctTopByUsername(username);
        if(user == null){
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        WeightEntry entry = this.weightEntryRepository.findDistinctFirstByUserOrderByEntryDateDesc(user);

        Double tdee = StatsService.calculateTDEE(user, entry.getWeight());
        Map<String, Double> response = new HashMap<>();
        response.put("TDEE", tdee);

        return response;
    }

    @GetMapping("/bmi/{username}")
    public Map<String, Double> getBMIByUserId(@PathVariable(value = "username") String username) throws ResourceNotFoundException {
        // TODO verify user matches token
        User user = this.userRepository.findDistinctTopByUsername(username);
        if(user == null){
            throw new ResourceNotFoundException("User not found with username: " + username);
        }

        WeightEntry entry = this.weightEntryRepository.findDistinctFirstByUserOrderByEntryDateDesc(user);

        Double bmi = StatsService.calculateBMI(user, entry.getWeight());
        Map<String, Double> response = new HashMap<>();
        response.put("BMI", bmi);

        return response;
    }

    @GetMapping("/all/{username}")
    public Map<String, Double> getAllStatsByUserId(@PathVariable(value = "username") String username) throws ResourceNotFoundException {
        // TODO verify user matches token
        User user = this.userRepository.findDistinctTopByUsername(username);
        if(user == null){
            throw new ResourceNotFoundException("User not found with username: " + username);
        }

        WeightEntry entry = this.weightEntryRepository.findDistinctFirstByUserOrderByEntryDateDesc(user);

        Double bmr = StatsService.calculateBMR(user, entry.getWeight());
        Double tdee = StatsService.calculateTDEE(user, entry.getWeight());
        Double bmi = StatsService.calculateBMI(user, entry.getWeight());

        Map<String, Double> response = new HashMap<>();
        response.put("BMR", bmr);
        response.put("TDEE", tdee);
        response.put("BMI", bmi);

        return response;
    }

}
