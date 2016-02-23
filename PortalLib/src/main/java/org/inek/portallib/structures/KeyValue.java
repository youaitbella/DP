package org.inek.portallib.structures;

import java.util.Objects;

/**
 *
 * @author muellermi
 * @param <K>
 * @param <V>
 */
public class KeyValue<K, V> {
    private final K _key;
    private final V _value;

    public KeyValue(final K key, final V value) {
        _key = key;
        _value = value;
    }

    public K getKey() {
        return _key;
    }

    public V getValue() {
        return _value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(_key);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KeyValue<?, ?> other = (KeyValue<?, ?>) obj;
        return Objects.equals(_key, other._key);
    }

}
