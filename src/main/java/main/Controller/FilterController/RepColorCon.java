package main.Controller.FilterController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import main.FXMLLoad;
import main.filter.assiClasses.Filter;

public class RepColorCon extends FilterCon {

    @FXML
    ColorPicker color;
    @FXML
    TextField distance;
    @FXML
    ColorPicker repColor;
    @FXML
    private Button remove;

    public RepColorCon(ListView view, FXMLLoad load, Filter parent, HBox container){
        this.view = view;
        this.load = load;
        this.parent = parent;
        this.container = container;
    }

    public float[] getColor(){
        float[] colorArray = new float[3];

        colorArray[0] = (float)color.getValue().getRed();
        colorArray[1] = (float)color.getValue().getGreen();
        colorArray[2] = (float)color.getValue().getBlue();

        return colorArray;
    }

    public float[] getRepColor(){
        float[] colorArray = new float[3];

        colorArray[0] = (float)repColor.getValue().getRed();
        colorArray[1] = (float)repColor.getValue().getGreen();
        colorArray[2] = (float)repColor.getValue().getBlue();

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
