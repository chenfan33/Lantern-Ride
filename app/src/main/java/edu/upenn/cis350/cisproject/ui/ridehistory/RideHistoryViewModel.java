package edu.upenn.cis350.cisproject.ui.ridehistory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RideHistoryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RideHistoryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}