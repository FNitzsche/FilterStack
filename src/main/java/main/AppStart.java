package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.Controller.MainScreenCon;

public class AppStart extends Application {

    FXMLLoad mainScreen;

    @Override
    public void start(Stage stage) throws Exception {
        mainScreen = new FXMLLoad("/mainScreen.fxml", new MainScreenCon());
        stage.setScene(mainScreen.getScene());
        stage.show();
        stage.setOnCloseRequest(e -> mainScreen.getController(MainScreenCon.class).shutdown());
    }
}
