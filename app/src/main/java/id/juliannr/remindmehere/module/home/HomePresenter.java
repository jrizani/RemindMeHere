package id.juliannr.remindmehere.module.home;

import java.util.ArrayList;
import java.util.List;

import id.juliannr.remindmehere.data.controller.ReminderController;
import id.juliannr.remindmehere.data.model.Reminder;
import id.juliannr.remindmehere.module.base.BasePresenter;

/**
 * Created by juliannr on 19/04/18.
 */

public class HomePresenter extends BasePresenter<HomeView.View> implements HomeView.Presenter {

    private static HomePresenter instance;
    private ReminderController controller;

    public HomePresenter() {
        controller = ReminderController.getInstance();
    }

    public static HomePresenter getInstance(){
        if(instance == null)
            instance = new HomePresenter();
        return instance;
    }

    @Override
    public void getData() {
        List<Reminder> datas = new ArrayList<>();
        datas = controller.getAll();
        if(!datas.isEmpty()){
            getView().onDataFound(datas);
        }else{
            getView().onDataNotFound();
        }
    }

    @Override
    public void delete(Reminder data) {
        controller.delete(data);
        getView().onDeleteData();
    }
}
