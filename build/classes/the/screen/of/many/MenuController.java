/*
 * To change this license header, choose License Headers in Category Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package the.screen.of.many;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.Item;

import javax.sound.sampled.*;


/**
 *
 * @author Hendrick Ducasse
 */
public class MenuController implements Initializable {
    
    @FXML
    private Button newItem;
    @FXML
    private Button deleteItem;
    @FXML
    private Button saveItem;
    
    @FXML
    private Label notesLabel;

    @FXML
    private Label musicFile;

    @FXML
    private Label musicUrl;
    
    @FXML
    private ListView<String> categoriesList;
    
    @FXML
    private TableView<Item> itemsTable; 
    
    @FXML
    private TableColumn<Item, String> nameColumn;
    
    @FXML
    private TableColumn<Item, String> descriptionColumn; 
  
    @FXML
    TextArea notesValue;
    
    private ObservableList<Item> itemsList;
    private ObservableList<Item> locationsList;
    private ObservableList<Item> peopleList;
    private ObservableList<Item> miscList;
    
    private ListListener listListener;
    private TableListener tableListener;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        initTable(); 
        initList();
        NotesListener notesListener = new NotesListener();
        notesValue.textProperty().addListener(notesListener);
        try {
            loadItems();
        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void initTable(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(
            (CellEditEvent<Item, String> t) -> {
                ((Item) t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
        });
        
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setOnEditCommit(
            (CellEditEvent<Item, String> t) -> {
                ((Item) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDescription(t.getNewValue());
        });
        
        tableListener = new TableListener();
        itemsTable.getSelectionModel().selectedItemProperty().addListener(tableListener);
    }
    
    public class ListListener implements ChangeListener{

    @Override
    public void changed(ObservableValue ov, Object t, Object t1) {      
        //Write the new table values
        switch((String)t1){
            case("Items"):
                System.out.println("Switched to Items");
                itemsTable.setItems(itemsList);
                break;
            case("Locations"):
                System.out.println("Switched to Locations");
                itemsTable.setItems(locationsList);
                break;
            case("People"):
                System.out.println("Switched to People");
                itemsTable.setItems(peopleList);
                break;
            case("Miscellaneous"):
                System.out.println("Switched to Misc.");
                itemsTable.setItems(miscList); 
                break;
            default:
                System.out.println("Something's gone horribly wrong");
                break; 
        }
    }
    
}
    
    public class NotesListener implements ChangeListener{

        @Override
        public void changed(ObservableValue ov, Object t, Object t1) {
           Item item = itemsTable.getSelectionModel().getSelectedItem();
           item.setNotes((String)t1);
        }
        
    }
    
    public class TableListener implements ChangeListener{

        @Override
        public void changed(ObservableValue ov, Object t, Object t1) {
            System.out.println(ov + " " + t + " " + t1);
            notesValue.setText(((Item)t1).getNotes());
        }
        
    }

    public void playMusicFile() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        Boolean playing = false;

        Long currentFrame;
        Clip clip;

        // current status of clip
        String status;

        AudioInputStream audioInputStream;
        String filePath = musicFile.getText();

        // create AudioInputStream object
        audioInputStream =
                AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());

        // create clip reference
        clip = AudioSystem.getClip();

        // open audioInputStream to the clip
        clip.open(audioInputStream);

        clip.loop(Clip.LOOP_CONTINUOUSLY);

        clip.start();
    }
    
    public void initList(){
        categoriesList.getItems().add("Items");
        itemsList = FXCollections.observableArrayList();
        
        categoriesList.getItems().add("Locations");
        locationsList = FXCollections.observableArrayList();
        
        categoriesList.getItems().add("People");
        peopleList = FXCollections.observableArrayList();
        
        categoriesList.getItems().add("Miscellaneous");
        miscList = FXCollections.observableArrayList(); 
        
        //add a listener
        listListener = new ListListener();
        categoriesList.getSelectionModel().selectedItemProperty().addListener(listListener);
        
    }
    
    public void newItem(){
        String selectedCategory = categoriesList.getSelectionModel().getSelectedItems().get(0);
        System.out.println(selectedCategory);
        Item item = new Item("Name", "Description"); 
        switch(selectedCategory){
            case("Items"):
                itemsList.add(item);
                System.out.println("Item Added");
                break;
            case("Locations"):
                locationsList.add(item);
                System.out.println("Locations Added");
                break;
            case("People"):
                peopleList.add(item);
                System.out.println("People Added");
                break;
            case("Miscellaneous"):
                miscList.add(item);
                System.out.println("Misc Added");
                break;
            default:
                break; 
        }
    }
    
    public void deleteItem(){
        String selectedCategory = categoriesList.getSelectionModel().getSelectedItems().get(0);
        Item selectedItem = itemsTable.getSelectionModel().getSelectedItem();
        String filePath = selectedCategory + "/" + selectedItem.toString() + ".dnd";
        System.out.println(selectedCategory);
        switch(selectedCategory){
            case("Items"):
                itemsList.remove(selectedItem);
                System.out.println("Item Removed");
                break;
            case("Locations"):
                locationsList.remove(selectedItem);
                System.out.println("Location Removed");
                break;
            case("People"):
                peopleList.remove(selectedItem);
                System.out.println("Person Removed");
                break;
            case("Miscellaneous"):
                miscList.remove(selectedItem);
                System.out.println("Misc Removed");
                break;
            default:
                break; 
        }
        File file = new File(filePath);
        file.delete();
    }
    
    public void saveItem() throws FileNotFoundException, IOException{
        String selectedCategory = categoriesList.getSelectionModel().getSelectedItems().get(0);
        Item selectedItem = itemsTable.getSelectionModel().getSelectedItem();
        
        String filePath = selectedCategory + "/" + selectedItem.toString() + ".dnd";
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream(filePath);
        } catch (FileNotFoundException ex) {
            File file = new File(filePath);
            file.createNewFile();
            fileOut = new FileOutputStream(filePath);
        }
        try (ObjectOutputStream iteOut = new ObjectOutputStream(fileOut)) {
            iteOut.writeObject(selectedItem.getName());
            iteOut.writeObject(selectedItem.getDescription());
            iteOut.writeObject(selectedItem.getNotes());
        }
        System.out.println("Save Complete!");
    }
    
