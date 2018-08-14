package com.example.hiren_pc_hp.bakingapp.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hiren_pc_hp.bakingapp.R;
import com.example.hiren_pc_hp.bakingapp.network.Ingredient;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    List<Ingredient> mDetails;

    public IngredientAdapter() {
        this.mDetails = new ArrayList<Ingredient>();
    }


    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_ingredients_item, parent,false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = mDetails.get(position);
        holder.name.setText(ingredient.getIngredient());
        holder.quantity.setText(String.valueOf(ingredient.getQuantity()));
        holder.unit.setText(ingredient.getMeasure());
    }

    public void update(List<Ingredient> newIngredients){
        this.mDetails = newIngredients;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDetails.size();
    }

    public Ingredient getItem(int pos){
        return mDetails.get(pos);
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.quantity)
        TextView quantity;

        @BindView(R.id.unit)
        TextView unit;

        @BindView(R.id.name)
        TextView name;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
