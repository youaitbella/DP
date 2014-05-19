package org.inek.dataportal;


import java.io.Serializable;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author muellermi
 */
@Named
@ConversationScoped
public class MyBean implements Serializable{
    @Size(min=5, max=10)
    private String firstName;
    private String lastName;
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    @Pattern(regexp = "\\d*", message = "only digits")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
}
