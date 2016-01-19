/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inek.begleitforschung;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author vohldo
 */
@Named
@RequestScoped
public class MenuItem {
    
    private String name;
    private String url;
    private boolean visible = true;
    private boolean active = true;
    private List<MenuItem> subItems;
    
    public MenuItem() {
        this("", "");
    }
    
    public MenuItem(String name, String url) {
        this.name = name;
        this.url = url;
        subItems = new ArrayList<>();
    }
    
    public String getName() {
        return name;
    }

    public boolean isVisible() {
        return visible;
    }

    public List<MenuItem> getSubItems() {
        return subItems;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setSubItems(List<MenuItem> subItems) {
        this.subItems = subItems;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
}
