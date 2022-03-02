package edu.upenn.cis350.cisproject.dataSource;

import java.net.URL;

import edu.upenn.cis350.cisproject.AccessWebTask;

public class DataSource{

    public String getPassword(String id) {
        try {
            URL url = new URL("http://10.0.2.2:3000/api?id=" + id);
            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            String password = task.get();
            return password;
        }catch (Exception e){
            return e.toString();
        }
    }

}