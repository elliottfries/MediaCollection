package MediaCollection;

/**
 *@author Elliott Fries
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MediaCollection extends Application {

    
    private static ArrayList<MediaItem> collection = new ArrayList<>();
    private static ObservableList<MediaItem> visibleList = FXCollections.observableArrayList();
    private static ListView<MediaItem> myList = new ListView(visibleList);
    private static Scanner in = new Scanner(System.in);

    //Adds an item to the collection
    public static void addItem(String title, String format) {

        MediaItem newItem = new MediaItem(title, format);

        if (!collection.contains(newItem)) {
            collection.add(newItem);
            visibleList.add(newItem);
            visibleList.setAll();;
            myList.refresh();
            storeCollection();
        } else {

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Uh Oh!");
            alert.setHeaderText("Duplicate Error");
            alert.setContentText("Error: You already have an item with the title " + title);
            alert.showAndWait();
        }
    }

    //removes an item from the collection
    public static void removeItem(String remove) {

        MediaItem removeThis = new MediaItem(remove, "temp");
        if (collection.contains(removeThis)) {
            collection.remove(removeThis);
            visibleList.remove(removeThis);
            myList.refresh();
            storeCollection();
        } else {
            System.out.println("How did you do this?");
        }
    }

    //marks an item as on loan
    public static void loanItem(String title, String format, String person, Date date) {

        
        MediaItem loanThis = new MediaItem(title, "temp");
        if (collection.contains(loanThis)) {
            
            MediaItem theItem = null;
            for (MediaItem item : collection) {
                if (item.equals(loanThis)) {
                    theItem = item;
                    theItem.setOnLoan(title, format, person, date);
                    myList.refresh();
                    storeCollection();
                    break;
                }
            }
            //onLoan must be set to null to loan

        }
    }

    //marks a title as no longer on loan
    public static void returnItem(String title) {

        MediaItem returnThis = new MediaItem(title, "temp");
        if (collection.contains(returnThis)) {

            MediaItem theItem = null;
            for (MediaItem item : collection) {
                if (item.equals(returnThis)) {
                    theItem = returnThis;
                        returnThis.returnItem();
                        myList.refresh();
                        storeCollection();
                    break;
                }
            }

        } 
    }


    //saves the collection
    public static void storeCollection() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(new File("media.dat")));) {
            oos.writeObject(collection);
        } catch (Exception e) {
            System.out.println("Error: Unable to save the updates to your "
                    + "collection!");
        }
    }

    //loads the collection
    public static void readCollection() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(new File("media.dat")));) {
            collection = (ArrayList<MediaItem>) ois.readObject();
        } catch (Exception e) {
            System.out.println("Error: Unable to load your collection!");
        }
    }

    public static void main(String[] args) {

        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //pane set up
        BorderPane rootPane = new BorderPane();
        GridPane gp = new GridPane();
        rootPane.setLeft(myList);
        rootPane.setCenter(gp);
        gp.setVgap(5);
        gp.setHgap(5);

        

        //list loading
        readCollection();
        visibleList.setAll(collection);
        myList.refresh();
        

        //pane size adjustment
        rootPane.prefWidth(700);
        rootPane.prefHeight(400);
        
        gp.prefWidth(400);
        gp.prefHeight(400);

        //button, text field, radio button, label, and toggle group creation
        Button addTitle = new Button("Add");
        Button removeTitle = new Button("Remove");
        Button returnTitle = new Button("Return");
        Button loanTitle = new Button("Loan");

        TextField tfTitle = new TextField();
        TextField tfFormat = new TextField();
        TextField loanedTo = new TextField();
            TextField loanedDate = new TextField("MM-DD-YYYY");

        RadioButton sortTitle = new RadioButton("Sort by Title");
        RadioButton sortDate = new RadioButton("Sort by Date ");
        ToggleGroup tg = new ToggleGroup();
        sortTitle.setToggleGroup(tg);
        sortDate.setToggleGroup(tg);
        
        Label lTitle = new Label("Title: ");
        Label lFormat = new Label("Format: ");
        Label loanTo = new Label("Loan to: ");
        Label loanOn = new Label("Loan Date: ");
        Label sort = new Label("Sort: ");

        //grid pane element positioning
        gp.add(lTitle, 0, 0);
        gp.add(lFormat, 0, 1);
        gp.add(loanTo, 0, 12);
        gp.add(loanOn, 0, 13);
        gp.add(sort, 0, 18);
        gp.add(tfTitle, 1, 0);
        gp.add(tfFormat, 1, 1);
        gp.add(addTitle, 1, 2);
        gp.add(removeTitle, 1, 6);
        gp.add(returnTitle, 1, 8);
        gp.add(loanedTo, 1, 12);
        gp.add(loanedDate, 1, 13);
        gp.add(loanTitle, 1, 14);
        gp.add(sortTitle, 1, 19);
        gp.add(sortDate, 1, 20);

        //ADD ITEM EVENT
        addTitle.setOnAction(titleAdd -> {
            try {
                String title = tfTitle.getText();
                String format = tfFormat.getText();
                addItem(title, format);
            } catch (NullPointerException npe) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Uh Oh!");
                alert.setHeaderText("Error");
                alert.setContentText("Error: You did not enter a title name and/or"
                        + "a format");
                alert.showAndWait();
            }
        });

        //REMOVE ITEM EVENT
        removeTitle.setOnAction(titleRemove -> {
            try {
                MediaItem rSelect = myList.getSelectionModel().getSelectedItem();
                String remove = rSelect.getName();
                removeItem(remove);
            } catch (NullPointerException npe) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Uh Oh!");
                alert.setHeaderText("Error");
                alert.setContentText("Error: You did not select a title to be remove");
                alert.showAndWait();
            }
        });

        //RETURN ITEM EVENT
        returnTitle.setOnAction(titleReturn -> {
            try {
                MediaItem rSelect = myList.getSelectionModel().getSelectedItem();
                String returnItem = rSelect.getName();
                String returnFormat = rSelect.getFormat();
                removeItem(returnItem);
                addItem(returnItem, returnFormat);
            } catch (NullPointerException npe) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Uh Oh!");
                alert.setHeaderText("Error");
                alert.setContentText("Error: You did not select a title to be returned");
                alert.showAndWait();
            }

        });

        //LOAN ITEM EVENT
        loanTitle.setOnAction(titleLoan -> {

            try {
                String date = loanedDate.getText();
            
                    Date loanedOn = new SimpleDateFormat(
                            "MM-dd-yyyy").parse(date);
                    
                String person = loanedTo.getText();

                MediaItem rSelect = myList.getSelectionModel().getSelectedItem();
                String loanItem = rSelect.getName();
                String itemFormat = rSelect.getFormat();

                loanItem(loanItem, itemFormat, person, loanedOn);
            } catch (NullPointerException npe) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Uh Oh!");
                alert.setHeaderText("Error");
                alert.setContentText("Error: You did not select a title to be loaned out");
                alert.showAndWait();
            } catch (ParseException ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Uh Oh!");
                alert.setHeaderText("Date Input Error");
                alert.setContentText("That is not a valid date.");
                alert.showAndWait();
            }
        });

        //sort by title
        sortTitle.setOnAction(titleSort -> {
            Collections.sort(visibleList);
            
            myList.refresh();
        });

        //sort by date
        sortDate.setOnAction(dateSort -> {
            Collections.sort(visibleList, new Comparator<MediaItem>() {	
                
                @Override
                public int compare(MediaItem t, MediaItem t1)  {
                    try {
                        return t.getDate().compareTo(t1.getDate());
                    } catch (NullPointerException npe) {
                        
                    }
                   return 1;
                }
            });
            myList.refresh();
        });

        //scene creation
        Scene scene = new Scene(rootPane, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
