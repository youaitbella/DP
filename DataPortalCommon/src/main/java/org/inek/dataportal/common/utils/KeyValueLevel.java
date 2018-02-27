package org.inek.dataportal.common.utils;

import java.util.Objects;

/**
 *
 * @author muellermi
 * @param <K>
 * @param <V>
 */
public class KeyValueLevel<K, V> {
    private final K _key;
    private final V _value;
    private final int _level;

    public KeyValueLevel(final K key, final V value, int level) {
        _key = key;
        _value = value;
        _level = level;
    }

    public K getKey() {
        return _key;
    }

    public V getValue() {
        return _value;
    }

    public int getLevel(){
        return _level;
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
