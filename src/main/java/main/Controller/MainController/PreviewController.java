package main.Controller.MainController;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import model.BaseImage;
import model.FilterStack;
import model.Viewable;

import javax.imageio.ImageIO;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class PreviewController {

    ListView<Viewable> previewStackList;
    Button save;
    Button apply;
    ProgressIndicator progressC;
    ProgressBar progressB;
    Canvas preview;

    ObservableList<Viewable> viewables = FXCollections.observableList(new ArrayList<>());

    public PreviewController(ListView<Viewable> previewStackList, Button save, Button apply,
            ProgressIndicator progressC, ProgressBar progressB, Canvas preview){

        this.previewStackList = previewStackList;
        this.save = save;
        this.apply = apply;
        this.progressC = progressC;
        this.progressB = progressB;
        this.preview = preview;

        initList();
        apply.setOnAction(t -> previewFilters());
        save.setOnAction(t -> saveImage());
        startDrawing();

    }

    private void startDrawing(){
        Runnable run = new Runnable() {
            @Override
            public void run() {
                //System.out.println("draw");
                float precent = 1;
                if (previewStackList.getSelectionModel().getSelectedItem() != null) {
                    //System.out.println("selection found");
                    Image img = previewStackList.getSelectionModel().getSelectedItem().showImage(false, (int)preview.getWidth(), (int)preview.getHeight(), 1);
                    if (img != null) {
                        Platform.runLater(() -> preview.getGraphicsContext2D().drawImage(img, 0, 0));
                        //System.out.println("finished drawing");
                    }
                    if (previewStackList.getSelectionModel().getSelectedItem().getClass().equals(FilterStack.class)){

                                precent = 0;
                        for (FilterStack stack: ((FilterStack) previewStackList.getSelectionModel().getSelectedItem()).containing) {
                            precent += stack.filtersFinished/(((FilterStack) previewStackList.getSelectionModel().getSelectedItem()).containing.size()+1);
                        }
                        precent += ((FilterStack) previewStackList.getSelectionModel().getSelectedItem()).filtersFinished/(((FilterStack) previewStackList.getSelectionModel().getSelectedItem()).containing.size()+1);
                    }
                }
                float finalPrecent = precent;
                Platform.runLater(() -> progressC.setVisible(finalPrecent <1? true:false));
                Platform.runLater(() -> progressB.setProgress(finalPrecent));
            }
        };
        MainScreenCon.exe.scheduleAtFixedRate(run, 1000, 500, TimeUnit.MILLISECONDS);
    }

    private void previewFilters() {
        if (previewStackList.getSelectionModel().getSelectedItem() != null && previewStackList.getSelectionModel().getSelectedItem().getClass().equals(FilterStack.class)){
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    System.out.println("start app");
                    FilterStack.resetRan();
                    ((FilterStack) previewStackList.getSelectionModel().getSelectedItem()).runFilters(false, (int)preview.getWidth(), (int)preview.getHeight(), 1);
                    System.gc();
                    System.out.println("finished app");
                }
            };
            MainScreenCon.exeThread.execute(run);
        } else {
            Platform.runLater(new Runnable() {
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No Image");
                    alert.setHeaderText("select a FilterStack");
                    alert.showAndWait();
                }
            });
        }
    }

    private void saveImage(){
        if (previewStackList.getSelectionModel().getSelectedItem() != null) {
            final String p = MainScreenCon.fileChooser.showSaveDialog(MainScreenCon.stage).getAbsolutePath();
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    System.out.println("start saving");
                    BaseImage img;
                    float[][][] fullSizeImg;
                    if (previewStackList.getSelectionModel().getSelectedItem().getClass().equals(FilterStack.class)){
                        img = ((FilterStack) previewStackList.getSelectionModel().getSelectedItem()).baseImage;
                        FilterStack.resetRan();
                        fullSizeImg = img.getFullSize();
                        ((FilterStack) previewStackList.getSelectionModel().getSelectedItem()).runFilters(true, (int)fullSizeImg.length, (int)fullSizeImg[0].length
                                , (float) fullSizeImg[0].length / (float) preview.getHeight());
                    } else {
                        img = (BaseImage) previewStackList.getSelectionModel().getSelectedItem();
                        fullSizeImg = img.getFullSize();
                    }
                    Image image = previewStackList.getSelectionModel().getSelectedItem().showImage(true, (int)fullSizeImg.length, (int)fullSizeImg[0].length
                            , (float) fullSizeImg[0].length / (float) preview.getHeight());

                    File file = new File(p + ".png");
                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                    } catch (Exception s) {
                        System.out.println(s);
                    }
                    System.gc();
                    System.out.println("finished saving");
                }
            };
            MainScreenCon.exeThread.execute(run);
        } else {
            Platform.runLater(new Runnable() {
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No Image");
                    alert.setHeaderText("load or select Image first");
                    alert.showAndWait();
                }
            });
        }
    }

    private void initList(){
        FilterStack.stacks.addListener((ListChangeListener<? super FilterStack>) (c) -> updateList());
        BaseImage.bases.addListener((ListChangeListener<? super BaseImage>) c -> updateList());
        previewStackList.setItems(viewables);
    }

    private void updateList(){
        viewables.clear();
        viewables.addAll(FilterStack.stacks);
        viewables.addAll(BaseImage.bases);
    }
}
