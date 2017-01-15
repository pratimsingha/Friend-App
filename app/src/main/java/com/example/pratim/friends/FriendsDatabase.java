package com.example.pratim.friends;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by pratim on 08-01-2017.
 */
public class FriendsDatabase extends SQLiteOpenHelper{
    private static final String TAG = FriendsDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "friends.db";
    private static final int DATABASE_VERSION = 2;
    private final Context mContext;

    interface Tables{
        String FRIENDS = "friends";
    }

    public FriendsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + Tables.FRIENDS + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + FriendsContract.FriendsColumns.FRIENDS_NAME + " TEXT NOT NULL,"
            + FriendsContract.FriendsColumns.FRIENDS_EMAIL + " TEXT NOT NULL"
            + FriendsContract.FriendsColumns.FRIENDS_PHONE + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        int version = i;
        if(version == 1){
            //Add some extra fields to the database without deleting existing data
            version = 2;
        }
        if(version != DATABASE_VERSION){
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Tables.FRIENDS);
            onCreate(sqLiteDatabase);
        }
    }

    public static void deleteDatabase(Context context){
        context.deleteDatabase(DATABASE_NAME);
    }
}
