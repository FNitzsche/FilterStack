package main.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import main.FXMLLoad;
import main.filter.assiClasses.Filter;

public class SmoothTwoCon extends FilterCon{
    @FXML
    private TextField iter;
    @FXML
    private TextField border;
    @FXML
    private TextField pixelC;
    @FXML
    private Button remove;



    public SmoothTwoCon(ListView view, FXMLLoad load, Filter parent, HBox container){
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

    public float getBorder(){
        try {
            float ret = Float.parseFloat((border.getText()));
            return ret;
        } catch (Exception e){}
        return 0;
    }

    public int getPixelC(){
        try {
            int ret = Integer.parseInt((pixelC.getText()));
            return ret;
        } catch (Exception e){}
        return 0;
    }

    public void initialize(){
    }
}
