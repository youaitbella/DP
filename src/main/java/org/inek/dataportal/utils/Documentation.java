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

    /**
     * Replaces the field or method name by this literal name
     * @return 
     */
    public String name() default "";

    /**
     * Replaces the field or method name 
     * by an entry of the message bundle identified by this key
     *
     * @return
     */
    public String key() default "";

    /**
     * Translates values which are given as a list of pairs, separated by semicolon:
     * original=translation[;original=translation]...
     * Values which aren't in the list will be yielded without translation
     * The translation will be treated a key to the message bundle 
     * If no such key is fount, then the translation is literaly used.
     * @return
     */
    public String translateValue() default "";

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
    
    /**
     * always omit documentation 
     * no documentation is generated, but a field translation
     */
    public boolean omitAlways() default false;
    
    /**
     *
     * @return
     */
    public String dateFormat() default "dd.MM.yyyy HH:mm";
    
    /**
     *
     * @return
     */
    public boolean isMoneyFormat() default false;
}
