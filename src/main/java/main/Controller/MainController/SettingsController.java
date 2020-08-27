package main.Controller.MainController;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import main.filter.*;
import main.filter.assiClasses.Filter;
import model.BaseImage;
import model.FilterStack;

public class SettingsController {

    TextField stackName;
    ChoiceBox<BaseImage> baseImage;
    ListView<FilterStack> settingsStackList;
    Button addStack;
    Button removeStack;
    ListView<Filter> filterList;
    Button up;
    Button down;
    Button removeFilter;
    ToggleButton actFilter;
    Button add;
    ChoiceBox<String> addType;
    HBox container;

    public SettingsController(HBox container, TextField stackName, ChoiceBox<BaseImage> baseImage, ListView<FilterStack> settingsStackList,
            Button addStack, Button removeStack, ListView<Filter> filterList,
            Button up, Button down, Button removeFilter,
            ToggleButton actFiltr, Button add, ChoiceBox<String> addType){

        this.container = container;
        this.stackName = stackName;
        this.baseImage = baseImage;
        this.settingsStackList = settingsStackList;
        this.addStack = addStack;
        this.removeStack = removeStack;
        this.filterList = filterList;
        this.up = up;
        this.down = down;
        this.removeFilter = removeFilter;
        this.actFilter = actFiltr;
        this.add = add;
        this.addType = addType;

        initStackList();

        add.setOnAction(t -> addFilter());
        filterList.setOnMouseClicked(t -> viewSelectedFilter());
        settingsStackList.setOnMouseClicked(t -> viewSelectedStack());
        up.setOnAction(t -> moveUp());
        down.setOnAction(t -> moveDown());
        removeFilter.setOnAction(t -> deleteFilter());
        actFilter.setOnAction(t -> filterList.getSelectionModel().getSelectedItem().activ = actFilter.isSelected());
        addStack.setOnAction(t -> newStack());
        removeStack.setOnAction(t -> deleteStack());
        stackName.setOnKeyTyped(t -> changeStackName());
        baseImage.setOnAction(t -> setBaseImage());

    }

    private void addFilter(){
        Filter toAdd = null;
        switch (addType.getValue()){
            case "Voronoi": toAdd = new CellCalc(filterList, container);break;
            case "Limit Colors": toAdd = new simCalc(filterList, container);break;
            case "Smooth": toAdd = new FlatCalc(filterList, container);break;
            case "Smooth2": toAdd = new LineSmooth(filterList, container);break;
            case "Lines": toAdd = new LineCalc(filterList, container);break;
            case "Specific Colors": toAdd = new SpecificColors(filterList, container);break;
            case "Replace Color": toAdd = new RepColor(filterList, container);break;
            case "Saturation&Contrast": toAdd = new SatConCalc(filterList, container);break;
            case "Hue-Keep": toAdd = new HueKeepCalc(filterList, container);break;
        }
        if (toAdd != null){
            try {
                settingsStackList.getSelectionModel().getSelectedItem().filters.add(toAdd);
            } catch (NullPointerException e){
                Platform.runLater(new Runnable() {
                    public void run() {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("No Stack");
                        alert.setHeaderText("add Stack first");
                        alert.showAndWait();
                    }
                });
            }

            filterList.refresh();
        }
    }

    private void deleteFilter(){
        settingsStackList.getSelectionModel().getSelectedItem().filters.remove(filterList.getSelectionModel().getSelectedItem());
        filterList.getSelectionModel().getSelectedItem().deleteFilter();
    }

    private void viewSelectedFilter(){
        container.getChildren().clear();
        container.getChildren().add(filterList.getSelectionModel().getSelectedItem().screen.getParent());
        actFilter.setSelected(filterList.getSelectionModel().getSelectedItem().activ);
    }

    private void moveUp(){
        Filter filter = filterList.getSelectionModel().getSelectedItem();
        if (filter != null && filterList.getItems().indexOf(filter)>0){
            int index = filterList.getItems().indexOf(filter);
            FilterStack stack = settingsStackList.getSelectionModel().getSelectedItem();
            stack.filters.set(index, filterList.getItems().get(index-1));
            stack.filters.set(index -1, filter);
        }
    }

    private void moveDown(){
        Filter filter = filterList.getSelectionModel().getSelectedItem();
        if (filter != null && filterList.getItems().indexOf(filter)<filterList.getItems().size()-1){
            int index = filterList.getItems().indexOf(filter);
            FilterStack stack = settingsStackList.getSelectionModel().getSelectedItem();
            stack.filters.set(index, filterList.getItems().get(index+1));
            stack.filters.set(index +1, filter);
        }
    }

    private void initStackList(){
        settingsStackList.setItems(FilterStack.stacks);
    }

    private void newStack(){
        FilterStack stack = new FilterStack(MainScreenCon.exeThread);
    }

    private void deleteStack(){
        settingsStackList.getSelectionModel().getSelectedItem().delete();
    }

    private void viewSelectedStack(){
        filterList.setItems(settingsStackList.getSelectionModel().getSelectedItem().filters);
        stackName.setText(settingsStackList.getSelectionModel().getSelectedItem().stackName);
        baseImage.getSelectionModel().select(settingsStackList.getSelectionModel().getSelectedItem().baseImage);
    }

    private void changeStackName(){
        settingsStackList.getSelectionModel().getSelectedItem().stackName = stackName.getText();
    }

    private void setBaseImage(){
        settingsStackList.getSelectionModel().getSelectedItem().baseImage = baseImage.getSelectionModel().getSelectedItem();
    }

}
