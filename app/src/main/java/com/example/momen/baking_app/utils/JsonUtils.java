package com.example.momen.baking_app.utils;

import com.example.momen.baking_app.model.Ingredients;
import com.example.momen.baking_app.model.Recipe;
import com.example.momen.baking_app.model.Steps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Momen on 2/6/2019.
*/

public class JsonUtils {


    private static final String NAME = "name";
    private static final String INGREDIENTS = "ingredients";
    private static final String QUANTITY = "quantity";
    private static final String MEASURE = "measure";
    private static final String INGREDIENT = "ingredient";
    private static final String SHORTDESC = "shortDescription";
    private static final String DESC = "description";
    private static final String VIDEO_URL = "videoURL";
    private static final String IMAGE_URL = "thumbnailURL";
    private static final String SERVINGS = "servings";
    private static final String STEPS = "steps";

    public static List<Recipe> parseToRecipe (String json){

        List<Recipe> recipeList = new ArrayList<>();
        try {
            JSONArray recipeArray = new JSONArray(json);
            for (int i =0;i<recipeArray.length();i++){
                List<Ingredients> ingredientsList = new ArrayList<>();
                List<Steps> stepsList = new ArrayList<>();
                JSONObject recipeObject  = recipeArray.optJSONObject(i);
                String name = recipeObject.optString(NAME,"no");
                int serving = recipeObject.optInt(SERVINGS,-1);
                JSONArray ing = recipeObject.optJSONArray(INGREDIENTS);
                for (int j=0;j<ing.length();j++){
                    JSONObject ingArray = ing.optJSONObject(j);
                    double quantity = ingArray.optDouble(QUANTITY,-1);
                    String measure = ingArray.optString(MEASURE,"no");
                    String ingredient = ingArray.optString(INGREDIENT,"no");
                    ingredientsList.add(new Ingredients(quantity,measure,ingredient));
                }
                JSONArray stepArray = recipeObject.optJSONArray(STEPS);
                for (int j=0;j<stepArray.length();j++){
                    JSONObject stepObject = stepArray.optJSONObject(j);
                    String shortDisc = stepObject.optString(SHORTDESC,"no");
                    String Disc = stepObject.optString(DESC,"no");
                    String video = stepObject.optString(VIDEO_URL,"no");
                    String Image = stepObject.optString(IMAGE_URL,"no");
                    stepsList.add(new Steps(shortDisc,Disc,video,Image));
                }
                recipeList.add(new Recipe(name,stepsList,ingredientsList,serving));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipeList;
    }
}
