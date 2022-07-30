package com.shultzlab.weighttrackerapi.services;

import com.shultzlab.weighttrackerapi.models.User;
import com.shultzlab.weighttrackerapi.models.enums.ActivityLevel;
import com.shultzlab.weighttrackerapi.models.enums.Gender;

public class StatsService {
    public static Double calculateBMR(User user, Double weight){
        if(user.getGender() == Gender.male){
            return 66 + (13.7 * weight) + (5 * user.getHeight()) - (6.8 * user.getAge());
        } else if(user.getGender() == Gender.female) {
            return 655 + (9.6 * weight) + (1.8 * user.getHeight()) - (4.7 * user.getAge());
        }

        return -1.0;
    }

    public static Double calculateTDEE(User user, Double weight){
        Double bmr = calculateBMR(user, weight);

        if(user.getActivityLevel() == ActivityLevel.sedentary){
            return 1.2 * bmr;
        } else if (user.getActivityLevel() == ActivityLevel.lightlyActive) {
            return 1.375 * bmr;
        } else if (user.getActivityLevel() == ActivityLevel.moderatelyActive) {
            return 1.55 * bmr;
        } else if (user.getActivityLevel() == ActivityLevel.veryActive) {
            return 1.725 * bmr;
        } else if (user.getActivityLevel() == ActivityLevel.extraActive) {
            return 1.9 * bmr;
        }

        return -1.0;
    }

    public static Double calculateBMI(User user, Double weight){
        // BMI = kg/m2

        Double meters = user.getHeight() * 0.01;
        Double metersSquared = Math.pow(meters, 2);

        Double bmi = weight / metersSquared;

        return bmi;
    }
}
