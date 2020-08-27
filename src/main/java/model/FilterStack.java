package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import main.filter.assiClasses.Filter;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class FilterStack {

    public static ObservableList<FilterStack> stacks = FXCollections.observableList(new ArrayList<FilterStack>());
    static ExecutorService exeC;

     public BaseImage baseImage;

    public String stackName = "empty Stack";

    ArrayList<FilterStack> containing = new ArrayList<>();
    ArrayList<FilterStack> contained = new ArrayList<>();

    public ObservableList<Filter> filters = FXCollections.observableList(new ArrayList<Filter>());

    boolean ran = false;
    float[][][] lastImage;

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
        if (fullRun && ran){
            return lastImage;
        } else {
            float[][][] img = baseImage.getPreSize();
            for (Filter filter : filters) {
                img = filter.runRun(img, resX, resY, delta);
            }
            lastImage = img;
            ran = true;
            return img;
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

}
