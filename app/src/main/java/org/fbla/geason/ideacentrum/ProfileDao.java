package org.fbla.geason.ideacentrum;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProfileDao {

    @Query("SELECT * FROM profile")
    List<Profile> getAllProfiles();

    @Query("SELECT * FROM profile WHERE profileId = :id")
    Profile getProfile(int id);

    @Insert
    long newProfile(Profile profile);

    @Query("UPDATE profile SET image_file = :image, first_name = :first, last_name = :last, email = :email, phone = :phone, about = :about, education = :ed, experience = :exp WHERE profileId = :id")
    void updateProfile(String image, String first, String last, String email, String phone, String about, String ed, String exp, int id);
}