package com.vokal.db.test;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;

import java.util.ArrayList;

import com.vokal.db.*;
import com.vokal.db.util.ObjectCursor;

public class DataModelTest extends ProviderTestCase2<SimpleContentProvider> {

    private Context mContext;

    public DataModelTest() {
        super(SimpleContentProvider.class,"com.vokal.database");

    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getMockContext();
        DatabaseHelper.registerModel(mContext, TestModel.class, Test2Model.class);
    }

    public void testInsert() {
        TestModel testModel = new TestModel();
        testModel.boolean1 = false;
        testModel.double1 = 2.3;
        testModel.string1 = "test";
        testModel.long1 = 123123l;
        Uri uri = testModel.save(mContext);
        assertNotNull(uri);

        long id = testModel.getId();

        Cursor c = getMockContentResolver().query(DatabaseHelper.getContentUri(TestModel.class),null,null,null,null);
        ObjectCursor<TestModel> cursor = new ObjectCursor<TestModel>(c, TestModel.class);
        if (cursor.moveToFirst()) {
            TestModel m = cursor.getModel();
            assertEquals(false, m.boolean1);
            assertEquals(2.3, m.double1);
            assertEquals("test", m.string1);
            assertEquals(123123l, m.long1);
            assertEquals(id, m.getId());
        } else {
            assertFalse("cursor empty", true);
        }
    }

    public void testDelete() {
        TestModel testModel = new TestModel();
        testModel.boolean1 = false;
        testModel.double1 = 2.3;
        testModel.string1 = "test";
        testModel.long1 = 123123l;
        Uri uri = testModel.save(mContext);
        assertNotNull(uri);
        boolean success = testModel.delete(mContext);
        assertTrue(success);
    }


    public void testBulkInsert() {
        TestModel testModel = new TestModel();
        testModel.boolean1 = false;
        testModel.double1 = 1.3;
        testModel.string1 = "tasdf";
        testModel.long1 = 23123123l;

        TestModel testModel2 = new TestModel();
        testModel.boolean1 = true;
        testModel.double1 = 2.1;
        testModel.string1 = "aaaa";
        testModel.long1 = 2312l;

        TestModel testModel3 = new TestModel();
        testModel.boolean1 = false;
        testModel.double1 = 2.3;
        testModel.string1 = "test";
        testModel.long1 = 123123l;

        ArrayList<AbstractDataModel> models = new ArrayList<AbstractDataModel>();
        models.add(testModel);
        models.add(testModel2);
        models.add(testModel3);

        int count = TestModel.bulkInsert(mContext, models);
        assertEquals(count, 3);

    }

    public void testUpdate() {
        TestModel testModel = new TestModel();
        testModel.boolean1 = false;
        testModel.double1 = 2.3;
        testModel.string1 = "test";
        testModel.long1 = 123123l;
        Uri uri = testModel.save(mContext);
        assertNotNull(uri);

        testModel.boolean1 = true;
        testModel.double1 = 4.1;

        uri = testModel.save(mContext);
        assertNotNull(uri);

        long id = testModel.getId();

        Cursor c = getMockContentResolver().query(DatabaseHelper.getContentUri(TestModel.class),null,null,null,null);
        ObjectCursor<TestModel> cursor = new ObjectCursor<TestModel>(c, TestModel.class);
        if (cursor.moveToFirst()) {
            TestModel m = cursor.getModel();
            assertEquals(true, m.boolean1);
            assertEquals(4.1, m.double1);
            assertEquals(id, m.getId());
        } else {
            assertFalse("cursor empty", true);
        }
    }

    public void testWipeDatabase() {
        TestModel testModel = new TestModel();
        testModel.boolean1 = false;
        testModel.double1 = 2.3;
        testModel.string1 = "test";
        testModel.long1 = 123123l;
        testModel.save(mContext);

        Test2Model test2Model = new Test2Model();
        test2Model.boolean1 = true;
        test2Model.double1 = 3.4;
        test2Model.string1 = "test2";
        test2Model.long1 = 555444333;
        test2Model.save(mContext);

        Cursor c = getMockContentResolver().query(DatabaseHelper.getContentUri(TestModel.class),null,null,null,null);
        ObjectCursor<TestModel> cursor = new ObjectCursor<TestModel>(c, TestModel.class);
        if (cursor.moveToFirst()) {
            TestModel m = cursor.getModel();
            assertEquals(false, m.boolean1);
            assertEquals(2.3, m.double1);
            assertEquals("test", m.string1);
            assertEquals(123123l, m.long1);
        } else {
            assertFalse("cursor empty", true);
        }

        c = getMockContentResolver().query(DatabaseHelper.getContentUri(Test2Model.class),null,null,null,null);
        ObjectCursor<Test2Model> cursor2 = new ObjectCursor<Test2Model>(c, Test2Model.class);
        if (cursor2.moveToFirst()) {
            Test2Model m = cursor2.getModel();
            assertEquals(true, m.boolean1);
            assertEquals(3.4, m.double1);
            assertEquals("test2", m.string1);
            assertEquals(555444333, m.long1);
        } else {
            assertFalse("cursor empty", true);
        }

        DatabaseHelper.wipeDatabase(mContext);

        c = getMockContentResolver().query(DatabaseHelper.getContentUri(TestModel.class),null,null,null,null);
        cursor = new ObjectCursor<TestModel>(c, TestModel.class);
        assertEquals(cursor.moveToFirst(), false);

        c = getMockContentResolver().query(DatabaseHelper.getContentUri(Test2Model.class),null,null,null,null);
        cursor2 = new ObjectCursor<Test2Model>(c, Test2Model.class);
        assertEquals(cursor2.moveToFirst(), false);


    }

    public void testAutoIncrement() {
        TestModel testModel = new TestModel();
        testModel.boolean1 = false;
        testModel.double1 = 2.3;
        testModel.string1 = "test";
        testModel.long1 = 123123l;
        testModel.save(mContext);

        long id = testModel.getId();

        TestModel test2Model = new TestModel();
        test2Model.boolean1 = true;
        test2Model.double1 = 3.4;
        test2Model.string1 = "test2";
        test2Model.long1 = 555444333;
        test2Model.save(mContext);

        long id2 = test2Model.getId();

        assertEquals(id+1, id2);

    }

    public void testUniqueness() {
        Test2Model testModel = new Test2Model();
        testModel.boolean1 = false;
        testModel.double1 = 2.3;
        testModel.string1 = "test";
        testModel.long1 = 123123l;
        testModel.int1 = 12;
        testModel.save(mContext);

        assertEquals(testModel.int1, 12);

        Cursor c = getMockContentResolver().query(DatabaseHelper.getContentUri(Test2Model.class),null,null,null,null);
        assertTrue(c.moveToFirst());
        assertEquals(c.getCount(), 1);

        Test2Model test2Model = new Test2Model();
        test2Model.boolean1 = true;
        test2Model.double1 = 3.4;
        test2Model.string1 = "test2";
        test2Model.long1 = 555444333;
        test2Model.int1 = 12;
        test2Model.save(mContext);
        assertEquals(test2Model.int1, 12);

        Cursor c2 = getMockContentResolver().query(DatabaseHelper.getContentUri(Test2Model.class),null,null,null,null);
        ObjectCursor<Test2Model> cursor2 = new ObjectCursor<>(c2, Test2Model.class);
        assertTrue(c2.moveToFirst());
        assertEquals(c2.getCount(), 1);
        assertEquals(cursor2.getModel().double1, 3.4);
    }

}
