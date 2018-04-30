package id.juliannr.remindmehere.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by juliannr on 23/04/18.
 */

public class SavedLocation extends RealmObject implements Parcelable {
    @PrimaryKey
    @NonNull
    private String id;
    private String locationName;
    private double latitude;
    private double longitude;

    public SavedLocation(@NonNull String id, String locationName, double latitude, double longitude) {
        this.id = id;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public SavedLocation() {
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.locationName);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    protected SavedLocation(Parcel in) {
        this.id = in.readString();
        this.locationName = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Creator<SavedLocation> CREATOR = new Creator<SavedLocation>() {
        @Override
        public SavedLocation createFromParcel(Parcel source) {
            return new SavedLocation(source);
        }

        @Override
        public SavedLocation[] newArray(int size) {
            return new SavedLocation[size];
        }
    };
}
