package main.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import main.FXMLLoad;
import main.filter.assiClasses.Filter;

public class LineCon extends FilterCon {
    @FXML
    private TextField iter;
    @FXML
    private TextField rate;
    @FXML
    private TextField x;
    @FXML
    private TextField y;
    @FXML
    private TextField points;
    @FXML
    private TextField w;
    @FXML
    private Button remove;



    public LineCon(ListView view, FXMLLoad load, Filter parent, HBox container){
        this.view = view;
        this.load = load;
        this.parent = parent;
        this.container = container;
    }

    public int getIt(){
        return Integer.parseInt(iter.getText());
    }

    public float getRate(){
        return Float.parseFloat(rate.getText());
    }

    public int getX(){
        return Integer.parseInt(x.getText());
    }

    public int getY(){
        return Integer.parseInt(y.getText());
    }

    public int getPoints(){
        return Integer.parseInt(points.getText());
    }

    public float getW(){
        return Float.parseFloat(w.getText());
    }

    public void initialize(){
    }


}
