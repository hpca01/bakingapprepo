package com.example.hiren_pc_hp.bakingapp.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.hiren_pc_hp.bakingapp.network.Ingredient;
import com.example.hiren_pc_hp.bakingapp.network.Recipe;
import com.example.hiren_pc_hp.bakingapp.network.Step;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {
    Application application;
    private final MutableLiveData<List<Recipe>> recipes;
    private final MutableLiveData<List<Ingredient>> ingredients = new MutableLiveData<>();
    private final MutableLiveData<List<Step>> steps = new MutableLiveData<>();
    private static RecipeRepo repo;


    public RecipeViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        repo = RecipeRepo.getInstance(application);
        recipes = repo.getAllRecipes();
    }

    public LiveData<List<Recipe>> getResultsOb(){
        return recipes;
    }

    public void setIngredients(List<Ingredient> ing){
        ingredients.setValue(ing);
    }

    public void setSteps(List<Step> step){
        steps.setValue(step);
    }

    public LiveData<List<Ingredient>> getIngredients() {
        return ingredients;
    }

    public LiveData<List<Step>> getSteps() {
        return steps;
    }
}
