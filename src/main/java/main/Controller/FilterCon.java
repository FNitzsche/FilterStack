package main.Controller;

import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import main.FXMLLoad;
import main.filter.Filter;

public class FilterCon {
    ListView view;
    FXMLLoad load;
    Filter parent;
    HBox container;

    public void delete(){
        view.getItems().remove(parent);
        container.getChildren().clear();
        //load.delete();
    }
}