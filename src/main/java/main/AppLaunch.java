package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.filter.LineSmooth;

import java.util.ArrayList;
import java.util.Random;

public class AppLaunch extends Application {

    Canvas canvas = new Canvas(resX, rexY);

    float[][][] img = new float[resX][rexY][3];

    static final int resX = 1920;
    static final int rexY = 1080;

    Random random = new Random();


    @Override
    public void start(Stage stage) throws Exception {

        stage.setScene(new Scene(new HBox(canvas)));
        stage.show();
        calc3();
        canvas.getGraphicsContext2D().drawImage(draw(), 0, 0);
    }





    public Image draw(){
        WritableImage wimg = new WritableImage(resX, rexY);

        for (int i = 0; i < resX; i++){
            for (int j = 0; j < rexY; j++){
                wimg.getPixelWriter().setColor(i, j, Color.color(img[i][j][0], img[i][j][1], img[i][j][2]));
            }
        }

        return wimg;
    }

    public void calc1(){
        fill1();
        for (int i = 1; i < resX; i++){
            for (int j = 1; j < rexY-1; j++){
                int ran = random.nextInt(10)+1;
                int ran1 = random.nextInt(100)+1;
                int ran2 = random.nextInt(100)+1;
                img[i][j][0] = (img[i-1][j][0]*ran + img[i-1][j-1][0]*ran1 + img[i-1][j+1][0]*ran2)/(ran1+ran2+ran);
                img[i][j][1] = (img[i-1][j][1]*ran + img[i-1][j-1][1]*ran1 + img[i-1][j+1][1]*ran2)/(ran1+ran2+ran);
                img[i][j][2] = (img[i-1][j][2]*ran + img[i-1][j-1][2]*ran1 + img[i-1][j+1][2]*ran2)/(ran1+ran2+ran);
            }
        }
        //CellCalc.cell(4000, img, resX, rexY);
        //simCalc.cell(3, 5, 2, img, resX, rexY);
        img = LineSmooth.multiCalc(3, 6, 0.01f, img, resX, rexY);
    }

    public void fill1(){
        float r = 0, b = 0, g = 0;
        for (int i = 0; i < rexY; i++){
            if (i%20 == 0){
                r = random.nextFloat();
                g = random.nextFloat();
                b = random.nextFloat();
            }
            img[0][i][0] = r;
            img[0][i][1] = g;
            img[0][i][2] = b;
        }
    }

    int n = 5;

    public void calc2(){
        fill2();
        for(int a = 0; a < n; a++) {
            float[][][] tmp = new float[resX][rexY][3];
            for (int i = 1; i < resX - 1; i++) {
                for (int j = 1; j < rexY - 1; j++) {
                    int ran = random.nextInt(10) + 1;
                    int ran1 = random.nextInt(100) + 1;
                    int ran2 = random.nextInt(100) + 1;
                    int ran3 = random.nextInt(100) + 1;
                    int ran4 = random.nextInt(100) + 1;
                    tmp[i][j][0] = (img[i][j][0] * ran + img[i - 1][j - 1][0] * ran1 + img[i - 1][j + 1][0] * ran2 + img[i + 1][j - 1][0] * ran3 + img[i + 1][j + 1][0] * ran4) / (ran1 + ran2 + ran3 + ran4 + ran);
                    tmp[i][j][1] = (img[i][j][1] * ran + img[i - 1][j - 1][1] * ran1 + img[i - 1][j + 1][1] * ran2 + img[i + 1][j - 1][1] * ran3 + img[i + 1][j + 1][1] * ran4) / (ran1 + ran2 + ran3 + ran4 + ran);
                    tmp[i][j][2] = (img[i][j][2] * ran + img[i - 1][j - 1][2] * ran1 + img[i - 1][j + 1][2] * ran2 + img[i + 1][j - 1][2] * ran3 + img[i + 1][j + 1][2] * ran4) / (ran1 + ran2 + ran3 + ran4 + ran);
                }
            }
            img = tmp;
        }
        //CellCalc.cell(10000, img, resX, rexY, 100, 100, 500, 500);
        //simCalc.cell(5, img, resX, rexY);
    }

