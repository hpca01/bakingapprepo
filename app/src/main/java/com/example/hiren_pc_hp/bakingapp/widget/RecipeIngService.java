package com.example.hiren_pc_hp.bakingapp.widget;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.hiren_pc_hp.bakingapp.R;
import com.example.hiren_pc_hp.bakingapp.network.Ingredient;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class RecipeIngService extends IntentService {

    public static String WIDGET_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";
    public static String R_STRING_INGREDIENTS = "Ingredients";
    public static String R_STRING_RECIPENAME = "recipe_name";

    private static List<Ingredient> mIngredients;
    static String recipename;

    public RecipeIngService() {
        super("RecipeIngService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent!=null){
            mIngredients =  intent.getParcelableArrayListExtra(getString(R.string.ingredients));
            recipename = intent.getStringExtra(getString(R.string.recipename));
            handelActionUpdate(mIngredients, recipename);
        }
    }

    public void handelActionUpdate(List<Ingredient> list, String recipename){
        if(list!=null){
            Intent intent = new Intent(WIDGET_UPDATE);
            intent.setAction(WIDGET_UPDATE);
            intent.putParcelableArrayListExtra(getString(R.string.ingredients), (ArrayList<Ingredient>) list);
            intent.putExtra(getString(R.string.recipename),recipename);
            sendBroadcast(intent);
        }
    }
}
