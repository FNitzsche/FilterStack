package main.filter;

import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import main.Controller.MainScreenCon;
import main.Controller.SatConCon;
import main.Controller.SpecificColorCon;
import main.FXMLLoad;
import main.filter.assiClasses.Filter;

import java.awt.event.MouseAdapter;
import java.util.Arrays;

public class SatConCalc extends Filter {

    int id = -1;
    static int count = 0;


    public SatConCalc(ListView view, HBox container){
        screen = new FXMLLoad("/SatCon.fxml", new SatConCon(view, screen, this, container));
        id = count++;
    }

    @Override
    public float[][][] run(float[][][] img, int resX, int resY, float delta) {
        SatConCon con = screen.getController(SatConCon.class);
        return calc(con.getSat(), con.getCon(), img, resX, resY);
    }

    @Override
    public String toString(){
        SatConCon con = screen.getController(SatConCon.class);
        return (activ? "ACTIVE : ": "") + "Saturation&Contrast " + id + ", Sat: " + con.getSat() + ", Con: " + con.getCon();
    }

    public float[][][] calc(float sat, float con, float[][][] img, int resX, int resY){

        if (sat != 0){
            img = saturation(sat, img, resX, resY);
        }

        if (con != 1){
            img = contrast(con, img, resX, resY);
        }

        return img;

    }


    private float[][][] saturation(float sat, float[][][] img, int resX, int resY){
        Arrays.stream(img).parallel().flatMap(t -> Arrays.stream(t)).forEach(pixel -> {
            float mid = pixel[0]+pixel[1]+pixel[2];
            mid /= 3;
            pixel[0] = Math.max(0, Math.min(1, mid+(pixel[0]-mid)*sat));
            pixel[1] = Math.max(0, Math.min(1, mid+(pixel[1]-mid)*sat));
            pixel[2] = Math.max(0, Math.min(1, mid+(pixel[2]-mid)*sat));
        });
        return img;
    }

    private float[][][] contrast(float con, float[][][] img, int resX, int resY){

        float mid = Arrays.stream(img).parallel().flatMap(t -> Arrays.stream(t)).map(p -> (p[0]+p[1]+p[2])/3).reduce(0f, (f1, f2) -> (f1+f2)/2);

        Arrays.stream(img).parallel().flatMap(t -> Arrays.stream(t)).forEach(pixel -> {
            float m = pixel[0]+pixel[1]+pixel[2];
            m /= 3;
            float delta = ((m-mid)*con+mid)-m;
            pixel[0] = Math.max(0, Math.min(1, pixel[0]+delta));
            pixel[1] = Math.max(0, Math.min(1, pixel[1]+delta));
            pixel[2] = Math.max(0, Math.min(1, pixel[2]+delta));
        });

        return img;
    }

}
