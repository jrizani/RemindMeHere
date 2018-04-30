package id.juliannr.remindmehere.data.controller;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by juliannr on 19/04/18.
 */

public interface BaseController<T extends RealmObject> {
    public void insert(T data);

    public void update(T data);

    public void delete(T data);

    public T getOne(Object key);

    public List<T> getAll();

    public List<T> getByKey(String key, Object value);
}
