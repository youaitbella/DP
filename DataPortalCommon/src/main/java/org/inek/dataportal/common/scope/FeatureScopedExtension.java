/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.scope;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

public class FeatureScopedExtension implements Extension {

//    public void afterBeanDiscovery(@Observes AfterBeanDiscovery event, BeanManager manager) {
//        event.addContext(new FeatureScopedContext());
//    }
//
    public void addScope(@Observes BeforeBeanDiscovery event) {
        event.addScope(FeatureScoped.class, true, false);
    }

    public void registerContext(@Observes AfterBeanDiscovery event) {
        event.addContext(new FeatureScopedContext());
    }

}
