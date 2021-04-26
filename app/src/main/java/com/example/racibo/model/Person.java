package com.example.racibo.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "person")
public class Person {
    @PrimaryKey(autoGenerate = true)
    private int id;
   @ColumnInfo(name="fname")
    private String fname;
   @ColumnInfo(name="lname")
    private String lname;
   @ColumnInfo(name="phone")
    private String phone;

    public Person(int id, String fname, String lname, String phone) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
    }

    @Ignore
    public Person(String fname, String lname, String phone) {
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
