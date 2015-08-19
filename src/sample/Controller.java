package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    GridPane root;
    @FXML
    private Region region00;
    @FXML
    private Region region01;
    @FXML
    private Region region02;
    @FXML
    private Region region10;
    @FXML
    private Region region11;
    @FXML
    private Region region12;
    @FXML
    private Region region20;
    @FXML
    private Region region21;
    @FXML
    private Region region22;

    static RegionData regionData = new RegionData();
    private boolean[][] isTaken = new boolean[3][3];
    private Side[][] field = new Side[3][3];
    static Side playerChar;
    static Side opponentChar;
    private Line line;
    private Button restart;
    private static Circle greyCircle;
    private static Label greyX;
    private boolean playersTurn = true;
    private boolean done = false;
    private int turnCount = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        greyCircle = new Circle(47, Color.TRANSPARENT);
        greyCircle.setStrokeWidth(10);
        greyCircle.setStroke(Color.web("#808080", 0.5));
        greyCircle.setOnMouseClicked((e) -> drawCharacter());
        GridPane.setHalignment(greyCircle, HPos.CENTER);
        GridPane.setValignment(greyCircle, VPos.CENTER);

        greyX = new Label("X");
        greyX.setFont(Font.font("Arial", 120));
        greyX.setTextFill(Color.web("#808080", 0.5));
        greyX.setOnMouseClicked((e) -> drawCharacter());
        GridPane.setHalignment(greyX, HPos.CENTER);
        GridPane.setValignment(greyX, VPos.CENTER);

        line = new Line(0, 0, 0, 0);
        line.setStroke(Color.web("#f75d11"));
        line.setStrokeLineCap(StrokeLineCap.ROUND);
        GridPane.setHalignment(line, HPos.CENTER);
        line.setStrokeWidth(11);

        restart = new Button("Restart");
        restart.setId("restart");
        restart.setPrefHeight(35);
        restart.setPrefWidth(72);
        GridPane.setHalignment(restart, HPos.CENTER);
        GridPane.setValignment(restart, VPos.CENTER);
        GridPane.setRowIndex(restart, 2);
        GridPane.setColumnIndex(restart, 2);
        restart.setOnMouseClicked((e) -> {
            try {
                root.getScene().setRoot(FXMLLoader.load(getClass().getResource("TicTacToe.fxml")));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    public void showCharacter(MouseEvent me) {
        if (playersTurn && !done) {
            root.getChildren().removeAll(greyCircle, greyX);
            Object source = me.getSource();
            checkSource(source);
        }
    }

    private void checkSource(Object source) {
        if (source == region00) {
            showCharacter(0, 0);
        }
        if (source == region01) {
            showCharacter(0, 1);
        }
        if (source == region02) {
            showCharacter(0, 2);
        }
        if (source == region10) {
            showCharacter(1, 0);
        }
        if (source == region11) {
            showCharacter(1, 1);
        }
        if (source == region12) {
            showCharacter(1, 2);
        }
        if (source == region20) {
            showCharacter(2, 0);
        }
        if (source == region21) {
            showCharacter(2, 1);
        }
        if (source == region22) {
            showCharacter(2, 2);
        }
    }

    private void showCharacter(int row, int col) {
        regionData.setRow(row);
        regionData.setCol(col);
        if (!isTaken[regionData.getRow()][regionData.getCol()]) {
            root.getChildren().add(playerChar.createGrey(row, col));
        }
    }

    public void drawCharacter() {
        if (playersTurn && !done) {
            playerDraws();
        } else if (!done) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            opponentDraws();
        }
    }

    private void opponentsTurn() {
        drawCharacter();
    }

    private void playerDraws() {
        int row = regionData.getRow();
        int col = regionData.getCol();
        if (!isTaken[row][col]) {
            root.getChildren().removeAll(greyCircle, greyX);

            root.getChildren().add(playerChar.createChar(row, col));
            isTaken[row][col] = true;
            field[row][col] = playerChar;
            playersTurn = false;
            turnCount++;
            checkVictory();
            if (!done) {
                opponentsTurn();
            }
        }
    }

    private void opponentDraws() {
        AI ai = new AI();
        ai.makeDecision();
        int row = ai.getRow();
        int col = ai.getCol();
        root.getChildren().add(opponentChar.createChar(row, col));
        isTaken[row][col] = true;
        field[row][col] = opponentChar;
        playersTurn = true;
        turnCount++;
        checkVictory();
    }

    private void checkVictory() {
        if (field[0][0] != null) {
            if (field[0][0] == field[0][1] && field[0][1] == field[0][2]) {
                drawHLine(0);
                checkWinner(0, 0, false);
            }
        }
        if (field[1][0] != null) {
            if (field[1][0] == field[1][1] && field[1][1] == field[1][2]) {
                drawHLine(1);
                checkWinner(1, 0, false);
            }
        }
        if (field[2][0] != null) {
            if (field[2][0] == field[2][1] && field[2][1] == field[2][2]) {
                drawHLine(2);
                checkWinner(2, 0, false);
            }
        }
        if (field[0][0] != null) {
            if (field[0][0] == field[1][0] && field[1][0] == field[2][0]) {
                drawVLine(0);
                checkWinner(0, 0, false);
            }
        }
        if (field[0][1] != null) {
            if (field[0][1] == field[1][1] && field[1][1] == field[2][1]) {
                drawVLine(1);
                checkWinner(0, 1, false);
            }
        }
        if (field[0][2] != null) {
            if (field[0][2] == field[1][2] && field[1][2] == field[2][2]) {
                drawVLine(2);
                checkWinner(0, 2, false);
            }
        }
        if (field[0][0] != null) {
            if (field[0][0] == field[1][1] && field[1][1] == field[2][2]) {
                drawDLine(false);
                checkWinner(0, 0, false);
            }
        }
        if (field[0][2] != null) {
            if (field[0][2] == field[1][1] && field[1][1] == field[2][0]) {
                drawDLine(true);
                checkWinner(0, 2, false);
            }
        }
        if (turnCount == 9 && !done) {
            checkWinner(0, 0, true);
        }
    }

    private void drawDLine(boolean forward) {
        line.setStartY(forward ? 367 : -367);
        line.setEndX(369);
        GridPane.setColumnIndex(line, 1);
        GridPane.setRowIndex(line, 1);
        root.getChildren().add(line);
    }

    private void drawVLine(int col) {
        line.setEndY(374);
        GridPane.setRowIndex(line, 1);
        GridPane.setColumnIndex(line, col);
        root.getChildren().add(line);
    }

    private void drawHLine(int row) {
        line.setEndX(374);
        GridPane.setColumnIndex(line, 1);
        GridPane.setRowIndex(line, row);
        root.getChildren().add(line);
    }

    private void checkWinner(int row, int col, boolean draw) {
        done = true;
        Effect blur = new GaussianBlur(7);
        for (Node node : root.getChildren()) {
            node.setEffect(blur);
        }
        Label label = new Label();
        if (draw) {
            label.setText("Draw");
            label.setTextFill(Color.GREY);
        } else if (field[row][col] == playerChar) {
            label.setText("You Win!");
            label.setTextFill(Color.GREEN);
        } else {
            label.setText("You Lose!");
            label.setTextFill(Color.FIREBRICK);
        }
        label.setFont(Font.font("Poor Richard", 90));
        label.prefWidth(353);
        GridPane.setColumnIndex(label, 0);
        GridPane.setRowIndex(label, 1);
        GridPane.setColumnSpan(label, 3);
        GridPane.setHalignment(label, HPos.CENTER);

        root.getChildren().addAll(label, restart);
    }

    private static class RegionData {
        private int row;
        private int col;

        public RegionData() {
        }

        public RegionData(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public void setCol(int col) {
            this.col = col;
        }
    }

    enum Side {
        O {
            @Override
            Node createGrey(int row, int col) {
                GridPane.setRowIndex(greyCircle, row);
                GridPane.setColumnIndex(greyCircle, col);
                return greyCircle;
            }

            @Override
            Node createChar(int row, int col) {
                Circle circle = new Circle(47, Color.TRANSPARENT);
                circle.setStrokeWidth(10);
                circle.setStroke(Color.TURQUOISE);
                GridPane.setHalignment(circle, HPos.CENTER);
                GridPane.setValignment(circle, VPos.CENTER);
                GridPane.setRowIndex(circle, row);
                GridPane.setColumnIndex(circle, col);
                return circle;
            }
        },
        X {
            @Override
            Node createGrey(int row, int col) {
                GridPane.setRowIndex(greyX, row);
                GridPane.setColumnIndex(greyX, col);
                return greyX;
            }

            @Override
            Node createChar(int row, int col) {
                Label X = new Label("X");
                X.setFont(Font.font("Arial", 120));
                X.setTextFill(Color.web("#27d38b"));
                GridPane.setHalignment(X, HPos.CENTER);
                GridPane.setValignment(X, VPos.CENTER);
                GridPane.setRowIndex(X, row);
                GridPane.setColumnIndex(X, col);
                return X;
            }
        };

        abstract Node createGrey(int row, int col);

        abstract Node createChar(int row, int col);
    }

    private class AI {
        private RegionData opponentsChoice = new RegionData();
        private Random random = new Random();

        private void makeDecision() {
            if (isCenterEmpty()) {
                return;
            }

            if (opponentIsCloseToWin()) {
                return;
            }

            if (playerIsCloseToWin()) {
                return;
            }

            if (opponentHasOneChar()) {
                return;
            }
            chooseRandom();
        }

        private boolean isCenterEmpty() {
            if (!isTaken[1][1]) {
                opponentsChoice.setRow(1);
                opponentsChoice.setCol(1);
                return true;
            }
            return false;
        }

        private boolean playerIsCloseToWin() {
            if (hasTwoCharsInLine(0, 0, 0, 1, 0, 2, playerChar)) {
                return true;
            }
            if (hasTwoCharsInLine(1, 0, 1, 1, 1, 2, playerChar)) {
                return true;
            }
            if (hasTwoCharsInLine(2, 0, 2, 1, 2, 2, playerChar)) {
                return true;
            }
            if (hasTwoCharsInLine(0, 0, 1, 0, 2, 0, playerChar)) {
                return true;
            }
            if (hasTwoCharsInLine(0, 1, 1, 1, 2, 1, playerChar)) {
                return true;
            }
            if (hasTwoCharsInLine(0, 2, 1, 2, 2, 2, playerChar)) {
                return true;
            }
            if (hasTwoCharsInLine(0, 0, 1, 1, 2, 2, playerChar)) {
                return true;
            }
            if (hasTwoCharsInLine(0, 2, 1, 1, 2, 0, playerChar)) {
                return true;
            }
            return false;
        }

        private boolean opponentIsCloseToWin() {
            if (hasTwoCharsInLine(0, 0, 0, 1, 0, 2, opponentChar)) {
                return true;
            }
            if (hasTwoCharsInLine(1, 0, 1, 1, 1, 2, opponentChar)) {
                return true;
            }
            if (hasTwoCharsInLine(2, 0, 2, 1, 2, 2, opponentChar)) {
                return true;
            }
            if (hasTwoCharsInLine(0, 0, 1, 0, 2, 0, opponentChar)) {
                return true;
            }
            if (hasTwoCharsInLine(0, 1, 1, 1, 2, 1, opponentChar)) {
                return true;
            }
            if (hasTwoCharsInLine(0, 2, 1, 2, 2, 2, opponentChar)) {
                return true;
            }
            if (hasTwoCharsInLine(0, 0, 1, 1, 2, 2, opponentChar)) {
                return true;
            }
            if (hasTwoCharsInLine(0, 2, 1, 1, 2, 0, opponentChar)) {
                return true;
            }
            return false;
        }

        private boolean hasTwoCharsInLine(int r1, int c1, int r2, int c2, int r3, int c3, Side side) {
            if (field[r1][c1] == side && field[r2][c2] == side && !isTaken[r3][c3]) {
                opponentsChoice.row = r3;
                opponentsChoice.col = c3;
                return true;
            }
            if (field[r1][c1] == side && field[r3][c3] == side && !isTaken[r2][c2]) {
                opponentsChoice.row = r2;
                opponentsChoice.col = c2;
                return true;
            }
            if (field[r2][c2] == side && field[r3][c3] == side && !isTaken[r1][c1]) {
                opponentsChoice.row = r1;
                opponentsChoice.col = c1;
                return true;
            }
            return false;
        }

        private boolean opponentHasOneChar() {
            if (opponentHasOneChar(0, 0, 0, 1, 0, 2)) {
                return true;
            }
            if (opponentHasOneChar(1, 0, 1, 1, 1, 2)) {
                return true;
            }
            if (opponentHasOneChar(2, 0, 2, 1, 2, 2)) {
                return true;
            }
            if (opponentHasOneChar(0, 0, 1, 0, 2, 0)) {
                return true;
            }
            if (opponentHasOneChar(0, 1, 1, 1, 2, 1)) {
                return true;
            }
            if (opponentHasOneChar(0, 2, 1, 2, 2, 2)) {
                return true;
            }
            if (opponentHasOneChar(0, 0, 1, 1, 2, 2)) {
                return true;
            }
            if (opponentHasOneChar(0, 2, 1, 1, 2, 0)) {
                return true;
            }
            return false;
        }

        private boolean opponentHasOneChar(int r1, int c1, int r2, int c2, int r3, int c3) {
            if (field[r1][c1] == opponentChar && !isTaken[r2][c2] && !isTaken[r3][c3]) {
                opponentsChoice.row = r3;
                opponentsChoice.col = c3;
                return true;
            }
            if (field[r2][c2] == opponentChar && !isTaken[r1][c1] && !isTaken[r3][c3]) {
                opponentsChoice.row = r1;
                opponentsChoice.col = c1;
                return true;
            }
            if (field[r3][c3] == opponentChar && !isTaken[r1][c1] && !isTaken[r2][c2]) {
                opponentsChoice.row = r1;
                opponentsChoice.col = c1;
                return true;
            }
            return false;
        }

        private void chooseRandom() {
            while (true) {
                int r = random.nextInt(3);
                int c = random.nextInt(3);
                if (!isTaken[r][c]) {
                    opponentsChoice.setRow(r);
                    opponentsChoice.setCol(c);
                    break;
                }
            }
        }

        public int getRow() {
            return opponentsChoice.getRow();
        }

        public int getCol() {
            return opponentsChoice.getCol();
        }

    }
}
