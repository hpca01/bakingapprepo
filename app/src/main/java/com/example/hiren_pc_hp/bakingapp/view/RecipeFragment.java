package com.example.hiren_pc_hp.bakingapp.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hiren_pc_hp.bakingapp.MainActivity;
import com.example.hiren_pc_hp.bakingapp.R;
import com.example.hiren_pc_hp.bakingapp.network.Recipe;
import com.example.hiren_pc_hp.bakingapp.ui.RecipeAdapter;
import com.example.hiren_pc_hp.bakingapp.ui.UtilsForUi;
import com.example.hiren_pc_hp.bakingapp.data.RecipeViewModel;

import java.util.ArrayList;
import java.util.List;


public class RecipeFragment extends Fragment {
    //vars

    //data
    private RecipeViewModel recipeViewModel;
    //recyclerviews
    public RecyclerView mRecyclerView;
    public RecyclerView.LayoutManager mLayoutManager;
    public RecipeAdapter mAdapter;
    //interface objects
    private recipeInteractionListener mListener;
    private UtilsForUi utilsForUi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootview = inflater.inflate(R.layout.fragment_recipe, container, false);
        utilsForUi.setToolBarTitle(getString(R.string.main_activity));

        mRecyclerView = (RecyclerView)rootview.findViewById(R.id.main_recipe_view);

        mLayoutManager = new LinearLayoutManager(getContext());

        mAdapter = new RecipeAdapter(new ArrayList<Recipe>(), getContext(), new RecipeAdapter.OnItemClicked() {
            @Override
            public void onItemClick(Recipe recipe) {
                mListener.onRecipeInteractionListener(recipe);
            }
        });
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        getresponse();

        return rootview;
    }



    private void getresponse() {
        recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        recipeViewModel.getResultsOb().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                mAdapter.update(recipes);
            }
        });
    }


    public interface recipeInteractionListener{
        void onRecipeInteractionListener(Recipe recipe);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof recipeInteractionListener) {
            mListener = (recipeInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
        utilsForUi = (MainActivity)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
