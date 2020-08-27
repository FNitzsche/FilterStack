package main.filter;

import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import main.Controller.FilterController.MergeCon;
import main.Controller.FilterController.RepColorCon;
import main.FXMLLoad;
import main.filter.assiClasses.Filter;
import model.FilterStack;

import java.util.Arrays;

public class MergeCalc extends Filter {

    int id = -1;
    static int count = 0;


    public MergeCalc(ListView view, HBox container){
        screen = new FXMLLoad("/merge.fxml", new MergeCon(view, screen, this, container));
        id = count++;
    }

    @Override
    public float[][][] run(float[][][] img, int resX, int resY, float delta, boolean fullRun) {
        MergeCon con = screen.getController(MergeCon.class);
        return calc(con.getImage(), con.getMode(), con.getValue(), con.getInvert(), con.getUseMask(), con.getMask(), con.getSat(), img, resX, resY, delta, con.getCon(), fullRun);
    }

    @Override
    public String toString(){
        MergeCon con = screen.getController(MergeCon.class);
        return (activ? "ACTIVE : ": "") + "Merge " + id + ", with: " + (con.getImage()== null? "none": con.getImage().toString());
    }

    public float[][][] calc(FilterStack stack, String mode, float border, boolean invert, boolean useMask,
                            FilterStack mask, boolean useSat, float[][][] img, int resX, int resY, float delta, boolean con, boolean fullRun){

        float[][][] overlay = stack.runFilters(fullRun, resX, resY, delta);
        float[][][] maskArray = {};
        if (useMask) {
            maskArray = mask.runFilters(fullRun, resX, resY, delta);
        }


        img = overlay(img, overlay, maskArray, useMask, border, invert, useSat, con);


        return img;
    }


    public float[][][] overlay(float[][][] img, float[][][] overlay, float[][][] maskArray, boolean useMask, float border, boolean invert, boolean useSat, boolean con){
        for (int i = 0; i < img.length; i++){
            for (int j = 0; j < img[0].length; j++){
                if (useMask){
                    float mid = mid = (maskArray[i][j][0] + maskArray[i][j][1] + maskArray[i][j][2]) / 3;;
                    if (useSat) {
                        mid = (float)Math.max(0, Math.min(1, (Math.pow(maskArray[i][j][0]-mid, 2) + Math.pow(maskArray[i][j][1]-mid, 2) + Math.pow(maskArray[i][j][2]-mid, 2))));
                    }
                    if (con){
                        img[i][j][0] = img[i][j][0]*(1-mid) + overlay[i][j][0]*mid;
                        img[i][j][1] = img[i][j][1]*(1-mid) + overlay[i][j][1]*mid;
                        img[i][j][2] = img[i][j][2]*(1-mid) + overlay[i][j][2]*mid;
                    } else {
                        if (!invert){
                            if (mid > border){
                                img[i][j][0] = overlay[i][j][0];
                                img[i][j][1] = overlay[i][j][1];
                                img[i][j][2] = overlay[i][j][2];
                            }
                        } else {
                            if (mid < border){
                                img[i][j][0] = overlay[i][j][0];
                                img[i][j][1] = overlay[i][j][1];
                                img[i][j][2] = overlay[i][j][2];
                            }
                        }
                    }
                } else {
                    img[i][j][0] = overlay[i][j][0];
                    img[i][j][1] = overlay[i][j][1];
                    img[i][j][2] = overlay[i][j][2];
                }
            }
        }
        return img;
    }

}
