package main.filter.assiClasses;

import main.Controller.FilterController.FilterCon;
import main.FXMLLoad;

public abstract class Filter {

    public FXMLLoad screen = null;
    public boolean activ = true;

    public float[][][] runRun(float[][][] img, int resX, int resY, float delta, boolean fullRun){
        if (activ){
            return run(img, resX, resY, delta, fullRun);
        }
        return img;
    }

    public void deleteFilter(){
        screen.getController(FilterCon.class).delete();
    }
    
    public abstract float[][][] run(float[][][] img, int resX, int resY, float delta, boolean fullRun);
}
