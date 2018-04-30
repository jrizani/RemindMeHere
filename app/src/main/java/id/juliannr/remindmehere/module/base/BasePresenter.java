package id.juliannr.remindmehere.module.base;

/**
 * Created by juliannr on 19/04/18.
 */

public class BasePresenter<T extends BaseView> {
    private T view;

    public T getView() {
        return view;
    }

    public void setView(T view) {
        this.view = view;
    }
}