    public void fill2(){
        float r = 0, b = 0, g = 0;
        for (int i = 0; i < rexY; i++){
            if (i%20 == 0){
                r = random.nextFloat();
                g = random.nextFloat();
                b = random.nextFloat();
            }
            for (int j = 0; j < resX; j++) {
                img[j][i][0] = r;
                img[j][i][1] = g;
                img[j][i][2] = b;
            }
        }
    }

    public void cell(int n){
        ArrayList<int[]>[] cells = new ArrayList[n];
        float[][] centers = new float[n][5];
        for (int i = 0; i < n; i++){
            centers[i][0] = random.nextFloat()*resX;
            centers[i][1] = random.nextFloat()*rexY;
            //System.out.println("d;" + centers[i][0]);
            cells[i] = new ArrayList<>();
        }

        for (int i = 0; i < resX; i++) {
            for (int j = 0; j < rexY; j++) {
                float dist = resX+rexY;
                int cell = 0;
                for (int a = 0; a < n; a++){
                    float tmpDist = (float)Math.sqrt(Math.pow(i-centers[a][0], 2) + Math.pow(j-centers[a][1], 2));
                    //System.out.println(tmpDist);
                    if (tmpDist < dist){
                        dist = tmpDist;
                        cell = a;
                    }
                }
                /*if (dist > 100){
                    img[i][j][1] = 0;
                    img[i][j][2] = 0;
                    img[i][j][0] = 0;
                }*/

                cells[cell].add(new int[]{i, j});
            }
        }

        for (int i = 0; i < n; i++){
            float r = 0, g = 0, b = 0;
            for (int[] pixel: cells[i]){
                r += img[pixel[0]][pixel[1]][0];
                g += img[pixel[0]][pixel[1]][1];
                b += img[pixel[0]][pixel[1]][2];
            }
            if (cells[i].size() > 0){
                r /= cells[i].size();
                g /= cells[i].size();
                b /= cells[i].size();
            }
            centers[i][2] = r;
            centers[i][3] = g;
            centers[i][4] = b;
        }

        for (int i = 0; i < n; i++){
            for (int[] pixel: cells[i]){
                img[pixel[0]][pixel[1]][0] = centers[i][2];
                img[pixel[0]][pixel[1]][1] = centers[i][3];
                img[pixel[0]][pixel[1]][2] = centers[i][4];
            }
        }
    }


    public void calc3(){
        //Image image = new Image("SANY0119.JPG");
        //Image image = new Image("DSC_2921.JPG");
        //Image image = new Image("DSC_3260.JPG");
        //Image image = new Image("DSC_2966.JPG");
        Image image = new Image("Pictures/DSC_3058.JPG");

        for (int i = 0; i < resX; i++) {
            for (int j = 0; j < rexY; j++) {
                if (i < image.getWidth() && j < image.getHeight()) {
                    img[i][j][0] = (float) image.getPixelReader().getColor(i, j).getRed();
                    img[i][j][1] = (float) image.getPixelReader().getColor(i, j).getGreen();
                    img[i][j][2] = (float) image.getPixelReader().getColor(i, j).getBlue();
                }
            }
        }
        /*s = System.currentTimeMillis();
        cell(10000);
        System.out.println("Time serial: " + (System.currentTimeMillis()-s) + "ms");*/

        long s = System.currentTimeMillis();
        //img = LineSmooth.multiCalc(2, 6, 0.005f, img, resX, rexY);
        //simCalc.cell(4, img, resX, rexY);
        //simCalc.cell(4, 6, 6, img, resX, rexY);
        //CellCalc.cell(10000, img, resX, rexY, 100, 100, 500, 500);
        //img = FlatCalc.multiCalc(30, 0.05f, img, resX, rexY);

        //img = LineSmooth.multiCalc(2, 7, 0.002f, img, resX, rexY);
        //CellCalc.cell(100000, img, resX, rexY);
        System.out.println("Time parallel: " + (System.currentTimeMillis()-s) + "ms");
    }
}
