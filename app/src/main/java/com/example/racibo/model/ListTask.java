package com.example.racibo.model;


import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

public class ListTask extends AsyncTask<String, Void, Person> {

    private final MutableLiveData<Person> data= new MediatorLiveData<>();

    public LiveData<Person> getUserData() {
        return data;
    }

    /*@Override
    protected Person doInBackground(String... strings) {
        return strings;
    }*/

    @Override
    protected void onPostExecute(Person userData) {
        data.setValue(userData);
    }

    @Override
    protected Person doInBackground(String[] userId) {
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
