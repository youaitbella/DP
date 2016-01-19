package org.inek.dataportal.utils;

import java.util.Objects;

/**
 *
 * @author muellermi
 */
public class KeyValueLevel<K, V> {
    private final K _key;
    private final V _value;
    private final int _level;

    // this constructor is needed to deploy onto GF 3.1
    // todo: remove as soon as possible
    public KeyValueLevel() {
        _key = null;
        _value = null;
        _level = 0;
    }
    
    public KeyValueLevel(final K key, final V value, int level) {
        _key = key;
        _value = value;
        _level = level;
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

    public int getLevel(){
        return _level;
    }
    
    public void setLevel(int dummy) {
    }

    @Override
    public boolean equals(Object object){
        if (!(object instanceof KeyValueLevel)) {
            return false;
        }
        KeyValueLevel other = (KeyValueLevel) object;
       return _key.equals(other._key) 
               && _value.equals(other._value)
               && _level == other._level;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this._key);
        hash = 71 * hash + Objects.hashCode(this._value);
        hash = 71 * hash + this._level;
        return hash;
    }
}
