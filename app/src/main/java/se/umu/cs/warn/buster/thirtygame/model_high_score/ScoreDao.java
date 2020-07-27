package se.umu.cs.warn.buster.thirtygame.model_high_score;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * Implementation of DAO in room database. Each method is a simple QLite call to the Room database.
 *
 * @See <a href="https://developer.android.com/training/data-storage/room/accessing-data">Accessing
 * data using Room DAOs</a>
 *
 * Author:          Buster Hultgren Wärn - busterw@cs.umu.se.
 * Course:          Application in mobile development.
 * Written:         2020-07-23.
 */
@Dao
public interface ScoreDao {

    /**
     * Get all scores ordered in descending order from database.
     * @return The scores.
     */
    @Query("SELECT * FROM high_scores ORDER BY score DESC")
    List<ScoreEntity> getScores();

    /**
     * Insert a score into the database.
     * @param score The score.
     */
    @Insert
    void insert(ScoreEntity score);

    /**
     * Delete a score from the database.
     * @param score The score.
     */
    @Delete
    void delete(ScoreEntity score);
}
