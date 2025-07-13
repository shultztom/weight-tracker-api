package com.shultzlab.weighttrackerapi.services;

import com.shultzlab.weighttrackerapi.models.User;
import com.shultzlab.weighttrackerapi.models.enums.ActivityLevel;
import com.shultzlab.weighttrackerapi.models.enums.Gender;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class StatsService {
    /*
    Mifflin-St Jeor Equation:
    For men:
    BMR = 10W + 6.25H - 5A + 5
    For women:
    BMR = 10W + 6.25H - 5A - 161
     */
    public static Double calculateBMR(User user, Double weight){
        if(user.getGender() == Gender.male){
            return (10 * weight) + (6.25 * user.getHeight()) - (5 * user.getAge()) + 5;
        } else if(user.getGender() == Gender.female) {
            return (10 * weight) + (6.25 * user.getHeight()) - (5 * user.getAge()) - 161;
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

    public static HashMap<String, Integer> viewTDEEOptions(User user, Double weight){
        Double bmr = calculateBMR(user, weight);

        HashMap<String, Integer> tdeeOptions = new HashMap<>();
        tdeeOptions.put("sedentary", (int) Math.round(bmr * 1.2));
        tdeeOptions.put("lightlyActive", (int) Math.round(bmr * 1.375));
        tdeeOptions.put("moderatelyActive", (int) Math.round(bmr * 1.55));
        tdeeOptions.put("veryActive", (int) Math.round(bmr * 1.725));
        tdeeOptions.put("extraActive", (int) Math.round( bmr * 1.9));

        return tdeeOptions;

    }
}