    public void loadItems() throws FileNotFoundException, IOException, ClassNotFoundException{
        File path = new File("items");
        File[] files = path.listFiles((File pathname) -> pathname.toString().contains(".dnd"));
        for(File file : files){
            FileInputStream fileIn = new FileInputStream(file);
          
            ObjectInputStream obj = new ObjectInputStream(fileIn);
  
            String tempName = (String)obj.readObject();
            String tempDesc = (String)obj.readObject();
            String tempNote = (String)obj.readObject();
            
            Item item = new Item(tempName, tempDesc);
            item.setNotes(tempNote);
            
            System.out.println(item.getName());
            System.out.println(item.getDescription());
            System.out.println(item.getNotes());
            
            itemsList.add(item);
            System.out.println("Item Added");
            
        }
        
        path = new File("locations");
        files = path.listFiles((File pathname) -> pathname.toString().contains(".dnd"));
        for(File file : files){
            FileInputStream fileIn = new FileInputStream(file);
          
            ObjectInputStream obj = new ObjectInputStream(fileIn);
  
            String tempName = (String)obj.readObject();
            String tempDesc = (String)obj.readObject();
            String tempNote = (String)obj.readObject();
            
            Item item = new Item(tempName, tempDesc);
            item.setNotes(tempNote);
            
            System.out.println(item.getName());
            System.out.println(item.getDescription());
            System.out.println(item.getNotes());
            
            locationsList.add(item);
            System.out.println("Locale Added");
            
        }
        path = new File("people");
        files = path.listFiles((File pathname) -> pathname.toString().contains(".dnd"));
        for(File file : files){
            FileInputStream fileIn = new FileInputStream(file);
          
            ObjectInputStream obj = new ObjectInputStream(fileIn);
  
            String tempName = (String)obj.readObject();
            String tempDesc = (String)obj.readObject();
            String tempNote = (String)obj.readObject();
            
            Item item = new Item(tempName, tempDesc);
            item.setNotes(tempNote);
            
            System.out.println(item.getName());
            System.out.println(item.getDescription());
            System.out.println(item.getNotes());
            
            peopleList.add(item);
            System.out.println("Person Added");
            
        }
        path = new File("Miscellaneous");
        files = path.listFiles((File pathname) -> pathname.toString().contains(".dnd"));
        for(File file : files){
            FileInputStream fileIn = new FileInputStream(file);
          
            ObjectInputStream obj = new ObjectInputStream(fileIn);
  
            String tempName = (String)obj.readObject();
            String tempDesc = (String)obj.readObject();
            String tempNote = (String)obj.readObject();
            
            Item item = new Item(tempName, tempDesc);
            item.setNotes(tempNote);
            
            System.out.println(item.getName());
            System.out.println(item.getDescription());
            System.out.println(item.getNotes());
            
            miscList.add(item);
            System.out.println("Miscellany Added");
            
        }
    }
}
    
    