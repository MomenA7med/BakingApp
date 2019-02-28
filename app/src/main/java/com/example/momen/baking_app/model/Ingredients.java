package com.example.momen.baking_app.model;

/**
 * Created by Momen on 2/6/2019.
 */

public class Ingredients {

    private double quantity;
    private String measure;
    private String ingredient;

    public Ingredients(){}

    public Ingredients(double quantity,String measure,String ingredient){
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getIngredient() {
        return ingredient;
    }

    public String getMeasure() {
        return measure;
    }
}
