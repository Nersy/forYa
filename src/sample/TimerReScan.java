package sample;

import javafx.application.Platform;
import java.util.concurrent.TimeUnit;

public class TimerReScan implements Runnable {
    private int time=0;

    TimerReScan(){
        this.time=TimerPanel.correct*60;
    }

    @Override
    public void run() {
        ControlPanel.Start.setDisable(true);
        ControlPanel.Stop.setDisable(false);
        while(time>=0&&!ControlPanel.Stop.isDisabled()){
            Platform.runLater(() -> InfoBar.time.setText(" Следующее сканирование через: "+time+" "));
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("TimerReScan :: "+e.getMessage());
            }
            time--;

            if(time==2){
                Window.clearOut();
            }

        }
        if(!ControlPanel.Stop.isDisabled()){
            Window.Search(ControlPanel.GetIp.getText());
        }
    }
}
