package sample;

import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

class InfoBar extends FlowPane {
    static float count=0;
    static Label time=new Label(" Подключение базы данных ");
    static Label clear;
    private static ProgressIndicatorBar PIB;

    InfoBar(){
        getStylesheets().add((getClass().getResource("/res/style.css")).toExternalForm());

        setTranslateX(5);
        setTranslateY(5);
        setPrefSize(Main.D.width-5,20);
        setMaxSize(Main.D.width-5,20);

        time.setStyle("-fx-border-Color:black");
        getChildren().add(time);

        PIB=new ProgressIndicatorBar();
        PIB.setTranslateX(5);

        getChildren().add(PIB);

        clear=new Label(" Очистить вывод ");
        clear.getStyleClass().add("imitation");
        clear.setTranslateX(10);
        clear.setStyle("-fx-border-Color:black");
        clear.setOnMouseClicked(event -> {
            Window.clearOut();
            ProgressIndicatorBar.setProgress(0.0);
        });
        getChildren().add(clear);

        Label faq=new Label(" Справка ");
        faq.getStyleClass().add("imitation");
        faq.setTranslateX(15);
        faq.setStyle("-fx-border-Color:black");
        faq.setOnMouseClicked(event -> Main.FAQ.setVisible(true));
        getChildren().add(faq);
    }

    static String correctCounter(){
        String s=""+count;
        s=s.replace(".",",");
        String[] res=s.split(",");
        s=s.replace(",",".");

        if(res.length>1){
            if(res[1].length()>=3){
                res[1]=res[1].substring(0,2);
                s=res[0]+"."+res[1];
            }
        }

        return s;
    }
}
