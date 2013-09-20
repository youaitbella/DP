/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.utils;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 * This annotation is intended for documentation of current field values Any
 * method documenting the values of a given object should deliver name / value
 * pairs with name = name of this annotation or if empty: name of the field and
 * value = current value of the field rank is used to sort the output (within
 * the same rank: order of fields in the class)
 *
 * @author muellermi
 */
@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface Documentation {

    public String name() default "";

    /**
     * The key to a message bundle
     *
     * @return
     */
    public String key() default "";

    /**
     * relative order
     *
     * @return
     */
    public int rank() default 100;

    /**
     * omit documentation if value is empty
     */
    public boolean omitOnEmpty() default false;
}
