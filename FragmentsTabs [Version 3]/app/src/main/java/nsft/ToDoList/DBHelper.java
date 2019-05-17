package nsft.ToDoList;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private final static String TAG = "EDMDev";

    // DECLARING THE VARIABLES FOR THE DB  //
    private static String DB_Name   = "EDMTDev";
    private static int    DB_Ver    = 1;
    private static String DB_TablePersonal  = "PersonalTable";
    private static String DB_TableBusiness  = "BusinessTable";
    private static String DB_TableGallery   = "GalleryTable";
    private static String DB_ColForPersonal = "PersonalColumn";
    private static String DB_ColForBusiness = "PersonalColumn";
    private static String DB_ColForGallery  = "GalleryColumn";


    Cursor cursor;

    // DECLARING THE CONTEXT //
    private Context context;

    public DBHelper(Context context) {

        super(context, DB_Name, null, DB_Ver);
        Log.d(TAG, "CONSTRUCTOR OF THE DATABASE CREATED");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // EXECUTING THE QUERY FOR MY SQL TABLE //
        String createTablePersonal = String.format("CREATE TABLE " + DB_TablePersonal + " ( " +
                                                   " ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                   DB_ColForPersonal + " TEXT NOT NULL )");

        String createTableBusiness = String.format("CREATE TABLE " + DB_TableBusiness + " ( " +
                                                   " ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                   DB_ColForBusiness + " TEXT NOT NULL )");

        String createTableGallery = String.format("CREATE TABLE " + DB_TableGallery + " ( " +
                                                  " ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                   DB_ColForGallery + " TEXT NOT NULL )");

        db.execSQL(createTablePersonal);
        db.execSQL(createTableBusiness);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DB_TablePersonal);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TableBusiness);
        db.execSQL("DROP TABLE IF EXISTS " + DB_ColForGallery);
        onCreate(db);
    }

    public void insertToPersonal(String task) {
        Log.d("DbHelper", "INSERTING TASK '" + task + "'");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // PUTTING THE DATA INTO ITS PERSPECTIVE COLUMN //
        values.put(DB_ColForPersonal, task);
        db.insertWithOnConflict(DB_TablePersonal, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        // CLOSING THE DATABASE //
        db.close();
    }

    public void insertToBusiness(String task) {
        Log.d("TAG", "INSERTING TASK '" + task + "'");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // PUTTING THE DATA INTO ITS PERSPECTIVE COLUMN //
        values.put(DB_ColForBusiness, task);
        db.insertWithOnConflict(DB_TableBusiness, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        // CLOSING THE DATABASE //
        db.close();
    }

    public void insertToGallery(ImageView img) {
        Log.d(TAG, "insertToGallery: INSERTING IMAGE TO DATABASE");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DB_ColForGallery, String.valueOf(img));
        db.insertWithOnConflict(DB_TableGallery, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void deleteFromPersonal(int task) {
        Log.d(TAG, "deleteFromPersonal: DELETING '" + task + "'");
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(DB_TablePersonal, DB_ColForPersonal + " = ?", new String[]{String.valueOf(task)});

        db.close();
    }

    public void deleteFromBusinessl(int task) {
        Log.d(TAG, "deleteFromPersonal: DELETING '" + task + "'");
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(DB_TableBusiness, DB_ColForBusiness + " = ?", new String[]{String.valueOf(task)});

        db.close();
    }

    public ArrayList<String> loadPersonalList() {

        // NEW OBJECTS
        ArrayList<String> task = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Log.d("DbHelper", "GETTING THE ARRAYLIST '" + task + "'");

        //
        cursor = db.query(DB_TablePersonal, new String[]{DB_ColForPersonal}, null, null, null, null, null);

        //
        while (cursor.moveToNext()){
            int index = cursor.getColumnIndex(DB_ColForPersonal);
            task.add(cursor.getString(index));
        }

        // CLOSING THE CURSOR AND THE DATABASE //
        cursor.close();
        db.close();

        // RETURNING THE TASKLIST //
        return task;
    }

    public ArrayList<String> loadBusinessList() {

        // NEW OBJECTS
        ArrayList<String> task = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Log.d("DbHelper", "GETTING THE ARRAYLIST '" + task + "'");

        //
        cursor = db.query(DB_TableBusiness, new String[]{DB_ColForBusiness}, null, null, null, null, null);

        //
        while (cursor.moveToNext()){
            int index = cursor.getColumnIndex(DB_ColForBusiness);
            task.add(cursor.getString(index));
        }

        // CLOSING THE CURSOR AND THE DATABASE //
        cursor.close();
        db.close();

        // RETURNING THE TASKLIST //
        return task;
    }
}
