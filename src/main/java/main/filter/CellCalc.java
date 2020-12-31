package main.filter;

import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import main.Controller.FilterController.VoronoiCon;
import main.FXMLLoad;
import main.filter.assiClasses.Filter;
import main.filter.assiClasses.PixelSupplier;

import java.util.*;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.stream.Stream;

public class CellCalc extends Filter {

    public CellCalc (ListView view, HBox container){
        screen = new FXMLLoad("/voronoi.fxml", new VoronoiCon(view, screen, this, container));
        id = count++;
    }

    Random random = new Random();

    int sub = 15;

    public float[][][] cell(int n, float[][][] img, int resX, int rexY, int x, int y, int w, int h){
        System.gc();
        System.out.println("Cells " + n);
        int root = (int)Math.min(Math.min(Math.sqrt(n/10)/5, resX/30), rexY/30);
        if (n >= 100000 && resX > 700 && rexY > 700){
            sub = root;
        } else if (n >= 10000){
            sub = 10;
        } else if (n >= 1000){
            sub = 5;
        } else {
            sub = 1;
        }
        System.out.println("Divisions: " + sub);
        //List<int[]>[] cells = new List[n];
        int[][][] cellKeys = new int[resX][rexY][3];
        //AtomicIntegerArray cKeys = new AtomicIntegerArray(rexY*resX);
        float[][] centers = new float[n][6];

        ArrayList<Integer>[][] subdiv = new ArrayList[sub][sub];

        for (int i = 0; i < n; i++){
            centers[i][0] = random.nextFloat()*w+x;
            centers[i][1] = random.nextFloat()*h+y;
            //System.out.println("d;" + centers[i][0]);
            //cells[i] = Collections.synchronizedList(new ArrayList<int[]>());
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
        System.out.println("divided");


        Stream.generate(new PixelSupplier(resX, rexY)).limit(resX*rexY).forEach(pixel -> {
            if (pixel[0] < resX && pixel[1] < rexY) {
                cellKeys[pixel[0]][pixel[1]][0] = pixel[0];
                cellKeys[pixel[0]][pixel[1]][1] = pixel[1];
            }
        });

        Arrays.stream(cellKeys).parallel().flatMap(row -> Arrays.stream(row)).forEach(pixel -> {
            if (pixel[0] < resX && pixel[1] < rexY) {
                float dist = resX + rexY;
                int cell = 0;
                int divX = (int) (pixel[0] / (resX / sub + 1));
                int divY = (int) (pixel[1] / (rexY / sub + 1));

                for (int a : subdiv[divX][divY]) {
                    float tmpDist = (float) Math.sqrt(Math.pow(pixel[0] - centers[a][0], 2) + Math.pow(pixel[1] - centers[a][1], 2));
                    if (tmpDist < dist) {
                        dist = tmpDist;
                        cell = a;
                    }
                }
                //cells[cell].add(new int[]{pixel[0], pixel[1]});
                //cKeys.getAndSet(pixel[0]+pixel[1]*resX, cell);
                pixel[2] = cell;
            }
        });

        System.out.println("celled");


        for (int k = 0; k < resX; k++){
            for (int j = 0; j < rexY; j++) {
                    centers[cellKeys[k][j][2]][2] += img[k][j][0];
                    centers[cellKeys[k][j][2]][3] += img[k][j][1];
                    centers[cellKeys[k][j][2]][4] += img[k][j][2];
                    centers[cellKeys[k][j][2]][5]++;
                //centers[cKeys.get(k+j*resX)][2] += img[k][j][0];
                //centers[cKeys.get(k+j*resX)][3] += img[k][j][1];
                //centers[cKeys.get(k+j*resX)][4] += img[k][j][2];
                //centers[cKeys.get(k+j*resX)][5]++;
            }
        }
        for (int i = 0; i < n; i++) {
            if (centers[i][5] > 0) {
                centers[i][2] /= centers[i][5];
                centers[i][3] /= centers[i][5];
                centers[i][4] /= centers[i][5];
            }
        }


        for (int i = 0; i < resX; i++){
            for (int j = 0; j <rexY; j++){
                img[i][j][0] = centers[cellKeys[i][j][2]][2];
                img[i][j][1] = centers[cellKeys[i][j][2]][3];
                img[i][j][2] = centers[cellKeys[i][j][2]][4];
            }
        }
        System.out.println("pixels");
        System.gc();
        return img;
    }

    @Override
    public float[][][] run(float[][][] img, int resX, int resY, float delta, boolean fullRun) {
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
