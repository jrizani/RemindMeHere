package id.juliannr.remindmehere.module.addreminder;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import id.juliannr.remindmehere.data.model.Reminder;
import id.juliannr.remindmehere.data.model.SavedLocation;
import id.juliannr.remindmehere.module.base.BaseView;

/**
 * Created by juliannr on 19/04/18.
 */

public interface AddReminderView {
    interface View extends BaseView{
        void saveSuccess();

        void saveFailed(String message);
    }

    interface Presenter{
        void save(Reminder data);

        int checkSavedLocation();

        List<SavedLocation> getSavedLocation();

        LatLng getSavedLocationLatLong(String locationName);
    }
}
