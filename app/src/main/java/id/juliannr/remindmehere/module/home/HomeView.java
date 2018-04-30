package id.juliannr.remindmehere.module.home;

import java.util.List;

import id.juliannr.remindmehere.data.model.Reminder;
import id.juliannr.remindmehere.module.base.BaseView;

/**
 * Created by juliannr on 19/04/18.
 */

public interface HomeView {
    interface View extends BaseView {
        void onDataFound(List<Reminder> datas);

        void onDataNotFound();

        void onDeleteData();
    }

    interface Presenter{
        void getData();

        void delete(Reminder data);
    }
}
