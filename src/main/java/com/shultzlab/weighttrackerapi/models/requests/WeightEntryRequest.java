package com.shultzlab.weighttrackerapi.models.requests;

import java.time.LocalDate;

// Used for request where we can send id of user
public class WeightEntryRequest {
    private String username;

    private Double weight;

    private LocalDate entryDate;

    public WeightEntryRequest(String username, Double weight, LocalDate entryDate) {
        this.username = username;
        this.weight = weight;
        this.entryDate = entryDate;
    }

    public WeightEntryRequest() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }
}
