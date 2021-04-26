package com.example.racibo.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.adapters.TextViewBindingAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.racibo.activity.MainActivity;
import com.example.racibo.database.AppDataBase;
import com.example.racibo.database.AppExecutors;

import static com.example.racibo.activity.MainActivity.mDb;

public class UserDataViewModel extends ViewModel implements LifecycleOwner {

    private ListTask loadTask;

    private final MutableLiveData<String> firstName = new MediatorLiveData<>();
    private final MutableLiveData<String> lastName = new MediatorLiveData<>();
    private final MutableLiveData<String> phone = new MediatorLiveData<>();
    private final MutableLiveData<Integer> id = new MediatorLiveData<>();

    private final MutableLiveData<String> error = new MutableLiveData<>();
    int count;
    /**
     * Expose LiveData if you do not use two-way data binding
     */
    public LiveData<String> getFirstName() {
        return firstName;
    }
    public LiveData<String> getLastName(){
        return lastName;
    }
    public LiveData<String> getPh(){
        return phone;
    }

    /**
     * Expose MutableLiveData to use two-way data binding
     */

    public MutableLiveData<String>getFname(){
        return firstName;
    }
    public MutableLiveData<String> getLstName() {
        return lastName;
    }
    public MutableLiveData<String>getPhone(){
        return phone;
    }
    public MutableLiveData<Integer>getId() { return  id; }

    public LiveData<String> getError() {
        return error;
    }

    @MainThread
    public void loadUser(String userId) {
        // cancel previous running task
        cancelLoadTask();
        loadTask = new ListTask();
        Observer<Person> observer = new Observer<Person>() {
            @Override
            public void onChanged(@Nullable Person userData) {
                firstName.setValue(userData == null? null : userData.getFname());
                lastName.setValue(userData == null? null : userData.getLname());
                phone.setValue(userData == null? null : userData.getPhone());
                id.setValue(userData == null ? null : userData.getId());
                loadTask.getUserData().removeObserver(this);
            }

        };
        loadTask.getUserData().observeForever(observer);
        loadTask.execute(userId);
    }

    /*public void onTextChange(CharSequence text) {
        // TODO do something with text
        save();
    }*/



    public void save() {
        Log.d("saveeee","enter");
        error.setValue(null);
        String fName = firstName.getValue(), lName = lastName.getValue(),phon=phone.getValue();
        if (fName == null || lName == null||phon == null||phon.length()==10) {
            error.setValue("Opps! Data is invalid");
            return;
        }
        Person newData = new Person(fName, lName,phon);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(MainActivity.count==0) {
                    mDb.personDao().insertPerson(newData);
                }
                else if(MainActivity.count==1){
                    mDb.personDao().update(String.valueOf(newData.getFname()),String.valueOf(newData.getLname()), String.valueOf(newData.getPhone()),
                            MainActivity.id);
                }
            }
        });
        mDb.personDao().getCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                count=integer;
                Log.d("count  ",String.valueOf(integer));
            }
        });
    }



    @Override
    protected void onCleared() {
        super.onCleared();
        cancelLoadTask();
    }

    private void cancelLoadTask() {
        if (loadTask != null)
            loadTask.cancel(true);
        loadTask = null;
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return null;
    }


    
}