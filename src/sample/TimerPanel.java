package sample;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;

class TimerPanel extends GridPane{
    private static TextField MyTime;
    static int correct=1;

    TimerPanel(){
        getStylesheets().add((getClass().getResource("/res/style.css")).toExternalForm());

        setTranslateX(5);
        setTranslateY(5);

        setAlignment(Pos.CENTER);
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(5, 5, 5, 5));
        setStyle("-fx-border-Color:black");

        Label tm=new Label("Таймер (мин)");
        add(tm,0,0);
        setHalignment(tm, HPos.CENTER);

        MyTime=new TextField();
        MyTime.setPromptText("x >= 1");
        MyTime.getStyleClass().add("prompt");
        MyTime.setAlignment(Pos.CENTER);
        MyTime.setOnMouseClicked(event -> MyTime.setStyle("-fx-text-fill:black"));
        MyTime.setOnKeyPressed(event -> {
            MyTime.setStyle("-fx-text-fill:black");
            if(event.getCode()== KeyCode.ENTER){
                check(MyTime.getText());
            }
        });
        add(MyTime,0,1);

        Button btTimer=new Button("Установить");
        btTimer.setOnMouseClicked(event -> check(MyTime.getText()));
        add(btTimer,0,2);
        setHalignment(btTimer, HPos.CENTER);
    }

    private static void check(String s){
        int z=Main.parseInt(s);
        if(z<0){
            MyTime.setStyle("-fx-text-fill:red");
            return;
        }

        if(z<1){
            MyTime.setText("");
        }else{
            correct=z;
        }
    }
}
