package com.vokal.db.test;


import com.vokal.codegen.Column;
import com.vokal.db.codegen.DataModel;

public class TestModel extends DataModel {

    @Column int     int1;
    @Column long    long1;
    @Column double  double1;
    @Column String  string1;
    @Column boolean boolean1;

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        this._id = id;
    }
}
