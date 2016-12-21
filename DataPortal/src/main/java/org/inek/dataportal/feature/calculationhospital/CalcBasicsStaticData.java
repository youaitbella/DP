/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;

/**
 * This class is instantiated at application level.
 * Thus, it acts as a kind of Singleton.
 * It provides the form with data which is independend from a concrete session,
 * e.g. list items
 * 
 * @author muellermi
 */
@Named
@ApplicationScoped
public class CalcBasicsStaticData {
   public List<SelectItem> getFulfillmentItems(){
       // todo (low priority): get text from property file
       List<SelectItem> items = new ArrayList<>();
       items.add(new SelectItem(-1, "nicht erfüllt"));
       items.add(new SelectItem(0, "ganzjährig"));
       items.add(new SelectItem(1, "ab Januar"));
       items.add(new SelectItem(2, "ab Februar"));
       items.add(new SelectItem(3, "ab März"));
       items.add(new SelectItem(4, "ab April"));
       items.add(new SelectItem(5, "ab Mai"));
       items.add(new SelectItem(6, "ab Juni"));
       items.add(new SelectItem(7, "ab Juli"));
       items.add(new SelectItem(8, "ab August"));
       items.add(new SelectItem(9, "ab September"));
       items.add(new SelectItem(10, "ab Oktober"));
       items.add(new SelectItem(11, "ab November"));
       items.add(new SelectItem(12, "ab Dezember"));
       return items;
   } 
}
