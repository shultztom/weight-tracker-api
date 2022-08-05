package com.shultzlab.weighttrackerapi.controllers;

import com.shultzlab.weighttrackerapi.exceptions.ResourceNotFoundException;
import com.shultzlab.weighttrackerapi.exceptions.TokenForbiddenException;
import com.shultzlab.weighttrackerapi.models.User;
import com.shultzlab.weighttrackerapi.models.WeightEntry;
import com.shultzlab.weighttrackerapi.models.requests.WeightEntryRequest;
import com.shultzlab.weighttrackerapi.repositories.UserRepository;
import com.shultzlab.weighttrackerapi.repositories.WeightEntryRepository;
import com.shultzlab.weighttrackerapi.services.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/entry")
public class WeightEntryController {
    final WeightEntryRepository weightEntryRepository;
    final UserRepository userRepository;

    public WeightEntryController(WeightEntryRepository weightEntryRepository, UserRepository userRepository) {
        this.weightEntryRepository = weightEntryRepository;
        this.userRepository = userRepository;
    }

    // Commented out as not used by client
//    @GetMapping()
//    public List<WeightEntry> getAllWeightEntries(){
//        return this.weightEntryRepository.findAll();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<WeightEntry> getWeightEntryById(@PathVariable(value = "id") Long entryId) throws ResourceNotFoundException {
//        WeightEntry entry = this.weightEntryRepository.findById(entryId).orElseThrow(() -> new ResourceNotFoundException("Weight Entry not found for this id: " + entryId));
//        // TODO verify record belongs to user before returning
//        return ResponseEntity.ok().body(entry);
//    }
//
//    @GetMapping("/user/{id}")
//    public List<WeightEntry> getAllWeightEntriesForUser(@PathVariable(value = "id") Long userId) {
//        // TODO verify userId matches token
//        return this.weightEntryRepository.findAllByUserId(userId);
//    }

    @GetMapping("/username/{username}")
    public List<WeightEntry> getAllWeightEntriesForUserScopedByDays(@PathVariable(value = "username") String username,
                                                                    @RequestParam Optional<String> time,
                                                                    @RequestHeader("x-auth-token") String token) throws TokenForbiddenException {

        String tokenUser = TokenService.getUsernameFromToken(token);
        if (!username.equals(tokenUser)) {
            throw new TokenForbiddenException();
        }

        String daysStr = "-1";
        if (time.isPresent()) {
            daysStr = time.get();
        }

        // Account for empty string
        if (daysStr.equals("")) {
            daysStr = "-1";
        }

        int days = Integer.parseInt(daysStr);

        LocalDate date = LocalDate.now().minusDays(days);

        // Handle all
        if (days == -1) {
            date = LocalDate.EPOCH;
        }

        // TODO verify username matches token
        return this.weightEntryRepository.findAllByUsernameByDays(username, date);
    }

    @GetMapping("/username/{username}/last")
    public WeightEntry getLastWeightEntryByUsername(@PathVariable(value = "username") String username, @RequestHeader("x-auth-token") String token) throws ResourceNotFoundException, TokenForbiddenException {
        User user = this.userRepository.findDistinctTopByUsername(username);

        String tokenUser = TokenService.getUsernameFromToken(token);
        if (!user.getUsername().equals(tokenUser)) {
            throw new TokenForbiddenException();
        }

        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        return this.weightEntryRepository.findDistinctFirstByUserOrderByEntryDateDesc(user);
    }

    @PostMapping()
    public WeightEntry createWeightEntry(@RequestBody WeightEntryRequest entry, @RequestHeader("x-auth-token") String token) throws ResourceNotFoundException, TokenForbiddenException {
        String username = entry.getUsername();

        String tokenUser = TokenService.getUsernameFromToken(token);
        if (!username.equals(tokenUser)) {
            throw new TokenForbiddenException();
        }

        User user = this.userRepository.findDistinctTopByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        WeightEntry newWeightEntry = new WeightEntry(user, entry.getWeight(), entry.getEntryDate());
        return this.weightEntryRepository.save(newWeightEntry);
    }

    @PutMapping("/{id}")
    public WeightEntry updateWeightEntry(@PathVariable(value = "id") Long entryId, @RequestBody WeightEntryRequest entryRequest, @RequestHeader("x-auth-token") String token) throws ResourceNotFoundException, TokenForbiddenException {
        String username = entryRequest.getUsername();

        String tokenUser = TokenService.getUsernameFromToken(token);
        if (!username.equals(tokenUser)) {
            throw new TokenForbiddenException();
        }

        User user = this.userRepository.findDistinctTopByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        WeightEntry entry = this.weightEntryRepository.findById(entryId).orElseThrow(() -> new ResourceNotFoundException("Entry not found with id: " + entryId));
        entry.setUser(user);
        entry.setWeight(entryRequest.getWeight());
        entry.setEntryDate(entryRequest.getEntryDate());
        return this.weightEntryRepository.save(entry);
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteWeightEntry(@PathVariable(value = "id") Long entryId, @RequestHeader("x-auth-token") String token) throws ResourceNotFoundException, TokenForbiddenException {
        WeightEntry entry = this.weightEntryRepository.findById(entryId).orElseThrow(() -> new ResourceNotFoundException("Entry not found with id: " + entryId));

        String tokenUser = TokenService.getUsernameFromToken(token);
        if (!entry.getUser().getUsername().equals(tokenUser)) {
            throw new TokenForbiddenException();
        }


        this.weightEntryRepository.delete(entry);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;
    }

}

