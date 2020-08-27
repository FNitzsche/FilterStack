package main.Controller.MainController;

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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.filter.*;
import main.filter.assiClasses.Filter;
import model.BaseImage;
import model.FilterStack;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.URL;
import java.util.concurrent.*;


public class MainScreenCon {

    //Load
    @FXML
    TextField path;
    @FXML
    Button load;
    @FXML
    Button search;

    //Settings
    @FXML
    TextField stackName;
    @FXML
    ListView<Filter> filterList;
    @FXML
    Button removeFilter;
    @FXML
    ToggleButton actFilter;
    @FXML
    Button up;
    @FXML
    Button down;
    @FXML
    Button add;
    @FXML
    ChoiceBox<String> addType;
    @FXML
    GridPane gridSettings;
    @FXML
    ChoiceBox<BaseImage> baseImage;
    @FXML
    ListView<FilterStack> settingsStackList;
    @FXML
    Button addStack;
    @FXML
    Button deleteStack;

    //Preview
    @FXML
    Button save;
    @FXML
    Button apply;
    @FXML
    Canvas preview;
    @FXML
    HBox container;
    @FXML
    ProgressIndicator progressC;
    @FXML
    ProgressBar progressB;

    static ExecutorService exeThread = Executors.newCachedThreadPool();

    LoadController loadController;
    SettingsController settingsController;

    private float[][][] imgprev;
    static FileChooser fileChooser = new FileChooser();
    static Stage stage;

    public MainScreenCon(Stage stage){
        this.stage = stage;
    }


    public void initialize(){

        loadController = new LoadController(path, search, load, preview, baseImage);
        settingsController = new SettingsController(container, stackName, baseImage, settingsStackList, addStack, deleteStack, filterList, up, down, removeFilter, actFilter, add, addType);


        imgprev = new float[(int)preview.getWidth()][(int)preview.getHeight()][3];

        addType.getItems().add("Voronoi");
        addType.getItems().add("Limit Colors");
        addType.getItems().add("Smooth");
        addType.getItems().add("Smooth2");
        addType.getItems().add("Lines");
        addType.getItems().add("Specific Colors");
        addType.getItems().add("Replace Color");
        addType.getItems().add("Saturation&Contrast");
        addType.getItems().add("Hue-Keep");
        addType.getSelectionModel().select(0);


        apply.setOnAction(t -> previewFilters());
        save.setOnAction(t -> saveImage());

        removeFilter.setOnAction(t -> filterList.getSelectionModel().getSelectedItem().deleteFilter());
        actFilter.setOnAction(t -> filterList.getSelectionModel().getSelectedItem().activ = actFilter.isSelected());

        startDrawing();
    }

    public Image draw(float[][][] tmp){
        if (tmp!= null && tmp.length > 0) {
            int resX = tmp.length;
            int rexY = tmp[0].length;
            WritableImage wimg = new WritableImage(resX, rexY);

            for (int i = 0; i < resX; i++) {
                for (int j = 0; j < rexY; j++) {
                    wimg.getPixelWriter().setColor(i, j, Color.color(Math.max(0, Math.min(1, tmp[i][j][0])),Math.max(0, Math.min(1, tmp[i][j][1])), Math.max(0, Math.min(1,tmp[i][j][2]))));
                }
            }

            return wimg;
        } else {
            System.out.println("no Image");
        }
        return null;
    }

    ScheduledExecutorService exe;

    private void startDrawing(){
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Image img = draw(imgprev);
                if (img != null) {
                    Platform.runLater(() -> preview.getGraphicsContext2D().drawImage(img, 0, 0));
                }
                Platform.runLater(() -> filterList.refresh());
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

    private void previewFilters() {
        if (prevImage != null){
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    imgprev = applyFilters(prevImage, 1);

                }
            };
            exeThread.execute(run);
        } else {
            Platform.runLater(new Runnable() {
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No Image");
                    alert.setHeaderText("load Image first");
                    alert.showAndWait();
                }
            });
        }
    }

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
                    imgArray = filter.runRun(imgArray, (int)img.getWidth(), (int)img.getHeight(), delta);
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
        if (original != null) {
            final String p = fileChooser.showSaveDialog(stage).getAbsolutePath();
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    float[][][] tmp = applyFilters(original, (float) original.getHeight() / (float) prevImage.getHeight());
                    Image img = draw(tmp);

                    File file = new File(p + ".png");
                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", file);
                    } catch (Exception s) {
                    }
                }
            };
            exeThread.execute(run);
        } else {
            Platform.runLater(new Runnable() {
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No Image");
                    alert.setHeaderText("load Image first");
                    alert.showAndWait();
                }
            });
        }
    }
}