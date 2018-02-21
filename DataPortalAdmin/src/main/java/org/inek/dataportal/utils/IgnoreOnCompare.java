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
 * This annotation is intended to indicate an ignore on comparing objects
 *
 * @author muellermi
 */
@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface IgnoreOnCompare {

}
