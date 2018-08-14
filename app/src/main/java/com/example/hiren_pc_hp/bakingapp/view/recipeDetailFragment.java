package com.example.hiren_pc_hp.bakingapp.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.example.hiren_pc_hp.bakingapp.MainActivity;
import com.example.hiren_pc_hp.bakingapp.R;
import com.example.hiren_pc_hp.bakingapp.network.Ingredient;
import com.example.hiren_pc_hp.bakingapp.network.Recipe;
import com.example.hiren_pc_hp.bakingapp.network.Step;
import com.example.hiren_pc_hp.bakingapp.ui.MyrecipeDetailRecyclerViewAdapter;
import com.example.hiren_pc_hp.bakingapp.ui.UtilsForUi;
import com.example.hiren_pc_hp.bakingapp.data.RecipeViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class recipeDetailFragment extends Fragment {

    private Unbinder unbinder;

    //simple button code
    @BindView(R.id.ingredients_button)
    Button mIngredientsButton;

    @OnClick(R.id.ingredients_button)
    public void onClickButton(){
        title.getIngredientsList(TAG);
    }

    public UtilsForUi title;
    private String TAG = "";
    MyrecipeDetailRecyclerViewAdapter recyclerViewAdapter;

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public recipeDetailFragment() {
    }


    @SuppressWarnings("unused")
    public static recipeDetailFragment newInstance(int columnCount) {
        recipeDetailFragment fragment = new recipeDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recyclerViewAdapter = new MyrecipeDetailRecyclerViewAdapter(
               new ArrayList<Step>(), mListener);

        View view = inflater.inflate(R.layout.fragment_recipedetail_list, container, false);
        unbinder = ButterKnife.bind(this,view);


        if(getArguments()!=null) {
            TAG = getArguments().getString(getString(R.string.recipename));
        }else{
            TAG = getTag();
        }
        title.setToolBarTitle(TAG);
        RecyclerView iview = (RecyclerView) view.findViewById(R.id.list);

        // Set the adapter
        if (iview instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = iview;

            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(recyclerViewAdapter);

            ViewModelProviders.of(getActivity()).get(RecipeViewModel.class).getSteps().observe(this, new Observer<List<Step>>() {
                @Override
                public void onChanged(@Nullable List<Step> steps) {
                    recyclerViewAdapter.update(steps);
                }
            });
        }

        return view;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
        title = (MainActivity)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Step aStep);
    }
}
