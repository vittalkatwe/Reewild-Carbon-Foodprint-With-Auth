package org.example.authenticationwithverification.dto;

public class Ingredient {

    private String name;
    private double carbon_kg;

    public Ingredient() {
    }

    public Ingredient(String name, double carbon_kg) {
        this.name = name;
        this.carbon_kg = carbon_kg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCarbon_kg() {
        return carbon_kg;
    }

    public void setCarbon_kg(double carbon_kg) {
        this.carbon_kg = carbon_kg;
    }
}

