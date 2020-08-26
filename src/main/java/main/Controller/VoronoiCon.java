package main.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import main.FXMLLoad;
import main.filter.assiClasses.Filter;

public class VoronoiCon extends FilterCon {

    @FXML
    private TextField count;
    @FXML
    private TextField xp;
    @FXML
    private TextField yp;
    @FXML
    private TextField w;
    @FXML
    private TextField h;
    @FXML
    private Button remove;



    public VoronoiCon(ListView view, FXMLLoad load, Filter parent, HBox container){
        this.view = view;
        this.load = load;
        this.parent = parent;
        this.container = container;
    }

    public int getCellCount(){
        return Integer.parseInt(count.getText());
    }

    public int getXPos(){
        return Integer.parseInt(xp.getText());
    }

    public int getYPos(){
        return Integer.parseInt(yp.getText());
    }

    public int getW(){
        return Integer.parseInt(w.getText());
    }

    public int getH(){
        return Integer.parseInt(h.getText());
    }

    public void initialize(){
    }


}
