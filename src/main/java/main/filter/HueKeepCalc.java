package main.filter;

import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import main.Controller.HueKeepCon;
import main.Controller.RepColorCon;
import main.FXMLLoad;
import main.filter.assiClasses.Filter;

import java.util.Arrays;

public class HueKeepCalc extends Filter {

    int id = -1;
    static int count = 0;


    public HueKeepCalc(ListView view, HBox container){
        screen = new FXMLLoad("/onlyHue.fxml", new HueKeepCon(view, screen, this, container));
        id = count++;
    }

    @Override
    public float[][][] run(float[][][] img, int resX, int resY, float delta) {
        HueKeepCon con = screen.getController(HueKeepCon.class);
        return calc(con.getHue(), con.getDist(), img, resX, resY);
    }

    @Override
    public String toString(){
        HueKeepCon con = screen.getController(HueKeepCon.class);
        return (activ? "ACTIVE : ": "") + "Hue-Keep " + id + ", R: " + con.getHue()[0] + ", G: " + con.getHue()[1] + ", B: " + con.getHue()[2];
    }

    public float[][][] calc(float[] color, float maxDist, float[][][] img, int resX, int resY){

        float length = (float)Math.sqrt(Math.pow(color[0], 2) + Math.pow(color[1], 2) + Math.pow(color[2], 2));
        System.out.println("maxDist: " + maxDist);

        Arrays.stream(img).parallel().flatMap(a -> Arrays.stream(a)).forEach(pixel -> {
            float pl = (float)Math.sqrt(Math.pow(pixel[0], 2) + Math.pow(pixel[1], 2) + Math.pow(pixel[2], 2));
            float dist = 1;
            if (pl != 0) {
                dist = (float) (color[0] * pixel[0] + color[1] * pixel[1] + color[2] * pixel[2]) / (pl * length);
            }
            //System.out.println("dist : " + dist);
            if (1-dist > maxDist){
                float mid = (pixel[0]+pixel[1]+pixel[2])/3;
                pixel[0] = mid;
                pixel[1] = mid;
                pixel[2] = mid;
            }
        });


        return img;
    }

}
