package sample;

public class ThreadControl implements Runnable{
    static String SubNet=Window.SubNet;
    static int IP=Window.startIP;
    static int counter;

    @Override
    public void run() {
        System.out.println(IP);
        System.out.println(Window.endIP);

        ProgressIndicatorBar.setProgress(0.0);

        for(counter=0;counter<ThreadPanel.correct&&Window.endIP>=IP;counter++){
            if(ControlPanel.Stop.isDisable())break;

            createTh();
        }

        while(counter>0||Window.endIP>=IP){
            if(ControlPanel.Stop.isDisable())break;

            if(counter!=ThreadPanel.correct&&Window.endIP>=IP){
                createTh();
                counter++;
            }
        }

        if(!ControlPanel.Stop.isDisabled()){
            timer();
            ProgressIndicatorBar.setProgress(1.0);
        }
    }

    private static void createTh(){
        final int fIP=IP;
        final int Fcounter=OutPanel.counter;

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        IP+=1;
        OutPanel.counter+=1;

        final SearchThread ST=new SearchThread(Fcounter,SubNet+"."+fIP);
        final Thread T=new Thread(ST);
        T.setDaemon(true);
        T.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void timer(){
        TimerReScan TRS=new TimerReScan();
        Thread T=new Thread(TRS);
        T.setDaemon(true);
        T.start();
    }

}
