package main.Controller.FilterController;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import main.FXMLLoad;
import main.filter.assiClasses.Filter;
import model.FilterStack;

public class MergeCon extends FilterCon {
    @FXML
    ChoiceBox<FilterStack> image;
    @FXML
    ChoiceBox<String> mode;
    @FXML
    TextField value;
    @FXML
    CheckBox invert;
    @FXML
    ChoiceBox<FilterStack> mask;
    @FXML
    CheckBox useMask;
    @FXML
    CheckBox sat;
    @FXML
    CheckBox con;

    public MergeCon(ListView view, FXMLLoad load, Filter parent, HBox container){
        this.view = view;
        this.load = load;
        this.parent = parent;
        this.container = container;
    }

    public FilterStack getImage(){
        return image.getValue();
    }

    public String getMode(){
        return mode.getValue();
    }

    public float getValue(){
        try {
            float ret = Float.parseFloat((value.getText()));
            return ret;
        } catch (Exception e){}
        return 0;
    }

    public boolean getInvert(){
        return invert.isSelected();
    }

    public  boolean getUseMask(){
        return useMask.isSelected();
    }

    public FilterStack getMask(){
        return mask.getValue();
    }

    public boolean getSat(){
        return sat.isSelected();
    }

    public boolean getCon(){
        return con.isSelected();
    }

    public void initialize(){

        image.setItems(FilterStack.stacks);
        mask.setItems(FilterStack.stacks);

        mode.getItems().add("Overlay");
        mode.getItems().add("Add");
        mode.getItems().add("Multiply");
        mode.getItems().add("Substract");

    }
}
