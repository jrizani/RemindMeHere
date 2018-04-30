package id.juliannr.remindmehere.data.controller;

import java.util.List;

import id.juliannr.remindmehere.data.model.Reminder;
import io.realm.Realm;

/**
 * Created by juliannr on 19/04/18.
 */

public class ReminderController implements BaseController<Reminder> {
    private Realm realm = Realm.getDefaultInstance();
    private static ReminderController instance;

    public ReminderController() {
    }

    public static ReminderController getInstance() {
        if(instance == null)
            instance = new ReminderController();
        return instance;
    }

    @Override
    public void insert(final Reminder data) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insert(data);
            }
        });
    }

    @Override
    public void update(final Reminder data) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Reminder existing = realm.where(Reminder.class).equalTo("id", data.getId())
                        .findFirst();
                existing.deleteFromRealm();
                realm.insert(data);
            }
        });
    }

    @Override
    public void delete(final Reminder data) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Reminder existing = realm.where(Reminder.class).equalTo("id", data.getId())
                        .findFirst();
                existing.deleteFromRealm();
            }
        });
    }

    @Override
    public Reminder getOne(Object key) {
        return realm.where(Reminder.class).equalTo("id", (String)key).findFirst();
    }

    @Override
    public List<Reminder> getAll() {
        return realm.where(Reminder.class).findAll();
    }

    @Override
    public List<Reminder> getByKey(String key, Object value) {
        return realm.where(Reminder.class).equalTo(key, (String)value).findAll();
    }
}
