package com.shultzlab.weighttrackerapi.models.responses;

public class GoalResponse {
    double weightDiff;
    long daysUntilGoal;
    double calorieDeficitPerWeek;
    double calorieDeficitPerDay;
    double todayCalorieGoal;

    public GoalResponse() {
    }

    public GoalResponse(double weightDiff, long daysUntilGoal, double calorieDeficitPerWeek, double calorieDeficitPerDay, double todayCalorieGoal) {
        this.weightDiff = weightDiff;
        this.daysUntilGoal = daysUntilGoal;
        this.calorieDeficitPerWeek = calorieDeficitPerWeek;
        this.calorieDeficitPerDay = calorieDeficitPerDay;
        this.todayCalorieGoal = todayCalorieGoal;
    }

    public double getWeightDiff() {
        return weightDiff;
    }

    public void setWeightDiff(double weightDiff) {
        this.weightDiff = weightDiff;
    }

    public long getDaysUntilGoal() {
        return daysUntilGoal;
    }

    public void setDaysUntilGoal(long daysUntilGoal) {
        this.daysUntilGoal = daysUntilGoal;
    }

    public double getCalorieDeficitPerWeek() {
        return calorieDeficitPerWeek;
    }

    public void setCalorieDeficitPerWeek(double calorieDeficitPerWeek) {
        this.calorieDeficitPerWeek = calorieDeficitPerWeek;
    }

    public double getCalorieDeficitPerDay() {
        return calorieDeficitPerDay;
    }

    public void setCalorieDeficitPerDay(double calorieDeficitPerDay) {
        this.calorieDeficitPerDay = calorieDeficitPerDay;
    }

    public double getTodayCalorieGoal() {
        return todayCalorieGoal;
    }

    public void setTodayCalorieGoal(double todayCalorieGoal) {
        this.todayCalorieGoal = todayCalorieGoal;
    }
}
