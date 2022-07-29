package com.shultzlab.weighttrackerapi.models.requests;

import javax.persistence.Temporal;
import java.time.LocalDate;

// Used for request where we can send id of user
public class WeightEntryRequest {
    private Long userId;

    private Double weight;

    private LocalDate entryDate;

    public WeightEntryRequest(Long userId, Double weight, LocalDate entryDate) {
        this.userId = userId;
        this.weight = weight;
        this.entryDate = entryDate;
    }

    public WeightEntryRequest() {}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
