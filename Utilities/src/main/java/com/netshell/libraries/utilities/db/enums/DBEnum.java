package com.netshell.libraries.utilities.db.enums;

import com.netshell.libraries.utilities.filter.Filter;
import com.netshell.library.singleton.SingletonManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Abhishek
 *         Created on 12/13/2015.
 */
public final class DBEnum<I, O, T extends Initializable<I, O>> {

    private Map<O, T> tMap = new HashMap<>();

    private Class<T> tClass;

    private DBEnum(Class<T> tClass, DataSource<I> dataSource) {
        this.tClass = tClass;
        this.initialize(dataSource);
    }

    public static <TEntity, TEnumId, TEnum extends Initializable<TEntity, TEnumId>> void initialize(Class<TEnum> tEnumClass, DataSource<TEntity> dataSource) {
        SingletonManager.registerSingleton(tEnumClass, new DBEnum<>(tEnumClass, dataSource));
    }

    public static <TEntity, TEnumId, TEnum extends Initializable<TEntity, TEnumId>> TEnum findById(Class<TEnum> tEnumClass, TEnumId id) {
        return DBEnum.getDBEnumObject(tEnumClass).findById(id);
    }

    public static <TEntity, TEnumId, TEnum extends Initializable<TEntity, TEnumId>> TEnum findByIdOrDefault(Class<TEnum> tEnumClass, TEnumId id, TEnum tDefault) {
        return DBEnum.getDBEnumObject(tEnumClass).findByIdOrDefault(id, tDefault);
    }

    public static <TEntity, TEnumId, TEnum extends Initializable<TEntity, TEnumId>> TEnum getById(Class<TEnum> tEnumClass, TEnumId id) {
        return DBEnum.getDBEnumObject(tEnumClass).getById(id);
    }

    public static <TEntity, TEnumId, TEnum extends Initializable<TEntity, TEnumId>> Collection<TEnum> filter(Class<TEnum> tEnumClass, TEnum tEnum) {
        return DBEnum.getDBEnumObject(tEnumClass).filter(tEnum);
    }

    @SuppressWarnings("unchecked")
    private static <TEntity, TEnumId, TEnum extends Initializable<TEntity, TEnumId>> DBEnum<TEntity, TEnumId, TEnum> getDBEnumObject(Class<TEnum> tEnumClass) {
        return SingletonManager.getSingleton(tEnumClass, DBEnum.class, null);
    }

    private void initialize(DataSource<I> dataSource) {
        dataSource.activate();
        try {
            int i = 0;

            while (dataSource.hasNext()) {
                T t = tClass.newInstance();
                t.initialize(i++, dataSource.next());
                tMap.put(t.getId(), t);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new DBEnumException("Exception occurred", e);
        } finally {
            dataSource.close();
        }
    }

    public T findById(O id) {
        return tMap.get(id);
    }

    public T findByIdOrDefault(O id, T tDefault) {
        return tMap.getOrDefault(id, tDefault);
    }

    public T getById(O id) {
        if (!tMap.containsKey(id)) {
            throw new DBEnumException(id.toString());
        }

        return tMap.get(id);
    }

    public Collection<T> filter(T filterCriteria) {
        return Filter.filter(this.tMap.values(), filterCriteria);
    }
}
