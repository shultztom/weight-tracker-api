package com.shultzlab.weighttrackerapi.controllers;

import com.shultzlab.weighttrackerapi.exceptions.ResourceNotFoundException;
import com.shultzlab.weighttrackerapi.models.User;
import com.shultzlab.weighttrackerapi.models.WeightEntry;
import com.shultzlab.weighttrackerapi.models.requests.WeightEntryRequest;
import com.shultzlab.weighttrackerapi.repositories.UserRepository;
import com.shultzlab.weighttrackerapi.repositories.WeightEntryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/entry")
public class WeightEntryController {
    final WeightEntryRepository weightEntryRepository;
    final UserRepository userRepository;

    public WeightEntryController(WeightEntryRepository weightEntryRepository, UserRepository userRepository){
        this.weightEntryRepository = weightEntryRepository;
        this.userRepository = userRepository;
    }

    // TODO Get rid of this after token?
    @GetMapping()
    public List<WeightEntry> getAllWeightEntries(){
        return this.weightEntryRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeightEntry> getWeightEntryById(@PathVariable(value = "id") Long entryId) throws ResourceNotFoundException {
        WeightEntry entry = this.weightEntryRepository.findById(entryId).orElseThrow(() -> new ResourceNotFoundException("Weight Entry not found for this id: " + entryId));
        // TODO verify record belongs to user before returning
        return ResponseEntity.ok().body(entry);
    }

    @GetMapping("/user/{id}")
    public List<WeightEntry> getAllWeightEntriesForUser(@PathVariable(value = "id") Long userId) {
        // TODO verify userId matches token
        return this.weightEntryRepository.findAllByUserId(userId);
    }

    @GetMapping("/username/{username}")
    public List<WeightEntry> getAllWeightEntriesForUser(@PathVariable(value = "username") String username) {
        // TODO verify username matches token
        return this.weightEntryRepository.findAllByUsername(username);
    }

    @PostMapping()
    public WeightEntry createWeightEntry(@RequestBody WeightEntryRequest entry) throws ResourceNotFoundException{
        // TODO verify user matches token
        Long userId = entry.getUserId();
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        WeightEntry newWeightEntry = new WeightEntry(user, entry.getWeight(), entry.getEntryDate());
        return this.weightEntryRepository.save(newWeightEntry);
    }

    @PutMapping("/{id}")
    public WeightEntry updateWeightEntry(@PathVariable(value = "id") Long entryId, @RequestBody WeightEntryRequest entryRequest) throws ResourceNotFoundException {
        // TODO verify user matches token
        Long userId = entryRequest.getUserId();
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        WeightEntry entry = this.weightEntryRepository.findById(entryId).orElseThrow(() -> new ResourceNotFoundException("Entry not found with id: " + entryId));
        entry.setUser(user);
        entry.setWeight(entryRequest.getWeight());
        entry.setEntryDate(entryRequest.getEntryDate());
        return this.weightEntryRepository.save(entry);
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteWeightEntry(@PathVariable(value = "id")Long entryId) throws ResourceNotFoundException {
        WeightEntry entry = this.weightEntryRepository.findById(entryId).orElseThrow(() -> new ResourceNotFoundException("Entry not found with id: " + entryId));
        // TODO verify record belongs to user before deleting
        this.weightEntryRepository.delete(entry);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;
    }

}

