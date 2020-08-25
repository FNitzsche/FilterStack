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
        return Integer.parseInt(iter.getText());
    }

    public float getBorder(){
        return Float.parseFloat(border.getText());
    }

    public int getPixelC(){
        return Integer.parseInt(pixelC.getText());
    }

    public void initialize(){
        remove.setOnAction(t -> {
            delete();
        });
    }
}
