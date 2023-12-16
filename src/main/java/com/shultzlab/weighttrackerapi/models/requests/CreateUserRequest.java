package com.shultzlab.weighttrackerapi.models.requests;

import com.shultzlab.weighttrackerapi.models.enums.ActivityLevel;
import com.shultzlab.weighttrackerapi.models.enums.Gender;

import java.time.LocalDate;

public class CreateUserRequest {
    private String username;
    private String password;
    private Double height;
    private LocalDate birthday;
    private ActivityLevel activityLevel;
    private Gender gender;

    public CreateUserRequest(String username, String password, Double height, LocalDate birthday, ActivityLevel activityLevel, Gender gender) {
        this.username = username;
        this.password = password;
        this.height = height;
        this.birthday = birthday;
        this.activityLevel = activityLevel;
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public ActivityLevel getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(ActivityLevel activityLevel) {
        this.activityLevel = activityLevel;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
