package main.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import main.FXMLLoad;
import main.filter.assiClasses.Filter;

public class SatConCon extends FilterCon {

    @FXML
    private TextField sat;
    @FXML
    private TextField con;
    @FXML
    private Button remove;



    public SatConCon(ListView view, FXMLLoad load, Filter parent, HBox container){
        this.view = view;
        this.load = load;
        this.parent = parent;
        this.container = container;
    }

    public float getSat(){
        return Float.parseFloat(sat.getText());
    }

    public float getCon(){
        return Float.parseFloat(con.getText());
    }

    public void initialize(){
        remove.setOnAction(t -> {
            delete();
        });
    }

}
