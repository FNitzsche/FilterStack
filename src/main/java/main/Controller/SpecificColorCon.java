package main.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import main.FXMLLoad;
import main.filter.assiClasses.Filter;

public class SpecificColorCon extends FilterCon{

    @FXML
    private ListView<ColorPicker> colorList;
    @FXML
    private Button remove;
    @FXML
    private Button addC;
    @FXML
    private Button remC;



    public SpecificColorCon(ListView view, FXMLLoad load, Filter parent, HBox container){
        this.view = view;
        this.load = load;
        this.parent = parent;
        this.container = container;
    }

    public float[][] getColors(){
        float[][] colors = new float[colorList.getItems().size()][3];
        for(int i = 0; i < colorList.getItems().size(); i++){
            colors[i][0] = (float)colorList.getItems().get(i).getValue().getRed();
            colors[i][1] = (float)colorList.getItems().get(i).getValue().getGreen();
            colors[i][2] = (float)colorList.getItems().get(i).getValue().getBlue();
        }
        return colors;
    }

    public void addColor(){
        colorList.getItems().add(new ColorPicker());
    }

    public void removeColor(){
        colorList.getItems().remove(colorList.getSelectionModel().getSelectedItem());
    }

    public void initialize(){
        addC.setOnAction(t -> addColor());
        remC.setOnAction(t -> removeColor());
    }

}
