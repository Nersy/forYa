package sample;

import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

class FAQPanel extends FlowPane{
    private static FlowPane _faq=null;
    private ScrollPane SP=null;

    FAQPanel(){
        _faq=this;

        setPrefSize(Main.D.width,Main.D.height+5);
        setMaxSize(Main.D.width,Main.D.height+5);
        setStyle("-fx-background-color:white");
        setVisible(false);

        FlowPane menu=new FlowPane();
        menu.setStyle("-fx-background-color:rgb(174,174,174)");
        menu.setPrefSize(284,Main.D.height+5);
        menu.setMaxSize(284,Main.D.height+5);
        menu.setOrientation(Orientation.VERTICAL);


        Label faq=new Label("Меню справки");
        faq.setTranslateX(100);
        faq.setTranslateY(10);
        faq.setStyle("-fx-text-fill:black");
        menu.getChildren().add(faq);

        int inset=30;

        Button control=new Button("Панель управления");
        control.setPrefWidth(284);
        control.setTranslateY(inset);
        control.setId("1");
        control.setOnAction(event -> info(control.getId()));
        menu.getChildren().add(control);

        Button timer=new Button("Таймер");
        timer.setPrefWidth(284);
        timer.setTranslateY(inset);
        timer.setId("2");
        timer.setOnAction(event -> info(timer.getId()));
        menu.getChildren().add(timer);

        Button thread=new Button("Потоки");
        thread.setPrefWidth(284);
        thread.setTranslateY(inset);
        thread.setId("3");
        thread.setOnAction(event -> info(thread.getId()));
        menu.getChildren().add(thread);

        Button filter=new Button("Фильтр");
        filter.setPrefWidth(284);
        filter.setTranslateY(inset);
        filter.setId("4");
        filter.setOnAction(event -> info(filter.getId()));
        menu.getChildren().add(filter);

        Button delete=new Button("Удаление");
        delete.setPrefWidth(284);
        delete.setTranslateY(inset);
        delete.setId("5");
        delete.setOnAction(event -> info(delete.getId()));
        menu.getChildren().add(delete);

        Button out=new Button("Вывод");
        out.setPrefWidth(284);
        out.setTranslateY(inset);
        out.setId("6");
        out.setOnAction(event -> info(out.getId()));
        menu.getChildren().add(out);

        Button editnote=new Button("Заметки");
        editnote.setPrefWidth(284);
        editnote.setTranslateY(inset);
        editnote.setId("7");
        editnote.setOnAction(event -> info(editnote.getId()));
        menu.getChildren().add(editnote);

        Button down=new Button("Информационная панель");
        down.setPrefWidth(284);
        down.setTranslateY(inset);
        down.setId("8");
        down.setOnAction(event -> info(down.getId()));
        menu.getChildren().add(down);

        Button close=new Button("Закрыть");
        close.setPrefWidth(284);
        close.setTranslateY(inset+280);
        close.setOnAction(event -> {
            info("0");
            setVisible(false);
        });
        menu.getChildren().add(close);

        getChildren().add(menu);

        info("0");
    }

    private void info(String ID){
        switch(ID){
            case"1":del("/res/FAQ/1_control.png");
                break;
            case"2":del("/res/FAQ/2_timer.png");
                break;
            case"3":del("/res/FAQ/3_thread.png");
                break;
            case"4":del("/res/FAQ/4_filter.png");
                break;
            case"5":del("/res/FAQ/5_delete.png");
                break;
            case"6":del("/res/FAQ/6_out.png");
                break;
            case"7":del("/res/FAQ/7_editnote.png");
                break;
            case"8":del("/res/FAQ/8_down.png");
                break;
            default:del("/res/FAQ/0_faq.png");
        }
    }
    private void del(String path){
        if(SP!=null){
            _faq.getChildren().remove(SP);
        }
        ImageView IV=new ImageView(new Image(getClass().getResourceAsStream(path)));
        SP=new ScrollPane(IV);
        SP.setMinSize(700,Main.D.height+5);
        SP.setPrefSize(700,Main.D.height+5);
        SP.setMaxSize(700,Main.D.height+5);
        SP.setStyle("-fx-background-color:white");
        _faq.getChildren().add(SP);
    }
}
