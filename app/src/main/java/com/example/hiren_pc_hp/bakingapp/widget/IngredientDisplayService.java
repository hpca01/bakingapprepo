package com.example.hiren_pc_hp.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import static android.support.constraint.Constraints.TAG;

import com.example.hiren_pc_hp.bakingapp.R;
import com.example.hiren_pc_hp.bakingapp.network.Ingredient;

import java.util.List;

public class IngredientDisplayService extends RemoteViewsService {

    List<Ingredient> mIngredients;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewFactory(getApplicationContext(), intent);
    }

    class ListViewFactory implements RemoteViewsService.RemoteViewsFactory{
        private Context  mContext;

        public ListViewFactory(Context context, Intent intent) {
            mContext = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            mIngredients = IngredientsWidget.mIngredients;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return mIngredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredient_item);
            if(mIngredients!=null){
                Log.d(TAG, "getViewAt: in view at position "+position+" Ingredient "+mIngredients.get(position).getIngredient());
                Ingredient ingredient = mIngredients.get(position);
                //set text quantity
                views.setTextViewText(R.id.widget_ingredient_qty, String.valueOf(ingredient.getQuantity()+" "+ingredient.getMeasure()));
                //set text ingredient
                views.setTextViewText(R.id.widget_ingredient_name, ingredient.getIngredient());
            }
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
