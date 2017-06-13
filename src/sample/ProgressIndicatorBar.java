package sample;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

class ProgressIndicatorBar extends StackPane {
    private static ProgressBar bar=new ProgressBar(0);
    private static Text text=new Text();

    ProgressIndicatorBar(){
        setPrefSize(327,19);
        setMaxSize(327,19);

        text.setText("0.0%");
        text.setTranslateY(-1);

        bar.setPrefSize(327,19);
        bar.setMaxSize(327,19);
        bar.getStyleClass().add("progress-bar");

        Timeline task = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(bar.progressProperty(), 0)
                ),
                new KeyFrame(
                        Duration.seconds(2),
                        new KeyValue(bar.progressProperty(), 1)
                )
        );

        getChildren().setAll(bar, text);
    }

    static void setProgress(double value){
        Platform.runLater(() -> bar.setProgress(value));
        Platform.runLater(() -> text.setText(value*100+"%"));
    }
    static void setProgress(){
        Platform.runLater(() -> bar.setProgress(InfoBar.count/100));
        Platform.runLater(() -> text.setText(InfoBar.correctCounter()+"%"));
    }
}
