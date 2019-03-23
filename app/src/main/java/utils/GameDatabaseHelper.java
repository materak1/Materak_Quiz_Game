package utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class GameDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQL";
    private static GameDatabaseHelper sInstance;

    private static final String DATABASE_NAME = "gameDb";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_GAMES = "games";
    private static final String TABLE_USERS = "users";

    // Post Table Columns
    private static final String KEY_GAME_ID = "id";
    private static final String KEY_GAME_USER_ID_FK = "userId";
    private static final String KEY_GAME_Q1 = "q1";
    private static final String KEY_GAME_Q2 = "q2";
    private static final String KEY_GAME_Q3 = "q3";
    private static final String KEY_GAME_Q4 = "q4";
    private static final String KEY_GAME_Q5 = "q5";
    private static final String KEY_GAME_SCORE = "score";

    // User Table Columns
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_FIRST_NAME = "firstName";
    private static final String KEY_USER_LAST_NAME = "lastName";
    private static final String KEY_USER_DOB = "date";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_PASSWORD = "password";

    public static synchronized GameDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new GameDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private GameDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GAMES_TABLE = "CREATE TABLE " + TABLE_GAMES +
                "(" +
                KEY_GAME_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_GAME_USER_ID_FK + " INTEGER REFERENCES " + TABLE_USERS + "," + // Define a foreign key
                KEY_GAME_Q1 + " TEXT" + "," +
                KEY_GAME_Q2 + " TEXT" + "," +
                KEY_GAME_Q3 + " TEXT" + "," +
                KEY_GAME_Q4 + " TEXT" + "," +
                KEY_GAME_Q5 + " TEXT" + "," +
                KEY_GAME_SCORE + " TEXT" +
                ")";

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY," +
                KEY_USER_FIRST_NAME + " TEXT," +
                KEY_USER_LAST_NAME + " TEXT" + "," +
                KEY_USER_DOB + " TEXT" + "," +
                KEY_USER_EMAIL + " TEXT" + "," +
                KEY_USER_PASSWORD + " TEXT" +
                ")";

        db.execSQL(CREATE_GAMES_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }
    }

    // Insert a post into the database
    public void addGame(Game game) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).

            ContentValues values = new ContentValues();
            values.put(KEY_GAME_USER_ID_FK, game.user_id);
            values.put(KEY_GAME_Q1, game.question_1);
            values.put(KEY_GAME_Q2, game.question_2);
            values.put(KEY_GAME_Q3, game.question_3);
            values.put(KEY_GAME_Q4, game.question_4);
            values.put(KEY_GAME_Q5, game.question_5);
            values.put(KEY_GAME_SCORE, game.score);

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_GAMES, null, values);
            db.setTransactionSuccessful();
            Log.d(TAG, "Successfully added game");
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    // Insert or update a user in the database
    // Since SQLite doesn't support "upsert" we need to fall back on an attempt to UPDATE (in case the
    // user already exists) optionally followed by an INSERT (in case the user does not already exist).
    // Unfortunately, there is a bug with the insertOnConflict method
    // (https://code.google.com/p/android/issues/detail?id=13045) so we need to fall back to the more
    // verbose option of querying for the user's primary key if we did an update.
    public long addOrUpdateUser(User user) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_FIRST_NAME, user.first_name);
            values.put(KEY_USER_LAST_NAME, user.family_name);
            values.put(KEY_USER_DOB, user.date_of_birth);
            values.put(KEY_USER_EMAIL, user.email);
            values.put(KEY_USER_PASSWORD, user.password);


            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_USERS, values, KEY_USER_EMAIL + "= ?", new String[]{user.email});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_USER_ID, TABLE_USERS, KEY_USER_EMAIL);
                Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(user.email)});
                try {
                    if (cursor.moveToFirst()) {
                        userId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                userId = db.insertOrThrow(TABLE_USERS, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return userId;
    }

    public User getUser(User user) {
//        List<Game> games = new ArrayList<>();
        User newUser = new User(0,"","","","","");
        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
//        String POSTS_SELECT_QUERY = "SELECT * FROM TABLE_USERS";
        String POSTS_SELECT_QUERY = String.format("SELECT * FROM %s WHERE %s = ? AND %s = ?",
                TABLE_USERS, KEY_USER_EMAIL, KEY_USER_PASSWORD);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, new String[]{user.email,user.password});
//        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    newUser.first_name = cursor.getString(cursor.getColumnIndex(KEY_USER_FIRST_NAME));
                    newUser.family_name = cursor.getString(cursor.getColumnIndex(KEY_USER_LAST_NAME));
                    newUser.date_of_birth = cursor.getString(cursor.getColumnIndex(KEY_USER_DOB));
                    newUser.email = cursor.getString(cursor.getColumnIndex(KEY_USER_EMAIL));
                    newUser.user_id = cursor.getInt(cursor.getColumnIndex(KEY_USER_ID));

//                    Game newGame = new Game();
//                    newGame.game_id = cursor.getInt(cursor.getColumnIndex(KEY_GAME_ID));
//                    newGame.question_1 = cursor.getInt(cursor.getColumnIndex(KEY_GAME_Q1));
//                    newGame.question_2 = cursor.getInt(cursor.getColumnIndex(KEY_GAME_Q2));
//                    newGame.question_3 = cursor.getInt(cursor.getColumnIndex(KEY_GAME_Q3));
//                    newGame.question_4 = cursor.getInt(cursor.getColumnIndex(KEY_GAME_Q4));
//                    newGame.question_5 = cursor.getInt(cursor.getColumnIndex(KEY_GAME_Q5));
//                    newGame.user = newUser;
//                    newUser.games.add(newGame);
                } while(cursor.moveToNext());
            } else {
                Log.d(TAG, "No records found");
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get games from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return newUser;
    }

    // Update the user's profile picture url
