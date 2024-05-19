package view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import controller.TimerController;

public class TimerView extends Application {
    private TimerController controller;
    private Label timerLabel;
    private Button startStopButton;
    private Button resetButton;
    private VBox root;
    private Canvas canvas;
    private Timeline timeline;

    public TimerView() {
        this.controller = new TimerController();
    }

    @Override
    public void start(Stage primaryStage) {

        //Label
        this.timerLabel = new Label();
        this.timerLabel.setFont(new Font("Arial", 48));
        this.timerLabel.setTextFill(Color.BLACK); 


        
        //Bouton

        this.startStopButton = new Button("Stop");
        this.resetButton = new Button("Reset");
        this.resetButton.setDisable(true);
        
        String buttonStyle = "-fx-background-color: #f0f0f0; -fx-border-color: #d3d3d3; "
                           + "-fx-border-radius: 15; -fx-background-radius: 15; "
                           + "-fx-padding: 10 20; -fx-font-size: 14px;";
        this.startStopButton.setStyle(buttonStyle);
        this.resetButton.setStyle(buttonStyle);



        
        this.canvas = new Canvas(200, 200);
        



        this.startStopButton.setOnAction(event -> {
            if (controller.isRunning()) {
                controller.stopTimer();
            } else {
                controller.startTimer();
            }
            updateStartStopButtonText();
            updateResetButtonState();
        });

        
        this.resetButton.setOnAction(event -> {
            controller.resetTimer();
            updateStartStopButtonText();
            updateResetButtonState();
            drawArc();
        });

    

        HBox buttonPane = new HBox(10);
        buttonPane.getChildren().addAll(startStopButton, resetButton);
        buttonPane.setAlignment(Pos.CENTER);
    
        StackPane canvasPane = new StackPane();
        canvasPane.getChildren().addAll(canvas, timerLabel);
        canvasPane.setAlignment(Pos.CENTER);

        this.root = new VBox(20);
        this.root.getChildren().addAll(canvasPane, buttonPane);
        this.root.setAlignment(Pos.CENTER); 
        this.root.setStyle("-fx-background-color: #d4f0d7;");
    


        StackPane stackPane = new StackPane(root);
        Scene scene = new Scene(stackPane, 300, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chronomètre");
        primaryStage.setResizable(false);
        primaryStage.show();
    

        startTimer();
    }


    private void startTimer() {
        controller.startTimer();
        controller.addTimerListener((min, sec) -> {
            String time = String.format("%02d:%02d", min, sec);
            timerLabel.setText(time);
        });


        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> drawArc()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    
    public void drawArc() {
        int seconds = controller.getSeconds();
        double angle = (seconds / 60.0) * 360.0;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Dessiner le cercle noir
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(10);
        gc.strokeArc(20, 20, 160, 160, 90, 360, ArcType.OPEN);

        // Dessiner le cercle rouge des secondes
        gc.setStroke(Color.RED);
        gc.setLineWidth(10);
        gc.strokeArc(20, 20, 160, 160, 90, -angle, ArcType.OPEN);
    }


    public void updateStartStopButtonText() {
        if (controller.isRunning()) {
            startStopButton.setText("Stop");
            timeline.play();
        } else {
            startStopButton.setText("Start");
            timeline.pause();
        }
    }

    public void updateResetButtonState() {
        resetButton.setDisable(controller.isRunning());
    }

    public static void main(String[] args) {
        launch(args);
    }
}