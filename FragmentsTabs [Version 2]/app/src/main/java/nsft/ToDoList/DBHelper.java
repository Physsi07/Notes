package nsft.ToDoList;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private final static String Tag = "EDMDev";

    // DECLARING THE VARIABLES FOR THE DB  //
    private static String DB_Name   = "EDMTDev";
    private static int    DB_Ver    = 1;
    private static String DB_Table  = "TASK";
    private static String DB_Col1   = "Personal";
    private static String DB_Col2   = "Business";

    Cursor cursor;

    // DECLARING THE CONTEXT //
    private Context context;

    public DBHelper(Context context) {

        super(context, DB_Name, null, DB_Ver);
        Log.d(Tag, "CONSTRUCTOR OF THE DATABASE CREATED");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // SQL QUERY //
        String query = String.format("CREATE TABLE " + DB_Table + " ( ID INTEGER PRIMARY KEY AUTOINCREMENT, " +  DB_Col1 + " TEXT)");
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //
        String query = String.format("DELETE TABLE IF EXIST %s", DB_Table);
        db.execSQL(query);
        onCreate(db);
    }

    public void insert(String personalTask) {
        Log.d("DbHelper", "INSERTING TASK '" + personalTask + "'");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // PUTTING THE DATA INTO ITS PERSPECTIVE COLUMN //
        values.put(DB_Col1, personalTask);
//        values.put(DB_Col2, businessTask);

        //
        db.insertWithOnConflict(DB_Table, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        // CLOSING THE DATABASE //
        db.close();
    }

    public void insertInPersonal(String personalTask) {
        Log.d("DbHelper", "INSERTING TASK '" + personalTask + "'");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // PUTTING THE DATA INTO ITS PERSPECTIVE COLUMN //
        values.put(DB_Col1, personalTask);

        //
        db.insertWithOnConflict(DB_Table, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        // CLOSING THE DATABASE //
        db.close();
    }

    public void insertInBusiness(String businessTask) {
        Log.d("DbHelper", "INSERTING TASK '" + businessTask + "'");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // PUTTING THE DATA INTO ITS PERSPECTIVE COLUMN //
        values.put(DB_Col2, businessTask);

        //
        db.insertWithOnConflict(DB_Table, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        // CLOSING THE DATABASE //
        db.close();
    }

    public void deletePersonalTask(String personalTask) {
        Log.d("DbHelper", "DELETING TASK '" + personalTask + "'");
        SQLiteDatabase db = this.getWritableDatabase();

        // DELETING THE TASK FROM THE DATABASE AND FROM THE TABLE AND COLUMN //
        db.delete(DB_Table, DB_Col1 + "- ?", new String[]{personalTask});

        // CLOSING THE DATABASE //
        db.close();
    }

    public void deleteBusinessTask(String businessTask) {
        Log.d("DbHelper", "DELETING TASK '" + businessTask + "'");
        SQLiteDatabase db = this.getWritableDatabase();

        // DELETING THE TASK FROM THE DATABASE AND FROM THE TABLE AND COLUMN //
        db.delete(DB_Table, DB_Col2 + "- ?", new String[]{businessTask});

        // CLOSING THE DATABASE //
        db.close();
    }

    public ArrayList<String> loadList() {
        // NEW OBJECTS
        ArrayList<String> task = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Log.d("DbHelper", "GETTING THE ARRAYLIST '" + task + "'");

        //
        cursor = db.query(DB_Table, new String[]{DB_Col1}, null, null, null, null, null);

        //
        while (cursor.moveToNext()){
            int index = cursor.getColumnIndex(DB_Col1);
            task.add(cursor.getString(index));
        }

        // CLOSING THE CURSOR AND THE DATABASE //
        cursor.close();
        db.close();

        // RETURNING THE TASKLIST //
        return task;
    }
}
