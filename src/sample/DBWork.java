/******************************************************
 Класс «DBWork», отвечающий за взаимодействие с базой данных
******************************************************
 Используемые переменные:
 conn ─ связывает базу данных с программой
 statmt ─ хранит подключение к базе данных
 resSet ─ хранит результаты запроса
 lasQuery ─ последний выполненный запрос
******************************************************/

package sample;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.sql.*;

class DBWork {
    private static Connection conn;
    private static Statement statmt;
    private static ResultSet resSet;
    static String lastQuery="";

    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
    static void Connection() throws ClassNotFoundException, SQLException {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:ping_db.db");

        System.out.println("База Подключена!");

        CreateDB();
    }

    // --------Создание таблицы--------
    private static void CreateDB() throws ClassNotFoundException, SQLException {
        statmt = conn.createStatement();

        statmt.execute("CREATE TABLE if not exists 'address' " +
        "('ip' text PRIMARY KEY, 'note' text);");

        statmt.execute("CREATE TABLE if not exists 'stat' " +
        "('address' text, 'name' text, 'status' text, 'date' text);");

        statmt.execute("CREATE TABLE if not exists 'result' " +
        "('address' text PRIMARY KEY, 'name' text, 'status' text, 'date' text, 'note' text);");

        System.out.println("Таблицы созданы или уже существуют.");
    }

    // --------Запись в Stat, проверка записи и сравнение сетевых имен в таблице--------
    static boolean FindRead(Window.record rec) throws ClassNotFoundException, SQLException{
        String name=EnCrypto(rec.name);

        writeStat(rec);
        resSet = statmt.executeQuery("SELECT * FROM result where address='"+rec.address+"'");
        if(resSet.next()&&resSet.getString("address").equals(rec.address)){
            if(resSet.getString("name").equals(name)){
                updateResult(rec);
                return false;
            }else{
                updateResult(rec);
                return true;//подсветка разного ника тут
            }
        }else{
            writeResult(rec);
            return false;
        }
    }

    // --------Обновляет запись в талице Result--------
    private static void updateResult(Window.record rec) throws ClassNotFoundException, SQLException{
        String name=EnCrypto(rec.name);

        statmt.execute("UPDATE result SET name='"+name+"', status='"+rec.status+"', date='"+rec.date+
        "' WHERE address='"+rec.address+"'");

        System.out.println("Апдейт в Result");
    }


    // --------Возвращает заметку по ip-адресу--------
    static String getNote(String ip, boolean limit){
        String S="";

        try {
            resSet=statmt.executeQuery("SELECT note FROM address where ip='"+ip+"'");
            if(resSet.next()){
                S=DeCrypto(resSet.getString("note"));
                if(limit){
                    if(S.length()>50){
                        S=noteEdit(S,true);
                    }else{
                        S=noteEdit(S,false);
                    }
                }
            }else{
                statmt.executeQuery("INSERT INTO 'address' ('ip','note') VALUES ('"+ip+"','');");
            }
        } catch (SQLException e) {
            System.out.println("DBWork_getNote :: "+e.getErrorCode()+" :: "+e.getMessage());
        }

        return S;
    }
    // --------Обновляет заметку по ip-адресу--------
    static void setNote(String ip, String note){
        note=EnCrypto(note);

        try {
            statmt.execute("UPDATE address SET note='"+note+"' WHERE ip='"+ip+"'");
        } catch (SQLException e) {
            System.out.println("DBWork_setNote :: "+e.getErrorCode()+" :: "+e.getMessage());
        }
    }
    // --------Маленький кусочек для нормального отображения строк в заметках--------
    private static String noteEdit(String S, boolean cut){
        if(cut){
            S=S.substring(0,50)+"...";
        }
        S=S.replaceAll(System.getProperty("line.separator"),"");
        S=S.replaceAll("\n","");

        return S;
    }


    // --------Заполнение таблицы stat--------
    private static void writeStat(Window.record rec) throws SQLException {
        String name=EnCrypto(rec.name);

        statmt.execute("INSERT INTO 'stat' ('address','name','status','date') " +
        "VALUES ('"+rec.address+"','"+name+"','"+rec.status+"','"+rec.date+"'); ");

        System.out.println("Запись внесена в Stat");
    }
    // --------Заполнение таблицы result--------
    private static void writeResult(Window.record rec) throws SQLException {
        String name=EnCrypto(rec.name);

        statmt.execute("INSERT INTO 'result' ('address','name','status','date') " +
        "VALUES ('"+rec.address+"','"+name+"','"+rec.status+"','"+rec.date+"'); ");

        System.out.println("Запись внесена в Result");
    }


    // --------Закрытие--------
    static void CloseDB() throws ClassNotFoundException, SQLException, NullPointerException{
        conn.close();
        statmt.close();
        resSet.close();

        System.out.println("Соединения закрыты");
    }


