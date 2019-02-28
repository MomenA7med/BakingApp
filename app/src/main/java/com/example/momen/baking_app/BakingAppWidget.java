package com.example.momen.baking_app;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.momen.baking_app.model.Ingredients;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String recipeName, List<Ingredients> ingredients) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        views.setTextViewText(R.id.appwidget_text, recipeName);

        views.removeAllViews(R.id.widget_ingredients_container);
        for (Ingredients ingredient : ingredients){
            RemoteViews ingreView = new RemoteViews(context.getPackageName(), R.layout.ingredient);
            ingreView.setTextViewText(R.id.ingredient_name_text_view,
                    ingredient.getMeasure()+" "+
                         ingredient.getIngredient());
            views.addView(R.id.widget_ingredients_container,ingreView);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        SharedPreferences preferences = context.getSharedPreferences("Recipe",Context.MODE_PRIVATE);
        String recipeName = preferences.getString("recipeName",null);
        String il = preferences.getString("Ingredient",null);
        Gson gson = new Gson();
        List<Ingredients> ingredientList = gson.fromJson(il,
                new TypeToken<List<Ingredients>>(){}.getType());
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId,recipeName,ingredientList);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

