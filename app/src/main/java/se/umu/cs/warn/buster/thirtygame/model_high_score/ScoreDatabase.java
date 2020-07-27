package se.umu.cs.warn.buster.thirtygame.model_high_score;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Implementation of actual Room database. Implemented as a singleton. SHOULD NOT be run on multiple
 * threads but rather main one.
 *
 * @See <a href="https://developer.android.com/reference/androidx/room/Database">Database</a>
 *
 * Author:          Buster Hultgren Wärn - busterw@cs.umu.se.
 * Course:          Application in mobile development.
 * Written:         2020-07-23.
 */
@Database(entities = ScoreEntity.class, version = 1, exportSchema = false)
public abstract class ScoreDatabase extends RoomDatabase {

    private static final String databaseName = "thirty_game_database";
    private static ScoreDatabase instance;

    /**
     * Singleton getter for database.
     * @param context The context from where database is called. Should be "this".
     * @return The database.
     */
    public static synchronized ScoreDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ScoreDatabase.class, databaseName)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build();
        }
        return instance;
    }

    /**
     * Gets the ScoreDao for the database.
     * @see se.umu.cs.warn.buster.thirtygame.model_high_score.ScoreDao
     * @return The ScoreDao.
     */
    public abstract ScoreDao scoreDao();
}
