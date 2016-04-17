package anshul.org.quintypetask.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import anshul.org.quintypetask.Model.MovieList;


public class SqliteHelper extends SQLiteOpenHelper {

    private static final String LOG = "SqliteHelper";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "quintype";

    private static final String TABLE_FAVOURITE = "history";

    private static final String KEY_ID = "id";

    private static final String KEY_MOVIE_TITLE = "title";
    private static final String KEY_MOVIE_POSTER = "poster";
    private static final String KEY_MOVIE_YEAR = "year";
    private static final String KEY_MOVIE_TYPE = "type";


    private static final String CREATE_TABLE_BOOKMARKS = "CREATE TABLE "
            + TABLE_FAVOURITE + "(" + KEY_ID + " TEXT PRIMARY KEY,"
            + KEY_MOVIE_TITLE + " TEXT," + KEY_MOVIE_POSTER + " TEXT,"
            + KEY_MOVIE_YEAR + " TEXT," + " TEXT ," + KEY_MOVIE_TYPE + " TEXT" + ")";


    private static SqliteHelper sInstance;


    public SqliteHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }

    public static SqliteHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new SqliteHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_BOOKMARKS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITE);
        onCreate(db);
    }


    public boolean saveSearch(MovieList.SearchEntity model) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        values = new ContentValues();

        values.put(KEY_ID, model.getImdbID());
        values.put(KEY_MOVIE_TITLE, model.getTitle());
        values.put(KEY_MOVIE_POSTER, model.getPoster());

        values.put(KEY_MOVIE_YEAR, model.getYear());
        values.put(KEY_MOVIE_TYPE, model.getType());


        try {
            long rowInserted = db.insert(TABLE_FAVOURITE, null, values);
            db.close();
            return rowInserted != -1;

        } catch (SQLException e) {
            db.close();
            return false;
        }
    }


    public ArrayList<MovieList.SearchEntity> getSearch() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<MovieList.SearchEntity> addressList = new ArrayList<>();

        String query = "select *  from " + TABLE_FAVOURITE;
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MovieList.SearchEntity listData = new MovieList.SearchEntity();
                listData.setTitle(cursor.getString(cursor.getColumnIndex(KEY_MOVIE_TITLE)));
                listData.setImdbID(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                listData.setPoster(cursor.getString(cursor.getColumnIndex(KEY_MOVIE_POSTER)));
                listData.setYear(cursor.getString(cursor.getColumnIndex(KEY_MOVIE_YEAR)));
                listData.setType(cursor.getString(cursor.getColumnIndex(KEY_MOVIE_TYPE)));

                addressList.add(listData);


            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return addressList;

    }


}

