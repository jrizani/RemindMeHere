package id.juliannr.remindmehere.module.savedlocation;

import java.util.UUID;

import id.juliannr.remindmehere.data.controller.SavedLocationController;
import id.juliannr.remindmehere.data.model.SavedLocation;
import id.juliannr.remindmehere.module.base.BasePresenter;

/**
 * Created by juliannr on 25/04/18.
 */

public class AddSavedLocationPresenter extends BasePresenter<SavedLocationView.AddView>
        implements SavedLocationView.AddPresenter {

    private static AddSavedLocationPresenter instance;
    private SavedLocationController controller = SavedLocationController.getInstance();

    public AddSavedLocationPresenter() {
    }

    public static AddSavedLocationPresenter getInstance() {
        if (instance == null)
            instance = new AddSavedLocationPresenter();
        return instance;
    }

    @Override
    public void addData(String locationName, double latitude, double longitude) {
        String id = UUID.randomUUID().toString();
        SavedLocation data = new SavedLocation(
                id,
                locationName,
                latitude,
                longitude
        );
        try{
            controller.insert(data);
            getView().onSucceedSave();
        }catch (Exception e){
            getView().onFailedSave(e.getMessage());
        }

    }
}
