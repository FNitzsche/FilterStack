package main.filter;

import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import main.Controller.LineCon;
import main.Controller.SpecificColorCon;
import main.FXMLLoad;

import java.util.Arrays;

public class SpecificColors extends Filter {


    int id = -1;
    static int count = 0;


    public SpecificColors(ListView view, HBox container){
        screen = new FXMLLoad("/specificColors.fxml", new SpecificColorCon(view, screen, this, container));
        id = count++;
    }

    @Override
    public float[][][] run(float[][][] img, int resX, int resY, float delta) {
        SpecificColorCon con = screen.getController(SpecificColorCon.class);
        return calc(con.getColors(), img, resX, resY);
    }

    @Override
    public String toString(){
        SpecificColorCon con = screen.getController(SpecificColorCon.class);
        return "Specific Colors " + id + ", Count: " + con.getColors().length;
    }


    public float[][][] calc(float[][] colors, float[][][] img, int resX, int resY){
        Arrays.stream(img).parallel().flatMap(a -> Arrays.stream(a)).forEach(pixel -> {
            float sDist = 5;
            int c = -1;
            for (int i = 0; i < colors.length; i++){
                float dist = (float)Math.sqrt(Math.pow(colors[i][0]-pixel[0], 2) + Math.pow(colors[i][1]-pixel[1], 2) + Math.pow(colors[i][2]-pixel[2], 2));
                if (dist < sDist){
                    sDist = dist;
                    c = i;
                }
            }
            if (c != -1){
                pixel[0] = colors[c][0];
                pixel[1] = colors[c][1];
                pixel[2] = colors[c][2];
            }
        });

        return img;
    }
}
