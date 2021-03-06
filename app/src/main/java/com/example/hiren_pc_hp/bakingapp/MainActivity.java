package com.example.hiren_pc_hp.bakingapp;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.support.v7.widget.Toolbar;


import com.example.hiren_pc_hp.bakingapp.network.Recipe;
import com.example.hiren_pc_hp.bakingapp.network.Step;
import com.example.hiren_pc_hp.bakingapp.ui.UtilsForUi;
import com.example.hiren_pc_hp.bakingapp.data.RecipeViewModel;
import com.example.hiren_pc_hp.bakingapp.view.IngredientsFragment;
import com.example.hiren_pc_hp.bakingapp.view.RecipeFragment;
import com.example.hiren_pc_hp.bakingapp.view.StepFragment;
import com.example.hiren_pc_hp.bakingapp.view.recipeDetailFragment;

public class MainActivity extends AppCompatActivity implements recipeDetailFragment.OnListFragmentInteractionListener, RecipeFragment.recipeInteractionListener, UtilsForUi {

    private boolean tabletLayout = false;//assume mobile layout
    private Toolbar mToolBar;
    public RecipeViewModel recipeViewModel;
    private static final String TAG = "Constraints";

    public static String STACK_RECIPE_DETAIL_TABLET = "recipe_detail_stack";

    public static String STEP_FRAGMENT_SAVE = "step_frag";

    public boolean backEnabled = false;

