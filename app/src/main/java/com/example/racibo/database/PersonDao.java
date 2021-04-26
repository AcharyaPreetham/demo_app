package com.example.racibo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.racibo.model.Person;

import java.util.List;

@Dao
public interface PersonDao {
    @Query("Select * from person")
    List<Person>getPersonList();

    @Query("SELECT * FROM PERSON ORDER BY ID")
    LiveData<List<Person>> loadAllPersons();

    @Insert
    void insertPerson(Person person);

    @Update
    void updatePerson(Person person);

    @Delete
    void deletePerson(Person person);

    @Query("SELECT * FROM PERSON WHERE id = :id")
    Person loadPersonById(int id);

    @Query("SELECT COUNT(*) FROM PERSON")
    LiveData<Integer> getCount();

    @Query("UPDATE person SET fname = :fname, lname = :lname,phone=:phone WHERE id =:id")
    void update(String fname,String lname,String phone,int id);



}
