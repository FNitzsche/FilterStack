package main.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import main.FXMLLoad;
import main.filter.Filter;

public class ReducedColorCon extends FilterCon {

    @FXML
    private TextField red;
    @FXML
    private TextField green;
    @FXML
    private TextField blue;
    @FXML
    private Button remove;



    public ReducedColorCon(ListView view, FXMLLoad load, Filter parent, HBox container){
        this.view = view;
        this.load = load;
        this.parent = parent;
        this.container = container;
    }

    public int getRed(){
        return Integer.parseInt(red.getText());
    }

    public int getGreen(){
        return Integer.parseInt(green.getText());
    }

    public int getBlue(){
        return Integer.parseInt(blue.getText());
    }

    public void initialize(){
        remove.setOnAction(t -> {
            delete();
        });
    }

}
