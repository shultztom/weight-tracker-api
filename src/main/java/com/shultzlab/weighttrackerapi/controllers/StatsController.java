package com.shultzlab.weighttrackerapi.controllers;

import com.shultzlab.weighttrackerapi.exceptions.ResourceNotFoundException;
import com.shultzlab.weighttrackerapi.exceptions.TokenForbiddenException;
import com.shultzlab.weighttrackerapi.models.User;
import com.shultzlab.weighttrackerapi.models.WeightEntry;
import com.shultzlab.weighttrackerapi.repositories.UserRepository;
import com.shultzlab.weighttrackerapi.repositories.WeightEntryRepository;
import com.shultzlab.weighttrackerapi.services.StatsService;
import com.shultzlab.weighttrackerapi.services.TokenService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/stats")
public class StatsController {
    final WeightEntryRepository weightEntryRepository;
    final UserRepository userRepository;
    final TokenService tokenService;

    public StatsController(WeightEntryRepository weightEntryRepository, UserRepository userRepository, TokenService tokenService) {
        this.weightEntryRepository = weightEntryRepository;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    // https://www.k-state.edu/paccats/Contents/PA/PDF/Physical%20Activity%20and%20Controlling%20Weight.pdf

    @GetMapping("/bmr/{username}")
    public Map<String, Double> getBmrByUsername(@PathVariable(value = "username") String username,
                                                @RequestHeader("x-auth-token") String token) throws ResourceNotFoundException, TokenForbiddenException {
        User user = tokenService.getAndValidateUsernameFromToken(token, username);
        WeightEntry entry = this.weightEntryRepository.findDistinctFirstByUserOrderByEntryDateDesc(user);

        Double bmr = StatsService.calculateBMR(user, entry.getWeight());

        Map<String, Double> response = new HashMap<>();
        response.put("BMR", bmr);

        return response;
    }

    @GetMapping("/tdee/{username}")
    public Map<String, Double> getTdeeByUsername(@PathVariable(value = "username") String username,
                                                 @RequestHeader("x-auth-token") String token) throws ResourceNotFoundException, TokenForbiddenException {
        User user = tokenService.getAndValidateUsernameFromToken(token, username);
        WeightEntry entry = this.weightEntryRepository.findDistinctFirstByUserOrderByEntryDateDesc(user);

        Double tdee = StatsService.calculateTDEE(user, entry.getWeight());
        Map<String, Double> response = new HashMap<>();
        response.put("TDEE", tdee);

        return response;
    }

    @GetMapping("/bmi/{username}")
    public Map<String, Double> getBMIByUserId(@PathVariable(value = "username") String username,
                                              @RequestHeader("x-auth-token") String token) throws ResourceNotFoundException, TokenForbiddenException {
        User user = tokenService.getAndValidateUsernameFromToken(token, username);

        WeightEntry entry = this.weightEntryRepository.findDistinctFirstByUserOrderByEntryDateDesc(user);

        Double bmi = StatsService.calculateBMI(user, entry.getWeight());
        Map<String, Double> response = new HashMap<>();
        response.put("BMI", bmi);

        return response;
    }

    @GetMapping("/all/{username}")
    public Map<String, Double> getAllStatsByUserId(@PathVariable(value = "username") String username,
                                                   @RequestHeader("x-auth-token") String token) throws ResourceNotFoundException, TokenForbiddenException {
        User user = tokenService.getAndValidateUsernameFromToken(token, username);

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

    @GetMapping("/tdeeOptions/{username}")
    public Map<String, Integer> getTdeeOptionsByUserId(@PathVariable(value = "username") String username,
                                              @RequestHeader("x-auth-token") String token) throws ResourceNotFoundException, TokenForbiddenException {

        User user = tokenService.getAndValidateUsernameFromToken(token, username);
        WeightEntry entry = this.weightEntryRepository.findDistinctFirstByUserOrderByEntryDateDesc(user);
        return StatsService.viewTDEEOptions(user, entry.getWeight());
    }

}
