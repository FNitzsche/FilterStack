package main.Controller.MainController;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import model.BaseImage;

import java.net.URL;
import java.sql.BatchUpdateException;

public class LoadController {

    TextField path;
    Button search;
    Button load;
    Canvas preview;
    ChoiceBox<BaseImage> bases;

    public LoadController(TextField path, Button search, Button load, Canvas preview){
        this.path = path;
        this.search = search;
        this.load = load;
        this.preview = preview;
        load.setOnAction(t -> loadImage());
        search.setOnAction(t -> {
            path.setText(MainScreenCon.fileChooser.showOpenDialog(MainScreenCon.stage).getAbsolutePath());
            loadImage();
        });
    }

    private void loadImage(){
        BaseImage baseImage = new BaseImage(path.getText(), (int)preview.getWidth(), (int)preview.getHeight());
        if (baseImage.loaded) {
            bases.getItems().add(baseImage);
        }
    }

}
