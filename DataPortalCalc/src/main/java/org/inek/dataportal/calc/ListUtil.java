package org.inek.dataportal.calc;

import java.util.List;
import java.util.function.BiFunction;

/**
 *
 * @author kunkelan
 */
public class ListUtil {

    /**
     * Search for an item with the same Key of Value and List item. Returns the found value or null if no Element with the
     * same Key exists. The keyInfo is defined in the function.
     *
     * @param <T> Type of Element
     * @param items the list to search in
     * @param value with the key info to search for
     * @param hasSameKey function which returns true if the key info of value and item are the same
     * @return the element found or null if there is no element with the wanted key.
     */
    public static <T> T findItem(List<T> items, T value, BiFunction<T, T, Boolean> hasSameKey) {
        for (T item : items) {
            if (hasSameKey.apply(value, item)) {
                return item;
            }
        }
        return null;
    }
}
