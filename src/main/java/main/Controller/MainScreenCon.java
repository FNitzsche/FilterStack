package main.Controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import main.filter.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TreeMap;
import java.util.concurrent.*;


public class MainScreenCon {

    @FXML
    TextField path;
    @FXML
    Button load;
    @FXML
    ListView<Filter> filterList;
    @FXML
    Button apply;
    @FXML
    Button up;
    @FXML
    Button down;
    @FXML
    Button add;
    @FXML
    ChoiceBox<String> addType;
    @FXML
    Button save;
    @FXML
    GridPane gridSettings;
    @FXML
    Canvas preview;
    @FXML
    HBox container;
    @FXML
    ProgressIndicator progressC;
    @FXML
    ProgressBar progressB;

    private float[][][] imgprev;


    public void initialize(){
        imgprev = new float[(int)preview.getWidth()][(int)preview.getHeight()][3];

        addType.getItems().add("Voronoi");
        addType.getItems().add("Limit Colors");
        addType.getItems().add("Smooth");
        addType.getItems().add("Smooth2");
        addType.getItems().add("Lines");
        addType.getItems().add("Specific Colors");
        addType.getItems().add("Replace Color");
        addType.getSelectionModel().select(0);

        add.setOnAction(t -> addFilter());
        filterList.setOnMouseClicked(t -> viewSelected());
        filterList.setCellFactory(param -> new FilterCell());
        load.setOnAction(t -> loadImage());
        apply.setOnAction(t -> previewFilters());
        up.setOnAction(t -> moveUp());
        down.setOnAction(t -> moveDown());
        save.setOnAction(t -> saveImage());

        startDrawing();
    }

    private void viewSelected(){
        container.getChildren().clear();
        container.getChildren().add(filterList.getSelectionModel().getSelectedItem().screen.getParent());
    }


    private void addFilter(){
        switch (addType.getValue()){
            case "Voronoi": filterList.getItems().add(new CellCalc(filterList, container));break;
            case "Limit Colors": filterList.getItems().add(new simCalc(filterList, container));break;
            case "Smooth": filterList.getItems().add(new FlatCalc(filterList, container));break;
            case "Smooth2": filterList.getItems().add(new LineSmooth(filterList, container));break;
            case "Lines": filterList.getItems().add(new LineCalc(filterList, container));break;
            case "Specific Colors": filterList.getItems().add(new SpecificColors(filterList, container));break;
            case "Replace Color": filterList.getItems().add(new RepColor(filterList, container));break;
        }
    }

    public Image draw(float[][][] tmp){
        if (tmp.length > 0) {
            int resX = tmp.length;
            int rexY = tmp[0].length;
            WritableImage wimg = new WritableImage(resX, rexY);

            for (int i = 0; i < resX; i++) {
                for (int j = 0; j < rexY; j++) {
                    wimg.getPixelWriter().setColor(i, j, Color.color(tmp[i][j][0], tmp[i][j][1], tmp[i][j][2]));
                }
            }

            return wimg;
        }
        return new WritableImage(0, 0);
    }

    ScheduledExecutorService exe;

    private void startDrawing(){
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Image img = draw(imgprev);
                Platform.runLater(() -> preview.getGraphicsContext2D().drawImage(img, 0, 0));
            }
        };

        exe = Executors.newSingleThreadScheduledExecutor();
        exe.scheduleAtFixedRate(run, 1000, 500, TimeUnit.MILLISECONDS);
    }

    public void shutdown(){
        exe.shutdown();
        exeThread.shutdown();
    }

    Image original;
    Image prevImage;

    private void loadImage(){
        Image tmp = null;
        Image tmpP = null;
        try {
            URL url = new URL("file:" + path.getText());
            tmp = new Image(url.toString());
            tmpP = new Image(url.toString(), preview.getWidth(), preview.getHeight(), true, false);
        } catch (Exception e){
            System.out.println(e);
        }
        if (tmp != null){
            original = tmp;
            prevImage = tmpP;
        }
        imgprev = imgToArray(prevImage);
    }

    public float[][][] imgToArray(Image img){
        float[][][] ret = new float[(int)img.getWidth()][(int)img.getHeight()][3];
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                    ret[i][j][0] = (float) img.getPixelReader().getColor(i, j).getRed();
                    ret[i][j][1] = (float) img.getPixelReader().getColor(i, j).getGreen();
                    ret[i][j][2] = (float) img.getPixelReader().getColor(i, j).getBlue();
            }
        }
        return ret;
    }

    private void previewFilters(){
        Runnable run = new Runnable() {
            @Override
            public void run() {
                imgprev = applyFilters(prevImage, 1);
            }
        };
        exeThread.execute(run);
    }

    ExecutorService exeThread = Executors.newCachedThreadPool();

    private float[][][] applyFilters(Image img, float delta){
        Task<float[][][]> task = new Task<float[][][]>() {
            @Override
            protected float[][][] call() throws Exception {
                System.out.println("start application");
                float[][][] imgArray = imgToArray(img);
                int count = 0;
                Platform.runLater(() -> {
                    progressC.setVisible(true);
                    progressB.setProgress(0);
                });
                for (Filter filter: filterList.getItems()){
                    imgArray = filter.run(imgArray, (int)img.getWidth(), (int)img.getHeight(), delta);
                    count++;
                    int finalCount = count;
                    Platform.runLater(() -> {
                        progressB.setProgress(((float)(finalCount))/filterList.getItems().size());
                    });
                }
                System.out.println("finished: " + count);
                Platform.runLater(() -> {
                    progressC.setVisible(false);
                });
                return imgArray;
            }
        };
        exeThread.execute(task);
        float[][][] ret = new float[0][][];
        try {
            ret = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ;
        return ret;
    }

    private void saveImage(){
        Runnable run = new Runnable() {
            @Override
            public void run() {
                float[][][] tmp = applyFilters(original, (float) original.getHeight()/(float)prevImage.getHeight());
                Image img = draw(tmp);

                File file = new File(path.getText().substring(0, path.getText().lastIndexOf('.')) + "_Bearbeitet.png");
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", file);
                } catch (Exception s) {
                }
            }
        };
        exeThread.execute(run);
    }

    private void moveUp(){
        Filter filter = filterList.getSelectionModel().getSelectedItem();
        if (filter != null && filterList.getItems().indexOf(filter)>0){
            int index = filterList.getItems().indexOf(filter);
            filterList.getItems().set(index, filterList.getItems().get(index-1));
            filterList.getItems().set(index -1, filter);
        }
    }

    private void moveDown(){
        Filter filter = filterList.getSelectionModel().getSelectedItem();
        if (filter != null && filterList.getItems().indexOf(filter)<filterList.getItems().size()-1){
            int index = filterList.getItems().indexOf(filter);
            filterList.getItems().set(index, filterList.getItems().get(index+1));
            filterList.getItems().set(index +1, filter);
        }
    }
}
