/******************************************************
 Используемые переменные:
 SubNet ─ подсеть диапазона
 startIP ─ начальный ip-адрес
 endIP ─ конечный p-адрес
 StopFlag ─ флаг остановки программы
 OP - панель вывода
 storage - хранилище для записи полученных значений
 TC - переменная типа класса ThreadControl
 T - переменная класса типа Thread
 progress - прогрес выполнения программы
 filterP - панель для работы с базой данных
 record - набор переменных для "storage"
 ******************************************************/

package sample;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Window extends FlowPane{
    static String SubNet=null;
    static int startIP=-1;
    static int endIP=-1;
    static boolean StopFlag=false;
    static OutPanel OP=new OutPanel();
    static ArrayList<record> storage=new ArrayList<>();
    private static ThreadControl TC=null;
    private static Thread T=null;
    static float progress=0;
    static FilterPanel filterP;

    static class record{
        int counter=0;
        String address="";
        String name="";
        String status="";
        String date="";
        String note="";
    }
//Конструктор
    public Window(){
        setPrefSize(Main.D.width+10,Main.D.height+10);
        setStyle("-fx-background-Color:white");
        setVgap(5);
        setHgap(5);

        //панель дипазона и запуска
        ControlPanel CP=new ControlPanel();
        getChildren().add(CP);

        //таймер
        TimerPanel TP=new TimerPanel();
        getChildren().add(TP);

        //потоки
        ThreadPanel ThP=new ThreadPanel();
        getChildren().add(ThP);
        //потоки

        //фильтры для бд
        filterP=new FilterPanel();
        filterP.setDisable(true);
        getChildren().add(filterP);
        //фильтры для бд

        //окно вывода
        ScrollPane forOUT=new ScrollPane(OP);
        forOUT.setTranslateX(5);
        forOUT.setTranslateY(5);
        forOUT.setPrefSize(Main.D.width-2,Main.D.height-152);
        forOUT.setMaxSize(Main.D.width-2,Main.D.height-152);
        forOUT.setStyle("-fx-border-Color:black");

        getChildren().add(forOUT);
        //окно вывода

        //инормационная панель
        InfoBar IB=new InfoBar();
        getChildren().add(IB);
        //инормационная панель
    }

    //Процедура очищения вывода
    static void clearOut(){
        OutPanel.counter=1;
        Platform.runLater(() -> {
            OP.getChildren().clear();
            OutPanel.preStart();
        });
    }

    //Процедура, начинающая сканирование
    static void Search(String IP){
        if(checkValue(IP)==0){
            ControlPanel.Start.setDisable(false);
            ControlPanel.Stop.setDisable(true);
            return;
        }
        clearOut();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            System.out.println("Window_sleep :: "+e.getMessage());
        }

        Platform.runLater(() -> InfoBar.time.setText(" Идет сканирование "));

        if(endIP<startIP){
            startIP+=endIP;
            endIP=startIP-endIP;
            startIP=startIP-endIP;
        }

        progress=(float)100/(endIP-startIP+1);
        InfoBar.count=0;
        ProgressIndicatorBar.setProgress(0.0);

        System.out.println("SubNet: "+SubNet);
        System.out.println("startIP: "+startIP);
        System.out.println("endIP: "+endIP);

        if(T==null||TC==null){
            T=null;
            TC=null;
            TC=new ThreadControl();
            T=new Thread(TC);
            T.setDaemon(true);
            T.start();
        }else{
            T.stop();
            ThreadControl.SubNet =SubNet;
            ThreadControl.IP =startIP;
            T=new Thread(TC);
            T.setDaemon(true);
            T.start();
        }

    }

    //Процедура, проверяющая значение/диапазон значений и заполняет startIP и endIP
    private static int checkValue(String IP){
        IP=IP.replace(".",",");
        String[] dash=IP.split("-");

        //для первого числа
        String[] point=dash[0].split(",");
        if(point.length<3){
            ControlPanel.GetIp.setStyle("-fx-text-fill:red");
            return 0;
        }

        dash[0]=dash[0].replace(",",".");

        if(point.length==3){
            SubNet=dash[0];
            startIP=0;
            dash[0]+=".0";
        }else{
            if(point.length==4){
                SubNet=point[0]+"."+point[1]+"."+point[2];
                startIP=Integer.parseInt(point[3]);
            }
        }

        try {
            InetAddress ipAddress=InetAddress.getByName(dash[0]);
        } catch (UnknownHostException e) {
            ControlPanel.GetIp.setStyle("-fx-text-fill:red");
            return 0;
        }

        //для второго числа
        if(dash.length>1){
            String addr=point[0]+"."+point[1]+"."+point[2]+"."+dash[1];
            try {
                InetAddress ipAddress=InetAddress.getByName(addr);
                endIP=Integer.parseInt(dash[1]);
            } catch (UnknownHostException e) {
                ControlPanel.GetIp.setStyle("-fx-text-fill:red");
                return 0;
            }
        }
        if(endIP==-1)endIP=255;
        return 1;
    }

    //ПРоцедура, возвращающая кнопку для вывода заметки
    static Button MoreBT(String ip, Label note){
        Button bt=new Button(">");
        bt.setOnAction(event -> {
            Main.NE.setVisible(true);
            NoteEditor.text.setText("Заметки для "+ip);
            NoteEditor.edit=note;
            NoteEditor.ipaddress=ip;
            NoteEditor.note.setText(DBWork.getNote(ip,false));
        });
        return bt;
    }
}
