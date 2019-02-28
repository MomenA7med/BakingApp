package com.example.momen.baking_app.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.momen.baking_app.R;
import com.example.momen.baking_app.model.Recipe;

import java.util.List;

/**
 * Created by Momen on 2/6/2019.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeVH> {

    private List<Recipe> recipes;
    private Context context;
    private final RecipeClickListener recipeClickListener;

    public interface RecipeClickListener {
        void onListItemClick(int position);
    }

    public RecipeAdapter (Context context,List<Recipe> recipes,RecipeClickListener recipeClickListener){
        this.context = context;
        this.recipes = recipes;
        this.recipeClickListener = recipeClickListener;

    }

    @NonNull
    @Override
    public RecipeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item,parent,false);
        return new RecipeVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeVH holder, int position) {
        holder.dishname.setText(recipes.get(position).getName());
        String serving = "Serving : "+String.valueOf(recipes.get(position).getServings());
        holder.dishServing.setText(serving);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class RecipeVH extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView dishname,dishServing;
        public RecipeVH(View itemView) {
            super(itemView);
            dishname = itemView.findViewById(R.id.dish_name);
            dishServing = itemView.findViewById(R.id.dish_serving);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            recipeClickListener.onListItemClick(getAdapterPosition());
        }
    }
}
