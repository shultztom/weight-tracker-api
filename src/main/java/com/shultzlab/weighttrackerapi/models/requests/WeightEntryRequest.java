package com.shultzlab.weighttrackerapi.models.requests;

// Used for request where we can send id of user
public class WeightEntryRequest {
    private Long userId;

    private Double weight;

    public WeightEntryRequest(Long userId, Double weight) {
        this.userId = userId;
        this.weight = weight;
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
}
