package com.example.hiren_pc_hp.bakingapp.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hiren_pc_hp.bakingapp.R;
import com.example.hiren_pc_hp.bakingapp.network.Step;
import com.example.hiren_pc_hp.bakingapp.view.recipeDetailFragment.OnListFragmentInteractionListener;


import java.util.List;


public class MyrecipeDetailRecyclerViewAdapter extends RecyclerView.Adapter<MyrecipeDetailRecyclerViewAdapter.ViewHolder> {



    private List<Step> steps;
    private final OnListFragmentInteractionListener mListener;


    public MyrecipeDetailRecyclerViewAdapter(List<Step> steps, OnListFragmentInteractionListener listener) {

        this.steps = steps;
        mListener = listener;
    }

    @Override//to help distinguish between ingredients and non-ingredients
    public int getItemViewType(int position) {
        if(position==0){
            return 1;
        }else return 2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recipedetail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Step iStepObject = steps.get(position);

        holder.mIdView.setText("Step "+String.valueOf(iStepObject.getId()));
        holder.mContentView.setText(iStepObject.getShortDescription());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(steps.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return steps.size();
    }

    public void update(List<Step> step){

        this.steps = step;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.description);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

    }
}
