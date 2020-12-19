package org.fbla.geason.ideacentrum;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "centroid")
public class Centroid {

    @PrimaryKey(autoGenerate = true)
    public int centroidId;

    @ColumnInfo
    public int profileId;

    @ColumnInfo(name="title")
    public String title;

    @ColumnInfo(name="image")
    public String fileUri;

    @ColumnInfo(name="problem")
    public String problem;

    @ColumnInfo(name="idea")
    public String idea;

    @ColumnInfo(name="need")
    public String need;

    // Create Centroid object
    public Centroid(int profileId, String title, String fileUri, String problem, String idea, String need) {
        this.profileId = profileId; this.title = title; this.fileUri = fileUri; this.problem = problem; this.idea = idea; this.need = need;
    }
}
