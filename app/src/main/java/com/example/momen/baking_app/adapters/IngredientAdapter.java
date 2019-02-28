package com.example.momen.baking_app.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.momen.baking_app.R;
import com.example.momen.baking_app.model.Ingredients;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Momen on 2/8/2019.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientVH> {

    List<Ingredients> ingredients;
    Context context;

    public IngredientAdapter(Context context,List<Ingredients> ingredients){
        this.ingredients = ingredients;
        this.context = context;
    }

    @NonNull
    @Override
    public IngredientVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item,parent,false);
        return new IngredientVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientVH holder, int position) {

        String text = String.valueOf(ingredients.get(position).getQuantity())+" "+
                ingredients.get(position).getIngredient()+" "+
                ingredients.get(position).getMeasure();

        holder.ingre.setText(text);

    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class IngredientVH extends RecyclerView.ViewHolder {
        TextView ingre;
        public IngredientVH(View itemView) {
            super(itemView);
            ingre = itemView.findViewById(R.id.ingredientItem);
        }
    }
}
