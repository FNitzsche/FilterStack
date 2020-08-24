package main.filter;

import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import main.Controller.RepColorCon;
import main.Controller.SpecificColorCon;
import main.FXMLLoad;

import java.util.Arrays;

public class RepColor extends Filter {

    int id = -1;
    static int count = 0;


    public RepColor(ListView view, HBox container){
        screen = new FXMLLoad("/repColor.fxml", new RepColorCon(view, screen, this, container));
        id = count++;
    }

    @Override
    public float[][][] run(float[][][] img, int resX, int resY, float delta) {
        RepColorCon con = screen.getController(RepColorCon.class);
        return calc(con.getColor(), con.getDist(), con.getRepColor(), img, resX, resY);
    }

    @Override
    public String toString(){
        RepColorCon con = screen.getController(RepColorCon.class);
        return "Specific Colors " + id + ", R: " + con.getColor()[0] + ", G: " + con.getColor()[1] + ", B: " + con.getColor()[2];
    }

    public float[][][] calc(float[] color, float maxDist, float[] repColor, float[][][] img, int resX, int resY){

        Arrays.stream(img).parallel().flatMap(a -> Arrays.stream(a)).forEach(pixel -> {
            float dist = (float)Math.sqrt(Math.pow(color[0]-pixel[0], 2) + Math.pow(color[1]-pixel[1], 2) + Math.pow(color[2]-pixel[2], 2));
            if (dist < maxDist){
                pixel[0] = repColor[0];
                pixel[1] = repColor[1];
                pixel[2] = repColor[2];
            }
        });


        return img;
    }

}
