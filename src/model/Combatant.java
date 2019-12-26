package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Combatant{

    private SimpleStringProperty name;
    private SimpleIntegerProperty maxHealth;
    private int currentHealth;
    private SimpleIntegerProperty initiative;

    public Combatant(String name, int health, int initiative){
        this.name = new SimpleStringProperty(name);
        this.maxHealth = new SimpleIntegerProperty(health);
        this.currentHealth = health;
        this.initiative = new SimpleIntegerProperty(initiative);
    }

    public Combatant(Combatant original){
        this.name = original.name;
        this.maxHealth = original.maxHealth;
        this.currentHealth = original.currentHealth;
        this.initiative = original.initiative;

    }

    public String getName(){
        return name.get();
    }

    public void setName(String name){
        this.name.set(name);
    }

    public int getMaxHealth(){
        return maxHealth.get();
    }

    public int getInitiative(){
        return initiative.get();
    }

    public void setInitiative(int initiative){
        this.initiative.set(initiative);
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }
}
