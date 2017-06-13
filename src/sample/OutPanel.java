package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

class OutPanel extends GridPane{
    static int counter=1;
    private static GridPane _out;

    OutPanel(){
        _out=this;

        setAlignment(Pos.CENTER);
        setHgap(10);
        setVgap(3);
        setPadding(new Insets(5, 5, 5, 5));
    }

    static void preStart(){
        _out.add(new Label("ip-адрес"),0,0);
        _out.add(new Label("Сетевое имя"),1,0);
        _out.add(new Label("Статус"),2,0);
        _out.add(new Label("Дата/Время"),3,0);
        _out.add(new Label("Заметка"),4,0);
        _out.add(new Label("Подробнее"),5,0);
    }
}
