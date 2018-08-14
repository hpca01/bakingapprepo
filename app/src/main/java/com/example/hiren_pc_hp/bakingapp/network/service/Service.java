package com.example.hiren_pc_hp.bakingapp.network.service;

import android.arch.lifecycle.LiveData;

import com.example.hiren_pc_hp.bakingapp.network.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {

    public String recipes = "2017/May/59121517_baking/baking.json";
    @GET(recipes)
    Call<List<Recipe>> getRecipes();
}
