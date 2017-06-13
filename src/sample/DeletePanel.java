package sample;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;

class DeletePanel extends StackPane{
    private Button deleteAll;
    private Button deleteBut;
    private Button deleteIP;
    private TextField ip;
    private Label info;
    private boolean All=false;

    DeletePanel(){
        setPrefSize(Main.D.width,Main.D.height);
        setStyle("-fx-background-Color:rgba(255,255,255,0)");
        setVisible(false);

        ImageView IV=new ImageView(new Image(getClass().getResourceAsStream("/res/delete.png")));

        getChildren().add(IV);

        GridPane grid=new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));


        ip=new TextField();
        ip.setVisible(false);
        ip.setAlignment(Pos.CENTER);
        grid.add(ip,0,0);
        grid.setHalignment(ip, HPos.CENTER);

        deleteIP=new Button("Удалить по адресу");
        deleteIP.setOnMouseClicked(event -> {
            deleteIP.setVisible(false);
            deleteAll.setVisible(false);
            ip.setVisible(true);
            deleteBut.setVisible(true);
        });
        grid.add(deleteIP,0,0);
        grid.setHalignment(deleteIP, HPos.CENTER);

        info=new Label("Вы уверены?");
        info.setVisible(false);
        grid.add(info,0,0);
        grid.setHalignment(info,HPos.CENTER);


        deleteAll=new Button("Удалить все");
        deleteAll.setOnMouseClicked(event -> {
            All=true;
            deleteIP.setVisible(false);
            info.setVisible(true);
            deleteAll.setVisible(false);
            deleteBut.setVisible(true);
        });
        grid.add(deleteAll,0,1);
        grid.setHalignment(deleteAll, HPos.CENTER);

        deleteBut=new Button("Удалить");
        deleteBut.setVisible(false);
        deleteBut.setOnMouseClicked(event -> {
            if(!info.isVisible()){
                ip.setVisible(false);
                info.setVisible(true);
            }else{
                if(All){
                    try {
                        DBWork.delete();
                    } catch (Exception e) {
                        System.out.println("DeletePanel_All :: "+e.getMessage());
                    }
                }else{
                    try {
                        DBWork.delete(ip.getText());
                    } catch (Exception e) {
                        System.out.println("DeletePanel_IP :: "+e.getMessage());
                    }
                }
                setVisible(false);
                toClose();
            }
        });
        grid.add(deleteBut,0,1);
        grid.setHalignment(deleteBut, HPos.CENTER);

        Button close=new Button("Отменить");
        close.setOnMouseClicked(event -> {
            setVisible(false);
            toClose();
        });
        grid.add(close,0,2);
        grid.setHalignment(close, HPos.CENTER);

        getChildren().add(grid);
    }
    private void toClose(){
        All=false;

        ip.setVisible(false);
        info.setVisible(false);
        deleteBut.setVisible(false);

        deleteIP.setVisible(true);
        deleteAll.setVisible(true);
    }
}
