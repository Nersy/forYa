package sample;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import java.sql.SQLException;

class FilterPanel extends FlowPane {

    FilterPanel(){
        setTranslateX(5);
        setTranslateY(5);
        setPrefSize(450,100);
        setMaxSize(450,100);

        //-----первая таблица-----
        GridPane First=new GridPane();
        First.setAlignment(Pos.CENTER);
        First.setPrefHeight(125);
        First.setHgap(10);
        First.setVgap(10);
        First.setPadding(new Insets(-3, 5, 5, 5));
        First.setStyle("-fx-border-Color:black");

        TextField address=new TextField();//поле для адреса
        address.setOnKeyPressed(event -> {
            if(event.getCode()==KeyCode.ENTER){
                preparation();
                try {
                    DBWork.lastQuery="outStat-"+address.getText();
                    DBWork.outStat(address.getText());
                } catch (SQLException e) {
                    System.out.println("FilterPanel_getStatIP :: "+e.getMessage());
                }
            }
        });
        address.setAlignment(Pos.CENTER);
        First.add(address,0,0);
        First.setHalignment(address, HPos.CENTER);

        Button getStatIP=new Button("Статистика по адресу");//вывод статистики по адресу
        getStatIP.setOnMouseClicked(event -> {
            preparation();
            try {
                DBWork.lastQuery="outStat-"+address.getText();
                DBWork.outStat(address.getText());
            } catch (SQLException e) {
                System.out.println("FilterPanel_getStatIP :: "+e.getMessage());
            }
        });
        First.add(getStatIP,0,1);
        First.setHalignment(getStatIP, HPos.CENTER);

        Button getAllStat=new Button("Вся статистика");//вывод таблицы stat
        getAllStat.setOnMouseClicked(event -> {
            preparation();
            try {
                DBWork.lastQuery="outStat";
                DBWork.outStat();
            } catch (SQLException e) {
                System.out.println("FilterPanel_getAllStat :: "+e.getMessage());
            }
        });
        First.add(getAllStat,0,2);
        First.setHalignment(getAllStat, HPos.CENTER);
        //-----первая таблица-----


        //-----вторая таблица-----
        GridPane Second=new GridPane();
        Second.setAlignment(Pos.CENTER);
        Second.setTranslateX(5);
        Second.setHgap(10);
        Second.setVgap(10);
        Second.setPadding(new Insets(-3, 10, 5, 0));
        Second.setStyle("-fx-border-Color:black");

        Button getResult=new Button("Последнее сканирование");//вывод таблицы Result
        getResult.setOnMouseClicked(event -> {
            DBWork.lastQuery="";
            preparation();

            try {
                DBWork.getLastScan();
            } catch (SQLException e) {
                System.out.println("FilterPanel_getLastScan :: "+e.getMessage());
            }
        });
        Second.add(getResult,1,0);
        Second.setHalignment(getResult, HPos.CENTER);

        Button getOnLine=new Button("В сети");//вывести устройства, которые были в сети при последнем сканировании
        getOnLine.setOnMouseClicked(event -> {
            DBWork.lastQuery="";
            try {
                DBWork.getOnLineScan();
            } catch (SQLException e) {
                System.out.println("FilterPanel_getOnLineScan :: "+e.getMessage());
            }
        });
        Second.add(getOnLine,1,1);
        Second.setHalignment(getOnLine, HPos.CENTER);

        Button getOffLine=new Button("Не в сети");//вывести устройства, которые не были в сети при последнем сканировании
        getOffLine.setOnMouseClicked(event -> {
            DBWork.lastQuery="";
            try {
                DBWork.getOffLineScan();
            } catch (SQLException e) {
                System.out.println("FilterPanel_getOffLineScan :: "+e.getMessage());
            }
        });
        Second.add(getOffLine,1,2);
        Second.setHalignment(getOffLine, HPos.CENTER);
        //-----вторая таблица-----


        //-----третья таблица-----
        GridPane Third=new GridPane();
        Third.setAlignment(Pos.CENTER);
        Third.setTranslateX(10);
        Third.setHgap(10);
        Third.setVgap(10);
        Third.setPadding(new Insets(-3, 10, 5, -10));
        Third.setStyle("-fx-border-Color:black");

        Button getOnNote=new Button("С заметками");//вывести устройства с заметками
        getOnNote.setOnMouseClicked(event -> {
            DBWork.lastQuery="";
            try {
                DBWork.getOnNote();
            } catch (SQLException e) {
                System.out.println("FilterPanel_getOnNote :: "+e.getMessage());
            }
        });
        Third.add(getOnNote,2,0);
        Third.setHalignment(getOnNote, HPos.CENTER);

        Button getOffNote=new Button("Без заметок");//вывести устройства без заметок
        getOffNote.setOnMouseClicked(event -> {
            DBWork.lastQuery="";
            try {
                DBWork.getOffNote();
            } catch (SQLException e) {
                System.out.println("FilterPanel_getOffNote :: "+e.getMessage());
            }
        });
        Third.add(getOffNote,2,1);
        Third.setHalignment(getOffNote, HPos.CENTER);

        Button delete=new Button("Удаление");//меню удаления
        delete.setOnMouseClicked(event -> Main.DP.setVisible(true));
        Third.add(delete,2,2);
        Third.setHalignment(delete, HPos.CENTER);
        //-----третья таблица-----

        getChildren().add(First);
        getChildren().add(Second);
        getChildren().add(Third);
    }

    //подготовка
    static void preparation(){
        Window.StopFlag=true;
        ControlPanel.Start.setDisable(false);
        ControlPanel.Stop.setDisable(true);
        InfoBar.clear.setDisable(false);

        InfoBar.time.setText(" Вывод Базы Данных ");

        Window.clearOut();
    }
}
