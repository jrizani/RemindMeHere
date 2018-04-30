package id.juliannr.remindmehere.module.addreminder;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import id.juliannr.remindmehere.data.controller.ReminderController;
import id.juliannr.remindmehere.data.controller.SavedLocationController;
import id.juliannr.remindmehere.data.model.Reminder;
import id.juliannr.remindmehere.data.model.SavedLocation;
import id.juliannr.remindmehere.module.base.BasePresenter;

/**
 * Created by juliannr on 19/04/18.
 */

public class AddReminderPresenter extends BasePresenter<AddReminderView.View> implements
        AddReminderView.Presenter {
    private static AddReminderPresenter instance;
    private ReminderController          reminderController;
    private SavedLocationController     savedLocationController;

    public AddReminderPresenter() {
        reminderController = ReminderController.getInstance();
        savedLocationController = SavedLocationController.getInstance();
    }

    public static AddReminderPresenter getInstance() {
        if(instance == null){
            instance = new AddReminderPresenter();
        }
        return instance;
    }


    @Override
    public void save(Reminder data) {
        try{
            reminderController.insert(data);
            getView().saveSuccess();
        }catch (Exception e){
            getView().saveFailed(e.getMessage());
        }
    }

    @Override
    public int checkSavedLocation() {
        List<SavedLocation> data = savedLocationController.getAll();
        if (data == null) return 0;
        return data.size();
    }

    @Override
    public List<SavedLocation> getSavedLocation() {
        List<SavedLocation> data = savedLocationController.getAll();
        return data;
    }

    @Override
    public LatLng getSavedLocationLatLong(String locationName) {
        SavedLocation data = savedLocationController.getByKey("locationName", locationName).get(0);
        return new LatLng(data.getLatitude(), data.getLongitude());
    }
}
