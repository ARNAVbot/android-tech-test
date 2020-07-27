package com.bridge.androidtechnicaltest.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Pupils")
public class Pupil {
    @PrimaryKey
    @ColumnInfo(name = "pupil_id")
    private Long pupilId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "country")
    private String value;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "latitude")
    private Double latitude;

    @ColumnInfo(name = "longitude")
    private Double longitude;

    //Status to indicate if this pupil needs to be deleted.
    @ColumnInfo(name = "toBeDeleted")
    private boolean toBeDeleted = false;

    public Pupil(Long pupilId, String name, String value, String image, Double latitude, Double longitude, boolean toBeDeleted) {
        this.pupilId = pupilId;
        this.name = name;
        this.value = value;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.toBeDeleted = toBeDeleted;
    }

    public Long getPupilId() {
        return pupilId;
    }

    public void setPupilId(Long pupilId) {
        this.pupilId = pupilId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public boolean isToBeDeleted() {
        return toBeDeleted;
    }

    public void setToBeDeleted(boolean toBeDeleted) {
        this.toBeDeleted = toBeDeleted;
    }

    @Override
    public String toString() {
        return "Pupil{" +
                "pupilId=" + pupilId +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", image='" + image + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", toBeDeleted=" + toBeDeleted +
                '}';
    }
}