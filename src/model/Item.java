/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.io.Serializable;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Hendrick Ducasse
 */
public class Item implements Serializable{
    
    private SerializableStringProperty name;
    private SerializableStringProperty description;
    private String notes;
    
    public Item(String name, String description){
        this.name = new SerializableStringProperty(name);
        this.description = new SerializableStringProperty(description); 
    }
    
    public String getName(){
        return name.get();
    }
    
     public void setName(String name) {
        this.name.set(name);
    }
    
    public String getDescription(){
        return description.get();
    }

    public void setDescription(String description) {
        if(description == null){
            description = "";
        }
        this.description.set(description);
    }
    
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        if(notes == null){
            notes = "";
        }
        this.notes = notes;
    }
    
    public String toString(){
        return getName(); 
    }
    
}
