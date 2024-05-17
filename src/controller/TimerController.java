package controller;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import model.TimerModel;

public class TimerController {
    private TimerModel model;
    private List<TimerListener> listeners;
    private boolean isRunning;
    private Timeline timeline;

    public interface TimerListener{
        void onTimeChanged(int min, int sec);
    }

    public TimerController() {
        this.model = new TimerModel();
        this.listeners = new ArrayList<>();
        this.isRunning = false;
    }

    public void startTimer() {
        this.timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            this.model.next();
            notifyListeners();
        }));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.isRunning = true;
        this.timeline.play();
    }
    
    public void stopTimer() {
        this.isRunning = false;
        this.timeline.stop();
    }
    
    public void toggleTimer() {
        if (isRunning) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    public void resetTimer() {
        model.res();
        notifyListeners();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void addTimerListener(TimerListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public void removeTimerListener(TimerListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        int min = model.getMin();
        int sec = model.getSec();
        for (TimerListener listener : listeners) {
            listener.onTimeChanged(min, sec);
        }
    }
}