package main.Controller.FilterController;

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
        try {
            float ret = Float.parseFloat((sat.getText()));
            return ret;
        } catch (Exception e){}
        return 0;
    }

    public float getCon(){
        try {
            float ret = Float.parseFloat((con.getText()));
            return ret;
        } catch (Exception e){}
        return 0;
    }

    public void initialize(){
    }

}
