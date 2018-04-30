package id.juliannr.remindmehere.module.savedlocation;

import java.util.List;
import java.util.UUID;

import id.juliannr.remindmehere.data.controller.SavedLocationController;
import id.juliannr.remindmehere.data.model.SavedLocation;
import id.juliannr.remindmehere.module.base.BasePresenter;

/**
 * Created by juliannr on 25/04/18.
 */

public class SavedLocationPresenter extends BasePresenter<SavedLocationView.View> implements
        SavedLocationView.Presenter {

    private static SavedLocationPresenter instance;
    private SavedLocationController controller = SavedLocationController.getInstance();

    public SavedLocationPresenter() {
    }

    public static SavedLocationPresenter getInstance(){
        if(instance == null)
            instance = new SavedLocationPresenter();
        return instance;
    }

    @Override
    public void loadData() {
        List<SavedLocation> data = controller.getAll();
        if(data == null || data.isEmpty())
            getView().onDataNotFound();
        else
            getView().onDataFound(data);
    }
}
