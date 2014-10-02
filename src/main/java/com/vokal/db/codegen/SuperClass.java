package com.vokal.db.codegen;


import android.content.ContentValues;
import android.util.Log;

import com.vokal.db.AbstractDataModel;
import com.vokal.db.SQLiteTable;
import com.vokal.db.util.CursorCreator;
import com.vokal.db.util.CursorGetter;

public class SuperClass extends AbstractDataModel implements Model {

    ModelHelper mClass;

    public SuperClass() {
        try {
            Log.e("HERE", getClass().getName() + "Helper");
            mClass = ((ModelHelper) Class.forName(getClass().getName() + "Helper").newInstance());
            mClass.setObject(this);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            Log.e("HERE2", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void populateContentValue(ContentValues contentValues) {
        mClass.populateContentValue(contentValues);
    }

}