    // --------FilterPanel - запрос статистики по адресу--------
    static void outStat(String ip) throws SQLException{
        resSet=statmt.executeQuery("SELECT stat.*, address.note FROM stat, address "+
        "WHERE stat.address='"+ip+"' AND address.ip='"+ip+"'");

        Out(resSet,"All");
    }
    // --------FilterPanel - запрос всей статистики--------
    static void outStat() throws SQLException{
        resSet=statmt.executeQuery("SELECT stat.*, address.note FROM stat, address "+
                "WHERE stat.address=address.ip");

        Out(resSet,"All");
    }


    // --------FilterPanel - запрос последнего сканирования--------
    static void getLastScan() throws  SQLException{
        resSet=statmt.executeQuery("SELECT result.*, address.note FROM result, address "+
                "WHERE result.address=address.ip");

        Out(resSet,"All");
    }
    // --------FilterPanel - запрос "В сети" в последнем сканировании--------
    static void getOnLineScan() throws  SQLException{
        resSet=statmt.executeQuery("SELECT result.*, address.note FROM result, address "+
                "WHERE result.status='В сети' AND result.address=address.ip");

        Out(resSet,"All");
    }
    // --------FilterPanel - запрос "Не в сети" в последнем сканировании--------
    static void getOffLineScan() throws  SQLException{
        resSet=statmt.executeQuery("SELECT result.*, address.note FROM result, address "+
                "WHERE result.status='Не в сети' AND result.address=address.ip");

        Out(resSet,"All");
    }


    // --------FilterPanel - запрос "С заметками" в address--------
    static void getOnNote() throws  SQLException{
        resSet=statmt.executeQuery("SELECT * FROM address WHERE note<>''");

        Out(resSet,"note");
    }
    // --------FilterPanel - запрос "Без заметок" в address--------
    static void getOffNote() throws  SQLException{
        resSet=statmt.executeQuery("SELECT * FROM address WHERE note=''");

        Out(resSet,"");
    }


    // --------FilterPanel - запрос на удаление всех записей--------
    static void delete() throws ClassNotFoundException, SQLException{
        statmt.execute("DROP TABLE address");
        statmt.execute("DROP TABLE result");
        statmt.execute("DROP TABLE stat");
        CreateDB();
    }
    // --------FilterPanel - запрос на удаление записей по ip--------
    static void delete(String com) throws ClassNotFoundException, SQLException{
        statmt.execute("DELETE FROM address WHERE ip='"+com+"'");
        statmt.execute("DELETE FROM result WHERE address='"+com+"'");
        statmt.execute("DELETE FROM stat WHERE address='"+com+"'");
    }


    // --------FilterPanel - проверка на null в результатах запроса--------
    private static boolean preOut(ResultSet rs) throws SQLException{
        FilterPanel.preparation();

        if(!rs.next()){
            Platform.runLater(() -> {
                Window.OP.add(new Label("Запрос вернул null"),1, OutPanel.counter);
                OutPanel.counter++;
            });
            return false;
        }
        return true;
    }
    // --------FilterPanel - большой вывод всех запросов--------
    private static void Out(ResultSet rs, String com) throws SQLException{
        if(!preOut(rs))return;

        do{
            Window.record rec=new Window.record();

            rec.address=rs.getString(1);

            if(com.equals("All")){

                rec.name=DeCrypto(rs.getString("name"));
                rec.status=rs.getString("status");
                rec.date=rs.getString("date");

            }

            if(com.equals("All")||com.equals("note")){

                rec.note=DeCrypto(rs.getString("note"));
                if(rec.note.length()>50){
                    rec.note=noteEdit(rec.note,true);
                }else{
                    rec.note=noteEdit(rec.note,false);
                }

            }

            final Window.record Frec=rec;
            final int Fcounter=Window.OP.counter;
            Platform.runLater(() -> {
                Window.OP.add(new Label(Frec.address),0,Fcounter);

                if(com.equals("All")){

                    Window.OP.add(new Label(Frec.name),1,Fcounter);
                    final Label Fstatus=new Label(Frec.status);
                    if(Fstatus.getText().equals("Не в сети")){
                        Fstatus.setStyle("-fx-text-fill:red");
                    }else{
                        Fstatus.setStyle("-fx-text-fill:green");
                    }
                    Window.OP.add(Fstatus,2,Fcounter);
                    Window.OP.add(new Label(Frec.date),3,Fcounter);

                }

                final Label N=new Label(Frec.note);
                Window.OP.add(N,4,Fcounter);
                Window.OP.add(Window.MoreBT(Frec.address,N),5,Fcounter);
            });
            Window.OP.counter++;

        }while(rs.next());
    }


    // --------Зашифровка строки--------
    static private String EnCrypto(String S){
        String out="";
        for(int i=0;i<S.length();i++){
            out+=(char)(S.charAt(i)*3);
        }
        return out;
    }
    // --------Расшифровка строки--------
    private static String DeCrypto(String S){
        String out="";
        for(int i=0;i<S.length();i++){
            out+=(char)(S.charAt(i)/3);
        }
        return out;
    }
}
