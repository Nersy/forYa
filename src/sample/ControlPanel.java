package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;

class ControlPanel extends GridPane {
    static TextField GetIp=new TextField();
    static Button Start=new Button();
    static Button Stop=new Button();
    private String IP;

    ControlPanel(){
        setTranslateX(5);
        setTranslateY(5);

        setAlignment(Pos.CENTER);
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(15, 5, 5, 5));
        setStyle("-fx-border-Color:black");

        add(new Label("Значение / Диапазон значений"),0,0);

        GetIp=new TextField();//172.16.30
        GetIp.setText("172.16.30");
        GetIp.setAlignment(Pos.CENTER);
        GetIp.setOnMouseClicked(event -> GetIp.setStyle("-fx-text-fill:black"));
        GetIp.setOnKeyPressed(event -> {
            GetIp.setStyle("-fx-text-fill:black");
            if(event.getCode()== KeyCode.ENTER&&!Start.isDisabled()){
                InfoBar.clear.setDisable(true);

                IP=GetIp.getText();
                Stop.setDisable(false);
                Window.StopFlag=false;
                Start.setDisable(true);

                Window.Search(IP);
            }
        });
        add(GetIp,0,1);

        GridPane ButtonPanel=new GridPane();
        ButtonPanel.setTranslateX(5);
        ButtonPanel.setTranslateY(5);

        ButtonPanel.setAlignment(Pos.CENTER);
        ButtonPanel.setHgap(5);
        ButtonPanel.setVgap(10);
        ButtonPanel.setPadding(new Insets(0,0,15,0));

        Start=new Button("Начать");
        Start.setOnAction(event -> {
            InfoBar.clear.setDisable(true);

            IP=GetIp.getText();

            if(IP.equals("")){
                GetIp.setStyle("-fx-text-fill:red");
                GetIp.requestFocus();
            }else{
                Window.StopFlag=false;
                Stop.setDisable(false);
                Start.setDisable(true);

                Window.Search(IP);
            }
        });

        Stop=new Button("Остановить");
        Stop.setDisable(true);
        Stop.setOnAction(event -> {
            InfoBar.clear.setDisable(false);

            Window.StopFlag=true;
            Stop.setDisable(true);
            Start.setDisable(false);
            InfoBar.time.setText(" Сканирование прервано ");
        });

        ButtonPanel.add(Start,0,0);
        ButtonPanel.add(Stop,1,0);

        add(ButtonPanel,0,2);
        setValignment(Start, VPos.CENTER);
    }
}
