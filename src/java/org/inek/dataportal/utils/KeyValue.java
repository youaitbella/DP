package org.inek.dataportal.utils;

/**
 *
 * @author muellermi
 */
public class KeyValue<K, V> {
    private final K _key;
    private final V _value;

    // this constructor is needed to deploy onto GF 3.1
    // todo: remove as soon as possible
    public KeyValue() {
        _key = null;
        _value = null;
    }
    
    public KeyValue(final K key, final V value) {
        _key = key;
        _value = value;
    }

    public K getKey() {
        return _key;
    }

    /**
     * This method wont set anything. It is needed, because JSF can't deal with
     * getters only :(
     *
     * @param dummy
     */
    public void setKey(K dummy) {
    }

    public V getValue() {
        return _value;
    }

    public void setValue(V dummy) {
    }
}
