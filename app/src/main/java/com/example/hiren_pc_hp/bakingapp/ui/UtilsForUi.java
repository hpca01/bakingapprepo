package com.example.hiren_pc_hp.bakingapp.ui;

import android.os.Bundle;

public interface UtilsForUi {
    void setToolBarTitle(String fragtag);

    void getIngredientsList(String fragtag);

    void toggleBack(boolean enable);

    void postStepsState(Bundle bundle);

    //void getNextStep(int pos);
    //void getPrevStep(int pos);
}
