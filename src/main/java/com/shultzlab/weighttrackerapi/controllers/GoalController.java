package com.shultzlab.weighttrackerapi.controllers;

import com.shultzlab.weighttrackerapi.exceptions.ResourceNotFoundException;
import com.shultzlab.weighttrackerapi.exceptions.TokenForbiddenException;
import com.shultzlab.weighttrackerapi.models.Goal;
import com.shultzlab.weighttrackerapi.models.User;
import com.shultzlab.weighttrackerapi.models.WeightEntry;
import com.shultzlab.weighttrackerapi.models.requests.GoalEntryRequest;
import com.shultzlab.weighttrackerapi.models.responses.GoalResponse;
import com.shultzlab.weighttrackerapi.repositories.GoalRepository;
import com.shultzlab.weighttrackerapi.repositories.UserRepository;
import com.shultzlab.weighttrackerapi.repositories.WeightEntryRepository;
import com.shultzlab.weighttrackerapi.services.StatsService;
import com.shultzlab.weighttrackerapi.services.TokenService;
import com.shultzlab.weighttrackerapi.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/goals")
public class GoalController {
    final GoalRepository goalRepository;
    final UserRepository userRepository;
    final WeightEntryRepository weightEntryRepository;
    final StatsService statsService;
    final TokenService tokenService;
    private final UserService userService;

    public GoalController(GoalRepository goalRepository, UserRepository userRepository, WeightEntryRepository weightEntryRepository, StatsService statsService, TokenService tokenService, UserService userService) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
        this.weightEntryRepository = weightEntryRepository;
        this.statsService = statsService;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public List<Goal> getAllGoalsByUsername(@PathVariable(value = "username") String username, @RequestHeader("x-auth-token") String token) throws ResourceNotFoundException, TokenForbiddenException {
        User user = tokenService.getAndValidateUsernameFromToken(token, username);
        return goalRepository.findByUser(user);
    }

    @GetMapping("/{username}/{id}")
    public Goal getGoalById(@PathVariable(value = "username") String username, @PathVariable(value = "id") Long goalId, @RequestHeader("x-auth-token") String token) throws ResourceNotFoundException, TokenForbiddenException {
        User user = tokenService.getAndValidateUsernameFromToken(token, username);
        Goal goal = goalRepository.findById(goalId).orElseThrow(() -> new ResourceNotFoundException("Goal not found for this id :: " + goalId));

        if (!goal.getUser().equals(user)) {
            throw new TokenForbiddenException("Not authorized to access this goal");
        }

        return goal;
    }

    @GetMapping("/{username}/goal/calorieBreakdown")
    public GoalResponse getCalorieBreakdown(@PathVariable(value = "username") String username, @RequestHeader("x-auth-token") String token) throws ResourceNotFoundException, TokenForbiddenException {

        LocalDate TODAY = LocalDate.now();
        int CALORIES_IN_KG = 7700;

        User user = tokenService.getAndValidateUsernameFromToken(token, username);

        // Get current weight for user
        WeightEntry currentWeight = weightEntryRepository.findDistinctFirstByUserOrderByEntryDateDesc(user);
        if(currentWeight == null) {
            return new GoalResponse();
        }

        // Get goal weight for user
        Goal goalWeight = goalRepository.findDistinctFirstByUserOrderByCreatedAtDesc(user);
        if(goalWeight == null){
            return new GoalResponse();
        }

        // Get diff
        double weightDiff = currentWeight.getWeight() - goalWeight.getWeight();
        if (weightDiff <= 0) {
            return new GoalResponse();
        }

        long daysUntilGoal = ChronoUnit.DAYS.between(TODAY, goalWeight.getGoalDate());
        if (daysUntilGoal <= 0) {
            return new GoalResponse();
        }

        double poundsInCalories = weightDiff * CALORIES_IN_KG;
        double calorieDeficitPerDay = poundsInCalories / daysUntilGoal;
        double calorieDeficitPerWeek = calorieDeficitPerDay * 7;

        // Get TDEE
        Double tdee = StatsService.calculateTDEE(user, currentWeight.getWeight());
        double todayCalorieGoal = tdee - calorieDeficitPerDay;

        return new GoalResponse(weightDiff, daysUntilGoal, calorieDeficitPerWeek, calorieDeficitPerDay, todayCalorieGoal);
    }

    @PostMapping("/{username}")
    public Goal createGoal(@PathVariable(value = "username") String username, @RequestBody GoalEntryRequest goal, @RequestHeader("x-auth-token") String token) throws ResourceNotFoundException, TokenForbiddenException {
        User user = tokenService.getAndValidateUsernameFromToken(token, username);
        Goal newGoal = new Goal(user, goal.getWeight(), goal.getGoalDate());
        return goalRepository.save(newGoal);
    }

    @PutMapping("/{username}/{id}")
    public ResponseEntity<Goal> updateGoal(@PathVariable(value = "username") String username, @PathVariable(value = "id") Long goalId, @RequestBody GoalEntryRequest goalDetails, @RequestHeader("x-auth-token") String token) throws ResourceNotFoundException, TokenForbiddenException {
        User user = tokenService.getAndValidateUsernameFromToken(token, username);
        Goal goal = goalRepository.findById(goalId).orElseThrow(() -> new ResourceNotFoundException("Goal not found for this id :: " + goalId));

        if (!goal.getUser().equals(user)) {
            throw new TokenForbiddenException("Not authorized to modify this goal");
        }

        goal.setWeight(goalDetails.getWeight());
        goal.setGoalDate(goalDetails.getGoalDate());
        final Goal updatedGoal = goalRepository.save(goal);
        return ResponseEntity.ok(updatedGoal);
    }

    @DeleteMapping("/{username}/{id}")
    public ResponseEntity<?> deleteGoal(@PathVariable(value = "username") String username, @PathVariable(value = "id") Long goalId, @RequestHeader("x-auth-token") String token) throws ResourceNotFoundException, TokenForbiddenException {
        User user = tokenService.getAndValidateUsernameFromToken(token, username);
        Goal goal = goalRepository.findById(goalId).orElseThrow(() -> new ResourceNotFoundException("Goal not found for this id :: " + goalId));

        if (!goal.getUser().equals(user)) {
            throw new TokenForbiddenException("Not authorized to delete this goal");
        }

        goalRepository.delete(goal);
        return ResponseEntity.ok().build();
    }
}
