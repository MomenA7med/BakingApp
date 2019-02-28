package com.example.momen.baking_app.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.momen.baking_app.DetailActivity;
import com.example.momen.baking_app.MainActivity;
import com.example.momen.baking_app.R;
import com.example.momen.baking_app.adapters.RecipeAdapter;
import com.example.momen.baking_app.model.Ingredients;
import com.example.momen.baking_app.model.Recipe;
import com.example.momen.baking_app.model.Steps;
import com.example.momen.baking_app.utils.JsonUtils;
import com.example.momen.baking_app.utils.NetworkUtils;
import com.example.momen.baking_app.utils.SimpleIdlingResource;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Momen on 2/6/2019.
 */

public class MainFragment extends Fragment implements RecipeAdapter.RecipeClickListener,
        LoaderManager.LoaderCallbacks<String>{

    private static final String URL_BUNDLE = "urlBundle";

    private static final int LOADER_ID = 1;

    SimpleIdlingResource simpleIdlingResource;

    List<Recipe> recipes;

    RecyclerView recyclerView;

    Gson gson = new Gson();

   // private ClickLisnerFragment clickLisnerFragment;
//
   // public interface ClickLisnerFragment{
   //      void onClickFragment(int position);
   // }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    //    clickLisnerFragment = (ClickLisnerFragment) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recipe_list,container,false);

        simpleIdlingResource = (SimpleIdlingResource) ((MainActivity) (getActivity())).getIdlingResource();

        if (simpleIdlingResource != null)
            simpleIdlingResource.setIdleState(false);

        recyclerView = view.findViewById(R.id.recipeRV);

        recipes = new ArrayList<>();

        LoaderManager manager = getLoaderManager();

        manager.initLoader(LOADER_ID,null,this);

        URL url = NetworkUtils.buildUrl();

        Bundle bundle = new Bundle();

        bundle.putString(URL_BUNDLE,url.toString());

        Loader<String> recipeLoader = manager.getLoader(LOADER_ID);
        if (recipeLoader == null)
            manager.initLoader(LOADER_ID,bundle,this);
        else
            manager.restartLoader(LOADER_ID,bundle,this);

        recyclerView.setHasFixedSize(true);


        return view ;
    }

    @Override
    public void onListItemClick(int position) {


        Intent intent = new Intent(getContext(),DetailActivity.class);

        List<Ingredients> ingres = recipes.get(position).getIngredients();

        List<Steps> steps = recipes.get(position).getSteps();

        String stepsIntent = gson.toJson(steps);

        String ingresIntent = gson.toJson(ingres);

        intent.putExtra("STEPS",stepsIntent);
        intent.putExtra("INGRES",ingresIntent);
        intent.putExtra("NAME",recipes.get(position).getName());

        startActivity(intent);

    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<String>(getContext()) {

            String result;
            @Override
            protected void onStartLoading() {
                if (args == null)
                    return;
                if (result != null)
                    deliverResult(result);
                else
                    forceLoad();
            }

            @Nullable
            @Override
            public String loadInBackground() {
                String urlString = args.getString(URL_BUNDLE);
                if (urlString == null || TextUtils.isEmpty(urlString))
                    return null;
                try {
                    URL url = new URL(urlString);
                    String result = NetworkUtils.getResponseFromHttpUrl(url);
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(@Nullable String data) {
                result = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

        recipes = JsonUtils.parseToRecipe(data);

        if(MainActivity.getNoPane())
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        else
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RecipeAdapter adapter = new RecipeAdapter(getContext(),recipes,this);

        recyclerView.setAdapter(adapter);

        simpleIdlingResource.setIdleState(true);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
