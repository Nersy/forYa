package sample;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import java.sql.SQLException;

class NoteEditor extends StackPane{
    static Label edit;
    static String ipaddress;
    static Label text;
    static TextArea note;

    NoteEditor(){
        setPrefSize(Main.D.width,Main.D.height);
        setStyle("-fx-background-Color:rgba(255,255,255,0)");
        setVisible(false);

        ImageView IV=new ImageView(new Image(getClass().getResourceAsStream("/res/note.png")));

        getChildren().add(IV);

        GridPane grid=new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15, 5, 5, 5));

        text=new Label("");
        grid.add(text,0,0);
        grid.setHalignment(text, HPos.CENTER);

        note=new TextArea("");
        note.setPrefSize(360,330);
        grid.add(note,0,1);
        grid.setHalignment(note, HPos.CENTER);



        GridPane forBut=new GridPane();

        forBut.setAlignment(Pos.CENTER);
        forBut.setHgap(10);
        forBut.setVgap(10);
        forBut.setPadding(new Insets(0, 5, 5, 5));

        Button write=new Button("Записать");
        write.setOnAction(event -> {
            DBWork.setNote(ipaddress,note.getText());
            final Label Fedit=edit;
            Platform.runLater(() -> Fedit.setText(DBWork.getNote(ipaddress,true)));
            setVisible(false);

            if(!DBWork.lastQuery.equals("")){
                String[] s=DBWork.lastQuery.split("-");
                try{
                    if(s.length==1){
                        DBWork.outStat();
                    }else{
                        DBWork.outStat(s[1]);
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        forBut.add(write,0,0);
        forBut.setHalignment(write, HPos.CENTER);

        Button close=new Button("Закрыть");
        close.setOnAction(event -> setVisible(false));
        forBut.add(close,1,0);
        forBut.setHalignment(close, HPos.CENTER);

        grid.add(forBut,0,2);
        grid.setHalignment(forBut, HPos.CENTER);



        getChildren().add(grid);
    }
}
