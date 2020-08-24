package main.filter;

import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import main.Controller.ReducedColorCon;
import main.Controller.VoronoiCon;
import main.FXMLLoad;
import main.filter.assiClasses.MyCom;

import java.util.Arrays;

public class simCalc extends Filter {

    int id = -1;
    static int count = 0;

    public simCalc (ListView view, HBox container){
        screen = new FXMLLoad("/reducedColor.fxml", new ReducedColorCon(view, screen, this, container));
        id = count++;
    }

     float[][][] cell(int n, float[][][] img, int resX, int rexY){{
        return cell(n, n, n, img, resX, rexY);
    }}

     public float[][][] cell(int r, int g, int b, float[][][] img, int resX, int rexY){
        float[][] flat = new float[resX*rexY][3];
        img = forColor(r, img, resX, rexY, 0, flat);
        img = forColor(g, img, resX, rexY, 1, flat);
        img = forColor(b, img, resX, rexY, 2, flat);

        return img;
    }

     private float[][][] forColor(int n, float[][][] img, int resX, int rexY, int index, float[][] flat){
        for (int i = 0; i < resX; i++) {
            for (int j = 0; j < rexY; j++) {
                flat[i*rexY+j][2] = i;
                flat[i*rexY+j][1] = j;
                flat[i*rexY+j][0] = img[i][j][index];
            }
        }
        Arrays.sort(flat, new MyCom());
        for (int i = 0; i < n; i++){
            float v = 0;
            for (int j = 0; j < resX*rexY/n; j++){
                v += flat[resX*rexY/n*i+j][0];
            }
            v /= resX*rexY/n;
            for (int j = 0; j < resX*rexY/n; j++){
                flat[resX*rexY/n*i+j][0] = v;
            }
        }
        for (int i = 0; i < resX; i++) {
            for (int j = 0; j < rexY; j++) {
                int x = (int)flat[i*rexY+j][2];
                int y = (int)flat[i*rexY+j][1];
                img[x][y][index] = flat[i*rexY+j][0];
            }
        }
        return img;
    }

    @Override
    public float[][][] run(float[][][] img, int resX, int resY, float delta) {
        ReducedColorCon con = screen.getController(ReducedColorCon.class);
        return cell(con.getRed(), con.getGreen(), con.getBlue(), img, resX, resY);
    }

    @Override
    public String toString(){
        ReducedColorCon con = screen.getController(ReducedColorCon.class);
        return "Reduced Color " + id + ", R: " + con.getRed() + ", G: " + con.getGreen() + ", B: " + con.getBlue();
    }
}
