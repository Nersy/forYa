package sample;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;

class ThreadPanel extends GridPane {
    private static TextField countThread;
    static int correct=5;

    ThreadPanel(){
        getStylesheets().add((getClass().getResource("/res/style.css")).toExternalForm());

        setTranslateX(5);
        setTranslateY(5);

        setAlignment(Pos.CENTER);
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(5, 5, 5, 5));
        setStyle("-fx-border-Color:black");

        Label thread=new Label("Потоки");
        add(thread,0,0);
        setHalignment(thread, HPos.CENTER);

        countThread=new TextField();
        countThread.setPromptText("5 <= x <= 20");
        countThread.getStyleClass().add("prompt");
        countThread.setAlignment(Pos.CENTER);
        countThread.setOnKeyPressed(event -> {
            countThread.setStyle("-fx-text-fill:black");
            if(event.getCode()== KeyCode.ENTER){
                check(countThread.getText());
            }
        });
        add(countThread,0,1);

        Button btThread=new Button("Применить");
        btThread.setOnAction(event -> check(countThread.getText()));
        add(btThread,0,2);
        setHalignment(btThread, HPos.CENTER);
    }

    private static void check(String s){
        int z=Main.parseInt(s);
        if(z<0){
            countThread.setStyle("-fx-text-fill:red");
            return;
        }

        if(z<5||z>20){
            countThread.setText("");
        }else{
            correct=z;
        }
    }


}
