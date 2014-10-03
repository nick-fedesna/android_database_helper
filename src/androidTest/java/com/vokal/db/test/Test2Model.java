package com.vokal.db.test;


import com.vokal.codegen.Column;
import com.vokal.codegen.Unique;
import com.vokal.db.codegen.DataModel;

public class Test2Model extends DataModel {

    @Unique
    @Column int     int1;
    @Column String  string1;
    @Column boolean boolean1;
    @Column long    long1;
    @Column double  double1;

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        this._id = id;
    }
}
