package com.example.hiren_pc_hp.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.example.hiren_pc_hp.bakingapp.R;
import com.example.hiren_pc_hp.bakingapp.network.Ingredient;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link IngredientsWidgetConfigureActivity IngredientsWidgetConfigureActivity}
 */
public class IngredientsWidget extends AppWidgetProvider {
    static List<Ingredient> mIngredients;
    static String recipeName;


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.d(TAG, "updateAppWidget: update called on "+appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        Intent sIntent = new Intent(context, IngredientDisplayService.class);
        views.setTextViewText(R.id.recipe_name, recipeName);
        views.setRemoteAdapter(R.id.ingredients_list_view, sIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        if(mIngredients!=null){
            for (int appWidgetId : appWidgetIds) {
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.ingredients_list_view);
                updateAppWidget(context, appWidgetManager, appWidgetId);
            }
        }
        else{
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
            views.setTextViewText(R.id.recipe_name, "Recipeeee");
            for(int appWidgetId: appWidgetIds){
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }

    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, IngredientsWidget.class));
        Log.d(TAG, "onReceive: app widgetid len "+appWidgetIds.length);

        if(intent.getAction() == AppWidgetManager.ACTION_APPWIDGET_UPDATE){
            mIngredients = intent.getParcelableArrayListExtra(RecipeIngService.R_STRING_INGREDIENTS);
            recipeName = intent.getStringExtra(RecipeIngService.R_STRING_RECIPENAME);
            Log.d(TAG, "onReceive: ingredients recieved? "+(mIngredients!=null));
            if(mIngredients!=null) {
                Log.d(TAG, "onReceive: ingredients "+mIngredients.size());
                for (int appWidgetId : appWidgetIds) {
                    Log.d(TAG, "onReceive: ing is full app widgetid "+appWidgetId);
                    updateAppWidget(context, appWidgetManager, appWidgetId);
                    //appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.ingredients_list_view);
                }
            }
        }

        super.onReceive(context, intent);
    }
}

