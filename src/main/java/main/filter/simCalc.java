package main.filter;

import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import main.Controller.ReducedColorCon;
import main.Controller.VoronoiCon;
import main.FXMLLoad;
import main.filter.assiClasses.MyCom;

import java.util.ArrayList;
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
        ArrayList<Integer> changePos = new ArrayList<>();
        for (int i = 1; i < flat.length; i++){
            if (flat[i][0] != flat[i-1][0]){
                changePos.add(i);
            }
        }

        float[][] positions = new float[n][2];
        for (int i = 0; i < n; i++){
            positions[i][0] = -1;
        }

        for (Integer i: changePos){
            for (int j = 1; j < n+1; j++){
                float dist = Math.abs((resX*rexY/n)*j - i);
                if (positions[j-1][0] == -1 || dist < positions[j-1][1]){
                    positions[j-1][0] = i;
                    positions[j-1][1] = dist;
                }
            }
        }


        for (int i = 0; i < n; i++){
            if (positions[i][0] != -1) {
                float v = 0;
                for (int j = (i == 0? 0: (int)positions[i-1][0]); j < positions[i][0]; j++) {
                    v += flat[j][0];
                }
                v /= positions[i][0]-(i == 0? 0: (int)positions[i-1][0]);
                for (int j = (i == 0? 0: (int)positions[i-1][0]); j < positions[i][0]; j++) {
                    flat[j][0] = v;
                }
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
