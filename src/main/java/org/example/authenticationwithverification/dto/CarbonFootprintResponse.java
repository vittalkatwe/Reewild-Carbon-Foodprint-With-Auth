package org.example.authenticationwithverification.dto;

import java.util.List;

public class CarbonFootprintResponse {

    private String dish;
    private double estimated_carbon_kg;
    private List<Ingredient> ingredients;
    private long waterPrint;

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public double getEstimated_carbon_kg() {
        return estimated_carbon_kg;
    }

    public void setEstimated_carbon_kg(double estimated_carbon_kg) {
        this.estimated_carbon_kg = estimated_carbon_kg;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public long getWaterPrint() {
        return waterPrint;
    }

    public void setWaterPrint(long waterPrint) {
        this.waterPrint = waterPrint;
    }
}

