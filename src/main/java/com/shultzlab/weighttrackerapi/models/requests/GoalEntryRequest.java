package com.shultzlab.weighttrackerapi.models.requests;

import java.time.LocalDate;

public class GoalEntryRequest {
    private Double weight;

    private LocalDate goalDate;

    public GoalEntryRequest(String username, Double weight, LocalDate goalDate) {
        this.weight = weight;
        this.goalDate = goalDate;
    }

    public GoalEntryRequest() {
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public LocalDate getGoalDate() {
        return goalDate;
    }

    public void setGoalDate(LocalDate goalDate) {
        this.goalDate = goalDate;
    }
}
