package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Igor Klimov on 8/11/2015.
 */
public class Controller2 implements Initializable {
    @FXML
    VBox choiceRoot;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void chooseX(Event event) {
        Stage currentStage = (Stage) choiceRoot.getScene().getWindow();
        currentStage.close();
        Controller.playerChar = Controller.Side.X;
        Controller.opponentChar = Controller.Side.O;
        Stage newStage = new Stage();
        try {
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("TicTacToe.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        newStage.setResizable(false);
        newStage.show();
    }

    public void chooseO(Event event) {
        Stage currentStage = (Stage) choiceRoot.getScene().getWindow();
        currentStage.close();
        Controller.playerChar = Controller.Side.O;
        Controller.opponentChar = Controller.Side.X;
        Stage newStage = new Stage();
        try {
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("TicTacToe.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        newStage.setResizable(false);
        newStage.show();
    }
}
