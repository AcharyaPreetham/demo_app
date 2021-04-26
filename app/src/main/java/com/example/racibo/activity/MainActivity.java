package com.example.racibo.activity;

import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.racibo.R;
import com.example.racibo.database.AppDataBase;
import com.example.racibo.database.AppExecutors;
import com.example.racibo.databinding.ActivityMainBinding;
import com.example.racibo.model.ListTask;
import com.example.racibo.model.Person;
import com.example.racibo.model.UserDataViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static AppDataBase mDb;
    private UserDataViewModel viewModel;
    public static int count,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        viewModel = ViewModelProviders.of(MainActivity.this).get(UserDataViewModel.class);
        ActivityMainBinding binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        // start user loading (if necessary)
        viewModel.loadUser("user_id");
        mDb = AppDataBase.getInstance(getApplicationContext());

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.editTextTextPersonName.getText().toString().equalsIgnoreCase("") &&
                        !binding.editTextTextPersonName2.getText().toString().equalsIgnoreCase("") &&
                        !binding.editTextTextPersonName3.getText().toString().equalsIgnoreCase("")) {

                    Person person = new Person(binding.editTextTextPersonName.getText().toString(),
                            binding.editTextTextPersonName2.getText().toString(),
                            binding.editTextTextPersonName3.getText().toString());
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if(count==0) {
                                mDb.personDao().insertPerson(person);
                            }
                            else if(count==1){
                                mDb.personDao().update(String.valueOf(person.getFname()),String.valueOf(person.getLname()), String.valueOf(person.getPhone()),
                                        id);
                            }
                        }
                    });
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveTasks();
    }

    public void retrieveTasks() {
        mDb.personDao().loadAllPersons().observe(this, new Observer<List<Person>>() {
            @Override
            public void onChanged(@Nullable List<Person> persons) {
                for (int i = 0; i < persons.size(); i++) {
                    Log.d("valueess",persons.get(i).getId()+"\n"+persons.get(i).getFname()+"\n"+persons.get(i).getLname()+"\n"+persons.get(i).getPhone());
                    viewModel.getId().postValue(persons.get(i).getId());
                    id = persons.get(i).getId();
                    viewModel.getFname().postValue(persons.get(i).getFname());
                    viewModel.getLstName().postValue(persons.get(i).getLname());
                    viewModel.getPhone().postValue(persons.get(i).getPhone());
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
        viewModel.loadUser("user_id");
    }
}