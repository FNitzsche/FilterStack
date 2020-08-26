package main.filter;

import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import main.Controller.SmoothOneCon;
import main.FXMLLoad;
import main.filter.assiClasses.Filter;

import java.util.Arrays;

public class FlatCalc extends Filter {

    int id = -1;
    static int count = 0;

    public FlatCalc (ListView view, HBox container){
        screen = new FXMLLoad("/smoothOne.fxml", new SmoothOneCon(view, screen, this, container));
        id = count++;
    }

    public float[][][] calc(float border, float[][][] img, int resX, int rexY){

        float[][][] ret = new float[resX][rexY][3];

        for (int i = 1; i < resX-1; i++){
            for (int j = 1; j < rexY-1; j++){
                float[] m = new float[3];
                for (int x = -1; x < 2; x++){
                    for (int y = -1; y < 2; y++){
                        if (x != 0 || y!= 0 )
                        {
                            m[0] += img[i][j][0]-img[i+x][j+y][0];
                            m[1] += img[i][j][1]-img[i+x][j+y][1];
                            m[2] += img[i][j][2]-img[i+x][j+y][2];
                        }
                    }
                }
                m[0] /= 8;
                m[1] /= 8;
                m[2] /= 8;

                float varianz = 0;
                for (int x = -1; x < 2; x++){
                    for (int y = -1; y < 2; y++){
                        if (x != 0 || y!= 0 )
                        {
                            float[] vars = {(float)Math.pow(m[0]-img[i+x][j+y][0], 2), (float)Math.pow(m[1]-img[i+x][j+y][1], 2), (float)Math.pow(m[2]-img[i+x][j+y][2], 2)};
                            Arrays.sort(vars);
                            float tmp = vars[0]*0.5f + vars[1]*0.3f + vars[2]*0.2f;
                            varianz += tmp;
                        }
                    }
                }

                if (varianz/8 < border){

                    float[] combined = new float[3];
                    for (int x = -1; x < 2; x++){
                        for (int y = -1; y < 2; y++){
                            if (x != 0 || y!= 0 )
                            {
                                combined[0] += img[i+x][j+y][0];
                                combined[1] += img[i+x][j+y][1];
                                combined[2] += img[i+x][j+y][2];
                            }
                        }
                    }
                    ret[i][j][0] = combined[0]/8;
                    ret[i][j][1] = combined[1]/8;
                    ret[i][j][2] = combined[2]/8;
                } else {
                    ret[i][j][0] = img[i][j][0];
                    ret[i][j][1] = img[i][j][1];
                    ret[i][j][2] = img[i][j][2];
                }

            }
        }
        return ret;
    }

     public float[][][] multiCalc(int n, float border, float[][][] img, int resX, int rexY){
        for (int i = 0; i < n; i++){
            img = calc(border, img, resX, rexY);
        }
        return img;
    }

    @Override
    public float[][][] run(float[][][] img, int resX, int resY, float delta) {
        SmoothOneCon con = screen.getController(SmoothOneCon.class);
        return multiCalc(con.getIt(), con.getBorder(), img, resX, resY);
    }

    @Override
    public String toString(){
        SmoothOneCon con = screen.getController(SmoothOneCon.class);
        return (activ? "ACTIVE : ": "") + "Smooth " + id + ", Iterationen: " + con.getIt();
    }
}