    public Bundle stepsBundle = new Bundle();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mToolBar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolBar);

        if(findViewById(R.id.linear_activity_layout).getTag() == getString(R.string.mobile_layout)){
            tabletLayout = false;
        }else{
            tabletLayout = true;
            if(savedInstanceState != null) {
                int detail_frag_view = savedInstanceState.getInt("detail_frag_state");
                if(detail_frag_view == View.GONE){
                    findViewById(R.id.detail_frag).setVisibility(View.GONE);
                }else{
                    findViewById(R.id.detail_frag).setVisibility(View.VISIBLE);
                }
            }
             else {
                findViewById(R.id.detail_frag).setVisibility(View.GONE);
            }

        }

        if(savedInstanceState == null) {
            RecipeFragment recipeFragment = new RecipeFragment();
            replaceFragment(recipeFragment, false, null);
        }else {
            if(savedInstanceState.containsKey(getString(R.string.stepBundle))){
                Log.d(TAG, "onCreate: MAIN steps bundle");
                stepsBundle = savedInstanceState.getBundle(getString(R.string.stepBundle));
                Fragment stepFrag = getSupportFragmentManager().findFragmentById(R.id.recipe_frag);
                boolean playing = stepsBundle.getBoolean(getString(R.string.playback_state));
                int stepID = stepsBundle.getInt(getString(R.string.step_id));
                Step astep = stepsBundle.getParcelable(getString(R.string.step_url));
                long playbackPosition = stepsBundle.getLong(getString(R.string.current_position));
                ((StepFragment) stepFrag).setValues(astep, stepID, playbackPosition, playing);
            }
        }

        recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class );
        //mToolBarTitle = findViewById(R.id.toolbar_title);

    }

    public void replaceFragment(Fragment fragment, boolean addToStack, Bundle args){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (args!= null){
            fragment.setArguments(args);
        }
        transaction.replace(R.id.recipe_frag, fragment);
        if(addToStack){
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void replaceFragmentTablet(Fragment fragment, boolean addToStack, Bundle args){

        FrameLayout detailFrag = findViewById(R.id.detail_frag);
        if(detailFrag.getVisibility() == View.GONE){
            detailFrag.setVisibility(View.VISIBLE);
            //make the detail fragment visible again.
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (args!= null){
            fragment.setArguments(args);
        }
        transaction.replace(R.id.detail_frag, fragment);
        if(addToStack){
            transaction.addToBackStack(STACK_RECIPE_DETAIL_TABLET);
        }
        transaction.commit();
    }


    @Override
    public void onListFragmentInteraction(Step step) {
        StepFragment stepFragment = new StepFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.step_url), step);
        //for step fragment
        if(tabletLayout) {
            replaceFragmentTablet(stepFragment, true, args);
        }
        else {
            replaceFragment(stepFragment, true, args);
        }
    }

    @Override
    public void onRecipeInteractionListener(Recipe recipe) {
        recipeDetailFragment recipeDetail = new recipeDetailFragment();
        recipeViewModel.setSteps(recipe.getSteps());
        recipeViewModel.setIngredients(recipe.getIngredients());
        Bundle args = new Bundle();
        args.putString(getString(R.string.recipename), recipe.getName());
        //for recipe detail frag, only comes up when a recipe is clicked.
        if(tabletLayout){
            replaceFragment(recipeDetail, true, args);
            getIngredientsList(recipe.getName());//default ingredient view?
        }else {
            replaceFragment(recipeDetail, true, args);
        }
    }

    @Override
    public void setToolBarTitle(String fragtag) {
        getSupportActionBar().setTitle(fragtag);
    }

    @Override
    public void getIngredientsList(String fragtag) {
        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putString(getString(R.string.recipename), fragtag);
        if(tabletLayout){
            replaceFragmentTablet(ingredientsFragment, true, args);
        }else{
            replaceFragment(ingredientsFragment, true, args);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i< fm.getBackStackEntryCount(); i++){
            fm.popBackStack();
        }
        RecipeFragment recipeFragment = new RecipeFragment();
        replaceFragment(recipeFragment, false, null);
        toggleBack(false);
        return super.onSupportNavigateUp();
    }

    @Override
    public void toggleBack(boolean enable) {
        backEnabled = enable;
        getSupportActionBar().setDisplayHomeAsUpEnabled(backEnabled);
        getSupportActionBar().setDisplayShowHomeEnabled(backEnabled);

    }

    @Override
    public void postStepsState(Bundle aBundle) {
        stepsBundle = aBundle;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mToolBar.setTitle(getString(R.string.main_activity));
        toggleBack(false);
        if(tabletLayout){
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.recipe_frag);
            if(currentFragment instanceof RecipeFragment){
                FrameLayout detail_frag = findViewById(R.id.detail_frag);
                detail_frag.setVisibility(View.GONE);//hide the detail frag if back pressed
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null) {
            Fragment fragment = getSupportFragmentManager().getFragment(savedInstanceState, STEP_FRAGMENT_SAVE);
            if (fragment instanceof StepFragment) {
                stepsBundle = savedInstanceState.getBundle(getString(R.string.stepBundle));
                boolean playing = stepsBundle.getBoolean(getString(R.string.playback_state));
                int stepID = stepsBundle.getInt(getString(R.string.step_id));
                Step astep = stepsBundle.getParcelable(getString(R.string.step_url));
                long playbackPosition = stepsBundle.getLong(getString(R.string.current_position));
                Log.d(TAG, String.format("onRestoreInstanceState: MAIN ACTIVITY restore state %s, %d, %s, %d", playing, stepID, astep.getShortDescription(), playbackPosition));
                //((StepFragment)fragment).refreshStep(astep.getId());
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //need to save the visibility of detail frag no t the last fragment.
        Log.d(TAG, "onSaveInstanceState: MAIN ACTIVITY  onsaveinstance state called");
        if(tabletLayout){
            outState.putInt("detail_frag_state", findViewById(R.id.detail_frag).getVisibility());
        }else{
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.recipe_frag);
            if(fragment instanceof StepFragment){
                Log.d(TAG, "onSaveInstanceState: MAIN bundle found");
                getSupportFragmentManager().putFragment(outState, STEP_FRAGMENT_SAVE, fragment);
                outState.putBundle(getString(R.string.stepBundle), stepsBundle);
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: MAIN on stop called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: MAIN on start called");
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.recipe_frag);
        if(fragment instanceof StepFragment){
            fragment.setArguments(this.stepsBundle);
            boolean playing = stepsBundle.getBoolean(getString(R.string.playback_state));
            int stepID = stepsBundle.getInt(getString(R.string.step_id));
            Step astep = stepsBundle.getParcelable(getString(R.string.step_url));
            long playbackPosition = stepsBundle.getLong(getString(R.string.current_position));
            ((StepFragment)fragment).setValues(astep, stepID, playbackPosition, playing);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: MAIN on resume called");
    }
    
}
