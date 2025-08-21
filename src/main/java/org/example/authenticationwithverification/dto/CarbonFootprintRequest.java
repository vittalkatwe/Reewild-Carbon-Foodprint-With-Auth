package org.example.authenticationwithverification.dto;

public class CarbonFootprintRequest {

    private String dish;
    private long servings=1;

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public long getServings() {
        return servings;
    }

    public void setServings(long servings) {
        this.servings = servings;
    }
}