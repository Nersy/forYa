package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.awt.*;

public class Main extends Application {
    static Dimension D;
    static FAQPanel FAQ;
    static NoteEditor NE;
    static DeletePanel DP;

    private static Parent root(){
        StackPane p=new StackPane();

        Window W=new Window();
        FAQ=new FAQPanel();
        NE=new NoteEditor();
        DP=new DeletePanel();

        p.getChildren().addAll(W,NE,FAQ,DP);
        p.setAlignment(Pos.CENTER);
        return p;
    }

    @Override
    public void start(Stage primaryStage){
        D=GoodSize();

        primaryStage.setTitle("PingSystem");
        primaryStage.setScene(new Scene(root(),Main.D.width,D.height));
        primaryStage.setResizable(false);
        primaryStage.show();

        DataBaseConnection DBC=new DataBaseConnection();
        Thread t=new Thread(DBC);
        t.setDaemon(true);
        t.start();
    }

    private static Dimension GoodSize(){
        Dimension d=new Dimension();
        d.width=984;
        d.height=568;
        return d;
    }

    public void stop(){
        Platform.runLater(() -> {
            try {
                DBWork.CloseDB();
            } catch (Exception e) {
                System.out.println("Main :: Stop :: "+e.getMessage());
            }
        });
        Runtime.getRuntime().runFinalization();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().exit(0);
    }

    static int parseInt(String s){
        try{
            return Integer.parseInt(s);
        }catch(Exception e){
            return -1;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
