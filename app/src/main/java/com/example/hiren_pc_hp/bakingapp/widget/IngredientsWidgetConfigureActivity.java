package com.example.hiren_pc_hp.bakingapp.widget;

import android.app.Activity;
import android.app.Fragment;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.example.hiren_pc_hp.bakingapp.R;
import com.example.hiren_pc_hp.bakingapp.data.RecipeViewModel;
import com.example.hiren_pc_hp.bakingapp.network.Recipe;
import com.example.hiren_pc_hp.bakingapp.network.service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

/**
 * The configuration screen for the {@link IngredientsWidget IngredientsWidget} AppWidget.
 */
public class IngredientsWidgetConfigureActivity extends Activity {
    private final List<Recipe>  data = new ArrayList<>();
    private final List<String> recipeNames = new ArrayList<>();
    private Recipe recipeForWidget = null;

    static ArrayAdapter<String> arrayAdapter;
    private static final String PREFS_NAME = "com.example.hiren_pc_hp.bakingapp.widget.IngredientsWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId=AppWidgetManager.INVALID_APPWIDGET_ID;


    public IngredientsWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget


    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        getRecipe();
        setContentView(R.layout.ingredients_widget_configure);

        //set up listview adapter
        ListView recipesList = (ListView)findViewById(R.id.configure_list_view);
        for(String r: recipeNames){
            Log.d(TAG, "onCreate: recipe name "+r);
        }
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            final int appwidgetid = mAppWidgetId;
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        arrayAdapter = new ArrayAdapter<>(this, R.layout.widget_configure_recipe_item, recipeNames);
        recipesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Context context = IngredientsWidgetConfigureActivity.this;
                recipeForWidget = data.get(position);//get recipe from the position

                Intent intent;

                intent = new Intent(view.getContext(), RecipeIngService.class);
                intent.putParcelableArrayListExtra(getString(R.string.ingredients), (ArrayList<? extends Parcelable>) recipeForWidget.getIngredients());
                intent.putExtra(getString(R.string.recipename), recipeForWidget.getName());
                view.getContext().startService(intent);

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance((context));
                IngredientsWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);

                finish();
            }
        });

        recipesList.setAdapter(arrayAdapter);

        //mAppWidgetText = (EditText) findViewById(R.id.appwidget_text);
        //findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.


//        mAppWidgetText.setText(loadTitlePref(IngredientsWidgetConfigureActivity.this, mAppWidgetId));
    }

    void getRecipe(){
        DataService.getService().getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(response.isSuccessful()){
                    if(!data.isEmpty()){
                        data.clear();
                    }
                    data.addAll(response.body());
                    for(Recipe r:data){
                        recipeNames.add(r.getName());

                    }
                    arrayAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

            }
        });


    }
}

