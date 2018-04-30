package id.juliannr.remindmehere.data.controller;

import java.util.List;

import id.juliannr.remindmehere.data.model.SavedLocation;
import io.realm.Realm;

/**
 * Created by juliannr on 24/04/18.
 */

public class SavedLocationController implements BaseController<SavedLocation> {
    private Realm realm = Realm.getDefaultInstance();
    private static SavedLocationController instance;

    public SavedLocationController() {
    }

    public static SavedLocationController getInstance(){
        if(instance == null)
            instance = new SavedLocationController();
        return instance;
    }

    @Override
    public void insert(final SavedLocation data) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insert(data);
            }
        });
    }

    @Override
    public void update(final SavedLocation data) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SavedLocation existing = realm.where(SavedLocation.class).equalTo("id", data
                        .getId()).findFirst();
                existing.deleteFromRealm();
                realm.insert(data);
            }
        });
    }

    @Override
    public void delete(final SavedLocation data) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SavedLocation existing = realm.where(SavedLocation.class).equalTo("id", data
                        .getId()).findFirst();
                existing.deleteFromRealm();
            }
        });
    }

    @Override
    public SavedLocation getOne(Object key) {
        return realm.where(SavedLocation.class).equalTo("id", ((String) key)).findFirst();
    }

    @Override
    public List<SavedLocation> getAll() {
        return realm.where(SavedLocation.class).findAll();
    }

    @Override
    public List<SavedLocation> getByKey(String key, Object value) {
        return realm.where(SavedLocation.class).equalTo(key, ((String) value)).findAll();
    }
}
