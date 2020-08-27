package main.filter;

import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import main.Controller.FilterController.VoronoiCon;
import main.FXMLLoad;
import main.filter.assiClasses.Filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class CellCalc extends Filter {

    public CellCalc (ListView view, HBox container){
        screen = new FXMLLoad("/voronoi.fxml", new VoronoiCon(view, screen, this, container));
        id = count++;
    }

    Random random = new Random();

    int sub = 15;

    public float[][][] cell(int n, float[][][] img, int resX, int rexY, int x, int y, int w, int h){
        System.out.println("n " + n);
        int root = (int)Math.min(Math.min(Math.sqrt(n/10)/5, resX/30), rexY/30);
        System.out.println("r" + root);
        if (n >= 100000 && resX > 700 && rexY > 700){
            sub = root;
        } else if (n >= 10000){
            sub = 10;
        } else if (n >= 1000){
            sub = 5;
        } else {
            sub = 1;
        }
        System.out.println(sub);
        ArrayList<int[]>[] cells = new ArrayList[n];
        float[][] centers = new float[n][5];
        int[][][] toCell = new int[resX][rexY][3];

        ArrayList<Integer>[][] subdiv = new ArrayList[sub][sub];

        for (int i = 0; i < n; i++){
            centers[i][0] = random.nextFloat()*w+x;
            centers[i][1] = random.nextFloat()*h+y;
            //System.out.println("d;" + centers[i][0]);
            cells[i] = new ArrayList<>();
        }

        for (int i = 0; i < sub; i++){
            for (int j = 0; j < sub; j++){
                subdiv[i][j] = new ArrayList<>();
                for (int a = 0; a < n; a++){
                    float dist = (float)Math.sqrt(Math.pow((i+0.5f)*(resX/sub)-centers[a][0], 2) + Math.pow((j+0.5f)*(rexY/sub)-centers[a][1], 2));
                    if (dist < Math.max(resX/sub, rexY/sub)*1.5f){
                        subdiv[i][j].add(a);
                    }
                }
            }
        }

        for (int i = x; i < x+w; i++) {
            for (int j = y; j < y+h; j++) {
                if (i < resX && j < rexY) {
                    toCell[i][j][0] = i;
                    toCell[i][j][1] = j;
                }
            }
        }

        Arrays.stream(toCell).parallel().forEach(p -> Arrays.stream(p).parallel().forEach(pixel -> {
            float dist = resX+rexY;
            int cell = 0;
            int divX = (int)(pixel[0]/(resX/sub +1));
            int divY = (int)(pixel[1]/(rexY/sub +1));

            for (int a: subdiv[divX][divY]){
                float tmpDist = (float)Math.sqrt(Math.pow(pixel[0]-centers[a][0], 2) + Math.pow(pixel[1]-centers[a][1], 2));
                if (tmpDist < dist){
                    dist = tmpDist;
                    cell = a;
                }
            }

            /*for (int a = 0; a < n; a++){
                float tmpDist = (float)Math.sqrt(Math.pow(pixel[0]-centers[a][0], 2) + Math.pow(pixel[1]-centers[a][1], 2));
                if (tmpDist < dist){
                    dist = tmpDist;
                    cell = a;
                }
            }*/
            pixel[2] = cell;

        }));
        Arrays.stream(toCell).forEach(p -> Arrays.stream(p).forEach(pixel -> {
            cells[pixel[2]].add(new int[]{pixel[0], pixel[1]});
        }));

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

        return img;
    }

    @Override
    public float[][][] run(float[][][] img, int resX, int resY, float delta) {
        VoronoiCon con = screen.getController(VoronoiCon.class);
        return cell(con.getCellCount(), img, resX, resY, (int)(con.getXPos()*delta), (int)(con.getYPos()*delta), (int)(con.getW()*delta), (int)(con.getH()*delta));
    }

    static int count = 0;
    int id = -1;

    @Override
    public String toString(){
        return (activ? "ACTIVE : ": "") + "Voronoi " + id + ", Cells: " + screen.getController(VoronoiCon.class).getCellCount();
    }
}
