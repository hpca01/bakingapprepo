package com.example.hiren_pc_hp.bakingapp.data;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;

import com.example.hiren_pc_hp.bakingapp.network.Recipe;
import com.example.hiren_pc_hp.bakingapp.network.service.DataService;
import com.example.hiren_pc_hp.bakingapp.network.service.Service;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRepo {

    private static RecipeRepo recipeRepo;
    private static MutableLiveData<List<Recipe>> dataoutput = new MutableLiveData<>();

    private RecipeRepo(Application application) {

    }

    public synchronized static RecipeRepo getInstance(Application application) {
        if ( recipeRepo== null) {
            recipeRepo = new RecipeRepo(application);
        }
        return recipeRepo;
    }

    public MutableLiveData<List<Recipe>> getAllRecipes(){
        Service recipeResponse = DataService.getService();
        recipeResponse.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(response.isSuccessful()){
                    List<Recipe> data = response.body();
                    dataoutput.setValue(data);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

            }
        });

        return dataoutput;
    }

}
