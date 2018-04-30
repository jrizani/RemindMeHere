package id.juliannr.remindmehere.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by juliannr on 19/04/18.
 */

public class Reminder extends RealmObject implements Parcelable {
    private String id;
    private String locationName;
    private String latitude;
    private String longitude;
    private String description;
    private String dueDate;

    public Reminder() {
    }

    public Reminder(String id, String locationName, String latitude, String longitude, String description, String dueDate) {
        this.id = id;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.dueDate = dueDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.locationName);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.description);
        dest.writeString(this.dueDate);
    }

    protected Reminder(Parcel in) {
        this.id = in.readString();
        this.locationName = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.description = in.readString();
        this.dueDate = in.readString();
    }

    public static final Creator<Reminder> CREATOR = new Creator<Reminder>() {
        @Override
        public Reminder createFromParcel(Parcel source) {
            return new Reminder(source);
        }

        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };
}
