package org.fbla.geason.ideacentrum;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CentroidDao {
    @Query("SELECT * FROM centroid WHERE centroidId = :id")
    Centroid getCentroid(int id);

    @Query("SELECT * FROM centroid")
    List<Centroid> getAllCentroids();

    @Query("SELECT * FROM centroid WHERE profileId = :id")
    List<Centroid> getCentroids(int id);

    @Insert
    void insertCentroid(Centroid insertedCentroid);

    @Delete
    void deleteCentroid(Centroid deletedCentroid);
}
