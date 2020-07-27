package com.bridge.androidtechnicaltest.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface PupilDao {

    @Query("SELECT * FROM Pupils LIMIT :startIndex,5")
    LiveData<List<Pupil>> getPupils(int startIndex);

    @Query("SELECT * FROM Pupils where toBeDeleted = :tobeDeleted")
    List<Pupil> getPupilsToBeDeleted(boolean tobeDeleted);

    @Query("SELECT * FROM Pupils ")
    List<Pupil> getAllPupils();

    @Query("SELECT * FROM Pupils where pupil_id = :pupilId")
    LiveData<Pupil> getPupilById(Long pupilId);

    @Query("SELECT * FROM Pupils where pupil_id = :pupilId")
    Pupil getSimplePupilById(Long pupilId);

    @Query("DELETE FROM Pupils ")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(
            Pupil... pupils);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(
            Pupil pupil);

    @Query("DELETE FROM PUPILS where pupil_id = :pupilId")
    void deletePupil(Long pupilId);
}
