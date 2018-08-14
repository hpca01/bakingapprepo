package com.example.hiren_pc_hp.bakingapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hiren_pc_hp.bakingapp.R;
import com.example.hiren_pc_hp.bakingapp.network.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    public interface OnItemClicked{
        void onItemClick(Recipe recipe);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView recipeName;
        public TextView recipeServings;
        OnItemClicked onItemClicked;

        public ViewHolder(View itemView, OnItemClicked itemclicklistener) {
            super(itemView);
            recipeName = (TextView)itemView.findViewById(R.id.recipe_name);
            recipeServings = (TextView)itemView.findViewById(R.id.recipe_servings);
            this.onItemClicked = itemclicklistener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Recipe item = getItem(getAdapterPosition());
            this.onItemClicked.onItemClick(item);
        }
    }

    private List<Recipe> mRecipe;
    private Context mContext;
    private OnItemClicked onclick;

    public RecipeAdapter(List<Recipe> mRecipe, Context mContext, OnItemClicked onclick) {
        this.mRecipe = mRecipe;
        this.mContext = mContext;
        this.onclick = onclick;
    }

    public void update(List<Recipe> recipes){
        this.mRecipe = recipes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int recycler_view_layout = R.layout.main_recipe_card;

        View view = inflater.inflate(recycler_view_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, onclick);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe item = mRecipe.get(position);
        String name = item.getName();
        String servings = "Servings: "+String.valueOf(item.getServings());
        holder.recipeName.setText(name);
        holder.recipeServings.setText(servings);
    }

    @Override
    public int getItemCount() {
        return mRecipe.size();
    }

    public Recipe getItem(int pos){
        return mRecipe.get(pos);
    }


}