//    public int updateUserProfilePicture(User user) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_USER_PROFILE_PICTURE_URL, user.profilePictureUrl);
//
//        // Updating profile picture url for user with that userName
//        return db.update(TABLE_USERS, values, KEY_USER_NAME + " = ?",
//                new String[] { String.valueOf(user.userName) });
//    }

//    public List<User> getAllUsers() {
//        List<User> users = new ArrayList<>();
//
//        // SELECT * FROM POSTS
//        // LEFT OUTER JOIN USERS
//        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
//        String POSTS_SELECT_QUERY =
//                String.format("SELECT * FROM %s",
//                        TABLE_USERS);
//
//        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
//        // disk space scenarios)
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
//        try {
//            if (cursor.moveToFirst()) {
//                do {
//                    User newUser = new User(0,"","","",cursor.getString(cursor.getColumnIndex(KEY_USER_EMAIL)),cursor.getString(cursor.getColumnIndex(KEY_USER_PASSWORD)));
////                    newUser.email = cursor.getString(cursor.getColumnIndex(KEY_USER_EMAIL));
////                    newUser.profilePictureUrl = cursor.getString(cursor.getColumnIndex(KEY_USER_PROFILE_PICTURE_URL));
//
////                    Post newPost = new Post();
////                    newPost.text = cursor.getString(cursor.getColumnIndex(KEY_POST_TEXT));
////                    newPost.user = newUser;
//                    users.add(newUser);
//                } while(cursor.moveToNext());
//            } else {
//                Log.d(TAG, "No records found");
//            }
//        } catch (Exception e) {
//            Log.d(TAG, "Error while trying to get posts from database");
//        } finally {
//            if (cursor != null && !cursor.isClosed()) {
//                cursor.close();
//            }
//        }
//        return users;
//    }

    public List<Game> getUserGames(int user_id) {
        List<Game> games = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE %s = ?",
                        TABLE_GAMES, KEY_GAME_USER_ID_FK);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, new String[]{String.valueOf(user_id)});
        try {
            if (cursor.moveToFirst()) {
                do {
                    Log.d(TAG, "Found game records");
                    Game newGame = new Game(0,0,0,"","",0,0,0);
                    newGame.game_id = cursor.getInt(cursor.getColumnIndex(KEY_GAME_ID));
                    newGame.question_1 = cursor.getInt(cursor.getColumnIndex(KEY_GAME_Q1));
                    newGame.question_2 = cursor.getString(cursor.getColumnIndex(KEY_GAME_Q2));
                    newGame.question_3 = cursor.getString(cursor.getColumnIndex(KEY_GAME_Q3));
                    newGame.question_4 = cursor.getInt(cursor.getColumnIndex(KEY_GAME_Q4));
                    newGame.question_5 = cursor.getInt(cursor.getColumnIndex(KEY_GAME_Q5));
                    newGame.score = cursor.getInt(cursor.getColumnIndex(KEY_GAME_SCORE));
                    games.add(newGame);
                } while(cursor.moveToNext());
            } else {
                Log.d(TAG, "No records found");
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return games;
    }

    public List<Game> getAllGames() {
        List<Game> games = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_GAMES, KEY_USER_ID);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Log.d(TAG, "Found game records");
                    Game newGame = new Game(0,0,0,"","",0,0,0);
                    newGame.game_id = cursor.getInt(cursor.getColumnIndex(KEY_GAME_ID));
                    newGame.question_1 = cursor.getInt(cursor.getColumnIndex(KEY_GAME_Q1));
                    newGame.question_2 = cursor.getString(cursor.getColumnIndex(KEY_GAME_Q2));
                    newGame.question_3 = cursor.getString(cursor.getColumnIndex(KEY_GAME_Q3));
                    newGame.question_4 = cursor.getInt(cursor.getColumnIndex(KEY_GAME_Q4));
                    newGame.question_5 = cursor.getInt(cursor.getColumnIndex(KEY_GAME_Q5));
                    games.add(newGame);
                } while(cursor.moveToNext());
            } else {
                Log.d(TAG, "No records found");
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return games;
    }

    // Delete all posts and users in the database
    public void deleteAllPostsAndUsers() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_GAMES, null, null);
            db.delete(TABLE_USERS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }
}
