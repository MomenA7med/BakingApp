package com.example.momen.baking_app.fragments;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.momen.baking_app.BakingAppWidget;
import com.example.momen.baking_app.DetailActivity;
import com.example.momen.baking_app.R;
import com.example.momen.baking_app.VideoActivity;
import com.example.momen.baking_app.adapters.IngredientAdapter;
import com.example.momen.baking_app.adapters.VideoAdapter;
import com.example.momen.baking_app.model.Ingredients;
import com.example.momen.baking_app.model.Recipe;
import com.example.momen.baking_app.model.Steps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Momen on 2/8/2019.
 */

public class DetailFragmet extends Fragment implements VideoAdapter.VideoClickListener{



    public interface VideoClick {
         void onClicked(int position);
    }
    List<Ingredients> ingredientList;
    List<Steps> stepsList;
    RecyclerView ingreRV,videoRV;
    Gson gson;
    String sl,il;
    VideoClick videoClick;

    FloatingActionButton fab;
    String recipeName;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        videoClick = (VideoClick) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detail_fragment,container,false);

        ingreRV = view.findViewById(R.id.ingredient);
        videoRV = view.findViewById(R.id.steps);

        fab = view.findViewById(R.id.fab_widget);
        gson = new Gson();

        if (savedInstanceState != null){
            sl = savedInstanceState.getString("SL");
            il = savedInstanceState.getString("IL");
        }
        if (savedInstanceState == null){
            ingreRV.setLayoutManager(new LinearLayoutManager(getContext()));
            ingreRV.setHasFixedSize(true);
            videoRV.setLayoutManager(new LinearLayoutManager(getContext()));
            videoRV.setHasFixedSize(true);
        }

        ingredientList = gson.fromJson(il,
                new TypeToken<List<Ingredients>>(){}.getType());

        stepsList = gson.fromJson(sl,
                new TypeToken<List<Steps>>(){}.getType());

        IngredientAdapter ingredientAdapter = new IngredientAdapter(getContext(),ingredientList);
        VideoAdapter videoAdapter = new VideoAdapter(getContext(),stepsList,this);

        ingreRV.setAdapter(ingredientAdapter);

        videoRV.setAdapter(videoAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("Recipe",Context.MODE_PRIVATE).edit();
                editor.putString("Name",recipeName);
                editor.putString("Ingredient",il);
                editor.apply();
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity());
                Bundle bundle = new Bundle();
                int appWidgetId = bundle.getInt(
                        AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
                BakingAppWidget.updateAppWidget(getContext(),appWidgetManager,appWidgetId,recipeName,ingredientList);
            }
        });

        return view;
    }

    public void setIngredientList(String il)
    {
        this.il = il;
    }

    public void setStepsList(String sl){
        this.sl = sl;
    }

    public void setRecipeName (String recipeName){
        this.recipeName = recipeName;
    }

    @Override
    public void onItemClick(int position) {

        videoClick.onClicked(position);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("SL",sl);
        outState.putString("IL",il);
    }
}
