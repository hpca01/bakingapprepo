package com.example.hiren_pc_hp.bakingapp.view;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.hiren_pc_hp.bakingapp.MainActivity;
import com.example.hiren_pc_hp.bakingapp.R;
import com.example.hiren_pc_hp.bakingapp.data.RecipeViewModel;
import com.example.hiren_pc_hp.bakingapp.network.Ingredient;
import com.example.hiren_pc_hp.bakingapp.ui.IngredientAdapter;
import com.example.hiren_pc_hp.bakingapp.ui.RecipeAdapter;
import com.example.hiren_pc_hp.bakingapp.ui.UtilsForUi;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsFragment extends Fragment {
    //vars
    private UtilsForUi utilsForUi;
    private static final String TAG = "Ingredients";

    //recyclerview
    public RecyclerView mRecyclerView;
    public RecyclerView.LayoutManager mLayoutManager;
    public IngredientAdapter mAdapter;

    public IngredientsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_ingredients, container, false);

        mRecyclerView = (RecyclerView)rootview.findViewById(R.id.ingredients_recyclerview);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new IngredientAdapter();

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        String toolBarName = getArguments().getString(getString(R.string.recipename));
        utilsForUi.setToolBarTitle(toolBarName);

        getIngredientsList();
        return rootview;
    }

    private void getIngredientsList() {
        ViewModelProviders.of(getActivity()).get(RecipeViewModel.class).getIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                mAdapter.update(ingredients);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        utilsForUi = (MainActivity)getActivity();
    }
}
