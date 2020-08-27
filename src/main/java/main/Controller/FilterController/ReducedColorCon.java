package main.Controller.FilterController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import main.FXMLLoad;
import main.filter.assiClasses.Filter;

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
        try {
            int ret = Integer.parseInt((red.getText()));
            return ret;
        } catch (Exception e){}
        return 0;
    }

    public int getGreen(){
        try {
            int ret = Integer.parseInt((green.getText()));
            return ret;
        } catch (Exception e){}
        return 0;
    }

    public int getBlue(){
        try {
            int ret = Integer.parseInt((blue.getText()));
            return ret;
        } catch (Exception e){}
        return 0;
    }

    public void initialize(){
    }

}
