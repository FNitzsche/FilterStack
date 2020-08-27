package main.Controller.FilterController;

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
        try {
            int ret = Integer.parseInt((count.getText()));
            return ret;
        } catch (Exception e){}
        return 0;
    }

    public int getXPos(){
        try {
            int ret = Integer.parseInt((xp.getText()));
            return ret;
        } catch (Exception e){}
        return 0;
    }

    public int getYPos(){
        try {
            int ret = Integer.parseInt((yp.getText()));
            return ret;
        } catch (Exception e){}
        return 0;
    }

    public int getW(){
        try {
            int ret = Integer.parseInt((w.getText()));
            return ret;
        } catch (Exception e){}
        return 0;
    }

    public int getH(){
        try {
            int ret = Integer.parseInt((h.getText()));
            return ret;
        } catch (Exception e){}
        return 0;
    }

    public void initialize(){
    }


}
