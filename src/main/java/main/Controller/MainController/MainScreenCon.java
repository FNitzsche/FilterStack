package main.Controller.MainController;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
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
import model.Viewable;
import scala.concurrent.impl.FutureConvertersImpl;

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
    ListView<Viewable> previewStackList;
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

    static ScheduledExecutorService exe = Executors.newSingleThreadScheduledExecutor();
    static ExecutorService exeThread = Executors.newCachedThreadPool();

    LoadController loadController;
    SettingsController settingsController;
    PreviewController previewController;

    private float[][][] imgprev;
    static FileChooser fileChooser = new FileChooser();
    static Stage stage;

    public MainScreenCon(Stage stage){
        this.stage = stage;
    }


    public void initialize(){

        loadController = new LoadController(path, search, load, preview, baseImage);
        settingsController = new SettingsController(container, stackName, baseImage, settingsStackList, addStack, deleteStack, filterList, up, down, removeFilter, actFilter, add, addType);
        previewController = new PreviewController(previewStackList, save, apply, progressC, progressB, preview);
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
        addType.getItems().add("Merge");
        addType.getSelectionModel().select(0);
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
                    imgArray = filter.runRun(imgArray, (int)img.getWidth(), (int)img.getHeight(), delta, false);
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


}
