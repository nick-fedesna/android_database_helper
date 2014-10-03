package com.vokal.db.test;


import com.vokal.codegen.Column;
import com.vokal.db.codegen.SuperClass;

public class TestModel extends SuperClass {

        @Column public String  string1;
        @Column public boolean boolean1;
        @Column public int     int1;
        @Column public long    long1;
        @Column public double  double1;

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        this._id = id;
    }
}
