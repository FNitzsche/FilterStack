package model;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import main.filter.assiClasses.Filter;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class FilterStack implements Viewable{

    public static ObservableList<FilterStack> stacks = FXCollections.observableList(new ArrayList<FilterStack>());
    static ExecutorService exeC;

     public BaseImage baseImage;

    public String stackName = "empty Stack";

    public ArrayList<FilterStack> containing = new ArrayList<>();
    ArrayList<FilterStack> contained = new ArrayList<>();

    public ObservableList<Filter> filters = FXCollections.observableList(new ArrayList<Filter>());

    boolean ran = false;
    float[][][] lastImage;
    public float filtersFinished = 0;

    public FilterStack(ExecutorService exeC){
        this.exeC = exeC;
        stacks.add(this);
    }

    public void delete(){
        stacks.remove(this);
        if (baseImage != null) {
            baseImage.uses.remove(this);
        }
    }

    public ArrayList<FilterStack> getContainable(){
        ArrayList<FilterStack> con = new ArrayList<>();
        for (FilterStack stack: stacks){
            if (stack != this && !containing.contains(stack) && !contained.contains(stack)){
                con.add(stack);
            }
        }
        return con;
    }


    public void addParent(FilterStack parent){
        contained.add(parent);
        for (FilterStack stack: containing){
            stack.addParent(parent);
        }
    }

    public void addFilterStack(FilterStack child){
        containing.add(child);
        for (FilterStack stack: child.containing){
            if (!containing.contains(stack)){
                containing.add(stack);
            }
        }
    }

    public void removeParent(FilterStack parent){
        contained.remove(parent);
        for (FilterStack stack: containing){
            stack.removeParent(parent);
        }
    }

    public void removeFilterStack(FilterStack child){
        containing.remove(child);
        for (FilterStack stack: child.containing){
            boolean notContained = true;
            for (FilterStack stack1: child.containing){
                if (stack1.containing.contains(stack)){
                    notContained = false;
                }
            }
            if (notContained){
                containing.remove(stack);
            }
        }
    }



    public float[][][] runFilters(boolean fullRun, int resX, int resY, float delta){
        filtersFinished = 0;
        if (baseImage == null){
            Platform.runLater(new Runnable() {
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No Image");
                    alert.setHeaderText("select a base Image for " + stackName);
                    alert.showAndWait();
                }
            });
            return null;
        } else {
            if (fullRun && ran) {
                return lastImage;
            } else {
                float[][][] img = baseImage.getPreSize();
                if (fullRun) {
                    img = baseImage.getFullSize();
                }
                float toAdd = 1f / filters.size();
                for (Filter filter : filters) {
                    img = filter.runRun(img, resX, resY, delta);
                    filtersFinished += toAdd;
                }
                lastImage = img;
                ran = true;
                filtersFinished = 1;
                return img;
            }
        }
    }

    static public void resetRan(){
        for (FilterStack stack: stacks){
            stack.ran = false;
        }
    }

    @Override
    public String toString(){
        return stackName;
    }

    @Override
    public Image showImage(boolean fullRun, int resX, int rexY, float delta) {
        float[][][] tmp = lastImage;
        if (tmp!= null && tmp.length > 0) {
            WritableImage wimg = new WritableImage(resX, rexY);

            for (int i = 0; i < resX; i++) {
                for (int j = 0; j < rexY; j++) {
                    wimg.getPixelWriter().setColor(i, j, Color.color(Math.max(0, Math.min(1, tmp[i][j][0])),Math.max(0, Math.min(1, tmp[i][j][1])), Math.max(0, Math.min(1,tmp[i][j][2]))));
                }
            }

            return wimg;
        } else {
            System.out.println("no Image");
        }
        return null;
    }
}
