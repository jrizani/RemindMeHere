package id.juliannr.remindmehere.module.savedlocation;

import java.util.List;

import id.juliannr.remindmehere.data.model.SavedLocation;
import id.juliannr.remindmehere.module.base.BaseView;

/**
 * Created by juliannr on 25/04/18.
 */

public interface SavedLocationView {
    interface View extends BaseView{
        void onDataFound(List<SavedLocation> data);

        void onDataNotFound();
    }

    interface Presenter{
        void loadData();
    }

    interface AddView extends BaseView{
        void onSucceedSave();

        void onFailedSave(String message);
    }

    interface AddPresenter{
        void addData(String locationName, double latitude, double longitude);
    }
}
