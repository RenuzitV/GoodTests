package com.example.cosc2657_assignment1.Question;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class QuestionViewModel extends ViewModel {
        private final MutableLiveData<Question> selectedItem = new MutableLiveData<Question>(new Question());

        public void selectItem(Question question) {
            selectedItem.setValue(question);
        }

        public LiveData<Question> getSelectedItem() {
            return selectedItem;
        }
}
