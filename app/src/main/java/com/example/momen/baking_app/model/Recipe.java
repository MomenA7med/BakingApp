package com.example.momen.baking_app.model;

import java.util.List;

/**
 * Created by Momen on 2/6/2019.
 */

public class Recipe {
    private String name;
    private List<Steps> steps;
    private List<Ingredients> ingredients;
    private int servings ;

    public Recipe(){}

    public Recipe(String name,List<Steps> steps,List<Ingredients> ingredients,int servings){

        this.name = name;
        this.steps = steps;
        this.ingredients = ingredients;
        this.servings = servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public int getServings() {
        return servings;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }

    public String getName() {
        return name;
    }


    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public List<Steps> getSteps() {
        return steps;
    }
}
