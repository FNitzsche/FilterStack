package main.Controller.FilterController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import main.FXMLLoad;
import main.filter.assiClasses.Filter;

public class HueKeepCon extends FilterCon {

    @FXML
    private ColorPicker hue;
    @FXML
    private TextField distance;
    @FXML
    private Button remove;



    public HueKeepCon(ListView view, FXMLLoad load, Filter parent, HBox container){
        this.view = view;
        this.load = load;
        this.parent = parent;
        this.container = container;
    }

    public float[] getHue(){
        float[] colorArray = new float[3];

        colorArray[0] = (float)hue.getValue().getRed();
        colorArray[1] = (float)hue.getValue().getGreen();
        colorArray[2] = (float)hue.getValue().getBlue();

        return colorArray;
    }

    public float getDist(){
        try {
            float ret = Float.parseFloat((distance.getText()));
            return ret;
        } catch (Exception e){}
        return 0;
    }

    public void initialize(){
    }

}
