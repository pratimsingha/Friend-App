package com.example.pratim.friends;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.CancellationSignal;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by pratim on 08-01-2017.
 */
public class FriendsProvider extends ContentProvider{
    private FriendsDatabase mOpenHelper;

    private static String TAG = FriendsProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int FRIENDS = 100;
    private static final int FRIENDS_ID = 101;

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FriendsContract.CONTENT_AUTHORITY;
        matcher.addURI(authority,"friends",FRIENDS);
        matcher.addURI(authority,"friends/*",FRIENDS_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new FriendsDatabase(getContext());
        return true;
    }

    private void deleteDatabase(){
        mOpenHelper.close();
        FriendsDatabase.deleteDatabase(getContext());
        mOpenHelper = new FriendsDatabase(getContext());
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FRIENDS:
                return FriendsContract.Friends.CONTENT_TYPE;
            case FRIENDS_ID:
                return FriendsContract.Friends.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Log.v(TAG, "insert(uri=" + uri + ", values=" + contentValues.toString());
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match){
            case FRIENDS:
                long recordId = db.insertOrThrow(FriendsDatabase.Tables.FRIENDS,null,contentValues);
                return FriendsContract.Friends.buildFriendUri(String.valueOf(recordId));
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(FriendsDatabase.Tables.FRIENDS);

        switch (match){
            case FRIENDS:
                //do nothing
                break;
            case FRIENDS_ID:
                String id = FriendsContract.Friends.getFriendId(uri);
                queryBuilder.appendWhere(BaseColumns._ID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        Log.v(TAG, "update(uri=" + uri + ", values=" + contentValues.toString());
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);

        String selectionCriteria = s;
        switch (match){
            case FRIENDS:
                //do nothing
                break;
            case FRIENDS_ID:
                String id = FriendsContract.Friends.getFriendId(uri);
                selectionCriteria = BaseColumns._ID + "=" + id + (!TextUtils.isEmpty(s) ? " AND (" + s + ")" : "");
                break;

            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
        return db.update(FriendsDatabase.Tables.FRIENDS,contentValues,selectionCriteria,strings);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        Log.v(TAG, "delete(uri=" + uri);

        if(uri.equals(FriendsContract.BASE_CONTENT_URI)){
            deleteDatabase();
            return 0;
        }
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match){
            case FRIENDS:
                //do nothing
                break;
            case FRIENDS_ID:
                String id = FriendsContract.Friends.getFriendId(uri);
                String selectionCriteria = BaseColumns._ID + "=" + id + (!TextUtils.isEmpty(s) ? " AND (" + s + ")" : "");
                return db.delete(FriendsDatabase.Tables.FRIENDS, selectionCriteria, strings);


            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }
}
