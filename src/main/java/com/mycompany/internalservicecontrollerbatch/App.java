package com.mycompany.internalservicecontrollerbatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws InterruptedException {
        
        if(args.length > 0){
            ArrayList<String> callsToPerform = App.serverCalls(args[0]);
            //ArrayList<String> callsToPerform = App.serverCalls("/Users/gabriele.derossi/Desktop/test.txt");
            CallList list = new CallList();
            for (int i = 0; i < callsToPerform.size(); i++) {
                list.addCallToPerform(callsToPerform.get(i));
            }

            ExecutorService executor = Executors.newFixedThreadPool(50);
            for (int i = 0; i < callsToPerform.size(); i++) {
                Runnable worker = new ServerCall(i, list);
                executor.execute(worker);
            }
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.DAYS);
            
            ArrayList<String> calls=list.getServicesSucces();
            calls.addAll(list.getServicesFail());
            
            App.writeToFile(args[1], calls);
            
            System.out.println("\ndone");
        }
    }

    private static ArrayList<String> serverCalls(String path) {
        ArrayList<String> serverCalls = new ArrayList<String>();
        ArrayList<String> fileLines = App.readFile(path);
        if(fileLines.size() > 0){
            String url = fileLines.get(0);
            for(int i=1 ; i<fileLines.size(); i++){
                String params= fileLines.get(i);
                params=params.replace(",","/");
                String urlwithparam = url+params;
                serverCalls.add(urlwithparam);
            }
            
            
        }
        return serverCalls;
    }

    private static ArrayList<String> readFile(String path) {

        BufferedReader br = null;
        ArrayList<String> lines = new ArrayList<String>();
        String line = "";
        try {
            br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (Exception e) {
            lines = new ArrayList<String>();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return lines;
    }
    
    
    private static void writeToFile(String path, ArrayList<String> toWrite){
       try {
            PrintWriter writer = new PrintWriter(path, "UTF-8");
            Iterator it=toWrite.iterator();
            while(it.hasNext()){
                String line=(String)it.next();
                writer.println(line);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
