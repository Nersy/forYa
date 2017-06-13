package sample;

import javafx.application.Platform;

public class DataBaseConnection implements Runnable {
    private boolean flag;

    @Override
    public void run() {
        flag=true;

        Platform.runLater(() -> {
            Window.filterP.setDisable(true);
            InfoBar.time.setText(" Подключение базы данных ");
        });

        try {
            DBWork.Connection();
        } catch (Exception e) {
            err(e);
        }
        if(flag){
            Platform.runLater(() -> InfoBar.time.setText(" База данных подключена "));
            Window.filterP.setDisable(false);
        }
    }
    private void err(Exception e){
        flag=false;
        Platform.runLater(() -> InfoBar.time.setText(" База данных не подключена "));
        System.out.println("DataBaseConnection :: err :: "+e.getMessage());
    }
}
