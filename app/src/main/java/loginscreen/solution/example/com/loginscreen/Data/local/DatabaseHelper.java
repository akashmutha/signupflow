package loginscreen.solution.example.com.loginscreen.Data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import loginscreen.solution.example.com.loginscreen.Model.User;
import loginscreen.solution.example.com.loginscreen.Util.Constants;

/**
 * Created by mutha on 27/10/16.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE_USERS = "create table users " + " (" +
            "name" + " TEXT," +
            "email_id" + " TEXT PRIMARY KEY," +
            "phone_number" + " TEXT ," +
            "password" + " TEXT )";

    private static DatabaseHelper databaseHelper;
    private static final int DATABASE_VERSION = 1;

    // Have only one instance of database helper class
    public static synchronized DatabaseHelper getHelper(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    private DatabaseHelper(Context context) {
        super(context, Constants.USERS_DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String insertUser(final User user) throws SQLiteException {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = getContentValues(user);
        User tempUser = getUser(user.getEmailId());
        //If the user doesn't exist, insert it into the database
        if (tempUser == null) {
            db.insert(Constants.USERS_TABLE_NAME, null, contentValues);
        } else {
            return "User already exists!";
        }
        return "Your account has been created!";
    }

    @NonNull
    private ContentValues getContentValues(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", user.getName());
        contentValues.put("email_id", user.getEmailId());
        contentValues.put("phone_number", user.getPhoneNumber());
        contentValues.put("password", user.getPassword());
        return contentValues;
    }

    @NonNull
    private void closeCursor(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    @NonNull
    private User getUserInfoFromCursor(Cursor cursor) {
        User user = new User();
        user.setName(cursor.getString(cursor.getColumnIndex("name")));
        user.setEmailId(cursor.getString(cursor.getColumnIndex("email_id")));
        user.setPhoneNumber(cursor.getString(cursor.getColumnIndex("phone_number")));
        user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
        return user;
    }

    public User getUser(String emailId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + Constants.USERS_TABLE_NAME
                + " where " + "email_id" + " = ?", new String[]{emailId});
        if (cursor != null && cursor.moveToFirst()) {
            User user = getUserInfoFromCursor(cursor);
            closeCursor(cursor);
            return user;
        }
        closeCursor(cursor);
        return null;
    }

    public User getAuthorisedUser(String emailId, String password){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + Constants.USERS_TABLE_NAME
                + " where " + "email_id" + " = ? " +" and password = ?" , new String[]
                {emailId, password});
        if (cursor != null && cursor.moveToFirst()) {
            User authorisedUser = getUserInfoFromCursor(cursor);
            closeCursor(cursor);
            return authorisedUser;
        }
        closeCursor(cursor);
        return null;
    }
}
