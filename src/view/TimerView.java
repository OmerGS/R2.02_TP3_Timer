// TimerView.java

package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import controller.TimerController;

public class TimerView {
    private TimerController controller;
    private Label timerLabel;
    private Button startStopButton;
    private Button resetButton;
    private VBox root;

    public TimerView() {
        this.controller = new TimerController();
    }

    public void start(Stage primaryStage) {
        // Initialiser les composants de l'interface utilisateur
        this.timerLabel = new Label();
        this.timerLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: darkblue;");
        
        this.startStopButton = new Button("Stop");
        this.resetButton = new Button("Reset");
        this.resetButton.setDisable(true); // Désactiver le bouton de réinitialisation au démarrage
    
        // Ajouter des gestionnaires d'événements aux boutons
        this.startStopButton.setOnAction(event -> {
            controller.toggleTimer();
            updateStartStopButtonText();
            updateResetButtonState();
        });
    
        this.resetButton.setOnAction(event -> {
            controller.resetTimer();
            updateStartStopButtonText();
            updateResetButtonState();
        });
    
        // Créer la disposition de l'interface utilisateur
        VBox buttonPane = new VBox(10);
        buttonPane.getChildren().addAll(startStopButton, resetButton);
        buttonPane.setAlignment(Pos.CENTER); // Aligner les boutons au centre
    
        this.root = new VBox(20);
        this.root.getChildren().addAll(timerLabel, buttonPane);
        this.root.setAlignment(Pos.CENTER); // Centrer le contenu
    
        // Créer la scène et afficher la fenêtre
        StackPane stackPane = new StackPane(root);
        
        Scene scene = new Scene(stackPane, 300, 120);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chronomètre");
        primaryStage.setResizable(false); // Désactiver la redimensionnement de la fenêtre
        primaryStage.show();
    
        // Démarrer le chronomètre
        startTimer();
    }
    

    private void startTimer() {
        controller.startTimer();
        controller.addTimerListener((min, sec) -> {
            String time = String.format("%02d:%02d", min, sec);
            timerLabel.setText(time);
        });
    }

    // Méthode pour mettre à jour le texte du bouton en fonction de l'état du chronomètre
    private void updateStartStopButtonText() {
        if (controller.isRunning()) {
            startStopButton.setText("Stop");
        } else {
            startStopButton.setText("Start");
        }
    }

    // Méthode pour mettre à jour l'état de désactivation du bouton de réinitialisation en fonction de l'état du chronomètre
    private void updateResetButtonState() {
        if (!controller.isRunning()) {
            resetButton.setDisable(false);
        } else {
            resetButton.setDisable(true);
        }
    }
}