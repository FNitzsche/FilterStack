package main.Controller.FilterController;

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
        try {
            int ret = Integer.parseInt((iter.getText()));
            return ret;
        } catch (Exception e){}
        return 0;
    }

    public float getRate(){
        try {
            float ret = Float.parseFloat((rate.getText()));
            return ret;
        } catch (Exception e){}
        return 0;
    }

    public int getX(){
        try {
            int ret = Integer.parseInt((x.getText()));
            return ret;
        } catch (Exception e){}
        return 0;
    }

    public int getY(){
        try {
            int ret = Integer.parseInt((y.getText()));
            return ret;
        } catch (Exception e){}
        return 0;
    }

    public int getPoints(){
        try {
            int ret = Integer.parseInt((points.getText()));
            return ret;
        } catch (Exception e){}
        return 0;
    }

    public float getW(){
        try {
            float ret = Float.parseFloat((w.getText()));
            return ret;
        } catch (Exception e){}
        return 0;
    }

    public void initialize(){
    }


}
