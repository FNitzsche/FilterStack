package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.Controller.MainController.MainScreenCon;

public class AppStart extends Application {

    FXMLLoad mainScreen;

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("V_0.1.1");
        mainScreen = new FXMLLoad("/mainScreen.fxml", new MainScreenCon(stage));
        stage.setScene(mainScreen.getScene());
        stage.show();
        stage.setOnCloseRequest(e -> mainScreen.getController(MainScreenCon.class).shutdown());
    }
}
