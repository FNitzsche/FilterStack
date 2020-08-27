package main.filter;

import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import main.Controller.FilterController.LineCon;
import main.FXMLLoad;
import main.filter.assiClasses.Filter;

import java.util.ArrayList;
import java.util.Random;

public class LineCalc extends Filter {

    int id = -1;
    static int count = 0;


    public LineCalc (ListView view, HBox container){
        screen = new FXMLLoad("/line.fxml", new LineCon(view, screen, this, container));
        id = count++;
    }

    @Override
    public float[][][] run(float[][][] img, int resX, int resY, float delta) {
        LineCon con = screen.getController(LineCon.class);
        return calc(con.getIt(), con.getRate(), new float[]{con.getX(), con.getY()}, con.getW(), con.getPoints(), img, resX, resY, delta);
    }

    @Override
    public String toString(){
        LineCon con = screen.getController(LineCon.class);
        return (activ? "ACTIVE : ": "") + "Line " + id + ", Iterationen: " + con.getIt();
    }

    Random random = new Random();

    public float[][][] calc(int iter, float rate, float[] start, float width, int n, float[][][] img, int resX, int rexY, float delta){
        ArrayList<float[]> points = new ArrayList<>();
        float[][][] ret = new float[resX][rexY][3];
        for (int i = 0; i < n+1; i++){
            points.add(new float[]{random.nextFloat()*resX, random.nextFloat()*rexY});
        }
        float[] last = start;
        for (int a = 0; a < iter; a++) {
            for (float[] point : points) {
                float dirX = point[0] - last[0];
                float dirY = point[1] - last[1];
                float sub = (float) Math.sqrt(Math.pow(dirX, 2) + Math.pow(dirY, 2));
                dirX /= sub;
                dirY /= sub;

                float shortest = 100;
                int[] source = {0, 0};
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (i != 0 || j != 0) {
                            float dist = (float) Math.sqrt(Math.pow(dirX - i, 2) + Math.pow(dirY - j, 2));
                            if (dist < shortest) {
                                shortest = dist;
                                source[0] = i;
                                source[1] = j;
                            }
                        }
                    }
                }


                for (int i = 0; i < resX; i++) {
                    for (int j = 0; j < rexY; j++) {
                        float top = Math.abs(dirY*(i-point[0])- dirX*(j-point[1]));
                        float dist = (float) Math.sqrt(Math.pow(point[0] - i, 2) + Math.pow(point[1] - j, 2));
                        if (top < width*delta) {
                            int sX = i - source[0];
                            int sY = j - source[1];
                            if (sX >= 0 && sY >= 0 && sX < resX && sY < rexY) {
                                ret[i][j][0] = (img[i][j][0] + img[sX][sY][0] * rate) / (1 + rate);
                                ret[i][j][1] = (img[i][j][1] + img[sX][sY][1] * rate) / (1 + rate);
                                ret[i][j][2] = (img[i][j][2] + img[sX][sY][2] * rate) / (1 + rate);
                            } else {
                                ret[i][j][0] = img[i][j][0];
                                ret[i][j][1] = img[i][j][1];
                                ret[i][j][2] = img[i][j][2];
                            }
                        } else {
                            ret[i][j][0] = img[i][j][0];
                            ret[i][j][1] = img[i][j][1];
                            ret[i][j][2] = img[i][j][2];
                        }
                    }
                }
                last = point;
            }
            img = ret;
        }


        return img;
    }
}
