package sample;

import javafx.application.Platform;
import javafx.scene.control.Label;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.Date;

public class SearchThread implements Runnable{
    private static String readyIP;
    private static int counter;
    private static Label status=new Label();

    SearchThread(int position, String IP){
        counter=position;
        readyIP=IP;
    }

    @Override
    public void run(){
        System.out.println(counter+"//"+readyIP);

        final int Fcounter=counter;
        final String fIP=readyIP;

        Window.record rec=new Window.record();
        rec.counter=Fcounter;

        ProcessBuilder builder = new ProcessBuilder("tracert","-h","5",fIP).redirectErrorStream(true);

        Label ipAddress=new Label(fIP);
        rec.address=fIP;
        Platform.runLater(() -> Window.OP.add(ipAddress,0,Fcounter));

        status=new Label("Вычисляется");
        status.setStyle("-fx-text-fill:rgb(255,186,0)");
        final Label Fstatus=status;
        Platform.runLater(() -> Window.OP.add(Fstatus,2,Fcounter));

        Label date=new Label(new Date()+"");
        rec.date=date.getText();
        Platform.runLater(() -> Window.OP.add(date,3,Fcounter));

        final Label note=new Label("");
        Platform.runLater(() -> {
            final String S=DBWork.getNote(fIP,true);
            note.setText(S);
        });
        Platform.runLater(() -> Window.OP.add(note,4,Fcounter));

        Platform.runLater(() -> Window.OP.add(Window.MoreBT(fIP+"",note),5,Fcounter));

        Label netName=new Label("");
        final Label FNN=netName;
        Platform.runLater(() -> Window.OP.add(FNN,1,Fcounter));

        final Label FNetName=netName;
        try{
            Process process=builder.start();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();

            int b;
            String S;

            System.out.println("Получаю");
            while((b=process.getInputStream().read())!=-1&&(!Window.StopFlag)){
                baos.write(b);
            }
            S=baos.toString("Cp866");

            //System.out.println(S);

            String[] answer=StringWork(S,fIP);
            Platform.runLater(() -> {
                Fstatus.setText(answer[0]);
                if(answer[0].equals("Не в сети")){
                    Fstatus.setStyle("-fx-text-fill:red");
                }else{
                    Fstatus.setStyle("-fx-text-fill:green");
                }
                rec.status=Fstatus.getText();

                FNetName.setText(answer[1]);
                rec.name=FNetName.getText();
            });
        } catch(Exception e){
            Platform.runLater(() -> {
                Fstatus.setText("Ошибка");
                Fstatus.setStyle("-fx-text-fill:red");
            });
            rec.status=Fstatus.getText();
        }
        final Window.record Frec=rec;
        Platform.runLater(() -> {
            Window.storage.add(Frec);
            try {
                if(DBWork.FindRead(Frec)){
                    Platform.runLater(() -> FNetName.setStyle("-fx-text-fill:red"));
                }
            } catch (ClassNotFoundException e) {
                System.out.println("SearchThread_1 :: "+e.getMessage());
            } catch (SQLException e) {
                System.out.println("SearchThread_2 :: "+e.getErrorCode()+" :: "+e.getMessage());
            }
        });

        InfoBar.count+=Window.progress;
        ProgressIndicatorBar.setProgress();

        if(Window.StopFlag){
            Platform.runLater(() -> {
                status.setText("Прервано");
                status.setStyle("-fx-text-fill:rgb(255,186,0)");
            });
        }

        Platform.runLater(() -> ThreadControl.counter--);
    }

    private static String[] StringWork(String S, String ip){
        String[] answer=new String[2];
        answer[0]="Не в сети";
        answer[1]="";
        String[] IPsplit=S.split(ip);

        System.out.println(ip);

        for(int i=0;i<IPsplit.length;i++){
            System.out.println("Часть "+(i+1));
            System.out.println(IPsplit[i]);
        }

        if(IPsplit.length<3){
            return answer;
        }

        answer[0]="В сети";
        answer[1]=IPsplit[0].replace("Трассировка маршрута к","");
        answer[1]=answer[1].replace("[","");
        answer[1]=answer[1].replace("]","");
        answer[1]=answer[1].replace(" ","");
        answer[1]=answer[1].replaceAll(System.getProperty("line.separator"),"");

        return answer;
    }
}

