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
public class SerializableStringProperty extends SimpleStringProperty implements Serializable{

    SerializableStringProperty(String name) {
        super(name);
    }
    
}
