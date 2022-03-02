package edu.upenn.cis350.cisproject.ui.safety;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SafetyViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SafetyViewModel() {
        mText = new MutableLiveData<>();

        mText.setValue("This is send fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}