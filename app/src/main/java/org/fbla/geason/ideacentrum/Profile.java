package org.fbla.geason.ideacentrum;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "profile")
public class Profile {
    @PrimaryKey(autoGenerate = true)
    public int profileId;

    @ColumnInfo(name = "image_file")
    public String imageUri;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "phone")
    public String phone;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "about")
    public String about;

    @ColumnInfo(name = "education")
    public String education;

    @ColumnInfo(name = "experience")
    public String experience;

    // Create Profile object
    public Profile (String imageUri, String firstName, String lastName, String email, String phone, String password, String about, String education, String experience) {
        this.imageUri = imageUri; this.firstName = firstName; this.lastName = lastName; this.email = email; this.phone = phone;
        this.password = password; this.about = about; this.education = education; this.experience = experience;
    }
}
