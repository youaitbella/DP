package org.inek.dataportal.helper.scope;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.inject.Qualifier;
import javax.inject.Scope;

/**
 *
 * @author muellermi
 */
@Qualifier
@Target(value = {METHOD, TYPE, FIELD, PARAMETER})
@Retention(value = RUNTIME)
@Scope
public @interface FeatureScoped {
}
