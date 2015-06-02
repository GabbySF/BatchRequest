/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.internalservicecontrollerbatch;

import java.util.ArrayList;

/**
 *
 * @author gabriele.derossi
 */
public class CallList {
    private ArrayList<String> servicesToCall;
    private ArrayList<String> servicesFail;
    private ArrayList<String> servicesSucces;
    private int total_calls;
    private int exec_calls;
    public CallList(){
        servicesToCall = new ArrayList<String>();
        servicesFail = new ArrayList<String>();
        servicesSucces = new ArrayList<String>();
        total_calls=0;
        exec_calls=0;
    }
    
    public synchronized void addCallToPerform(String call){
        servicesToCall.add(call);
        total_calls++;
    }
    
    public synchronized String getCallToPerform(){
        if(servicesToCall.size() > 0){
            //System.out.println("Thread accessing getCallToPerform: "+Thread.currentThread().getName() + " Size: "+servicesToCall.size());
            return servicesToCall.remove(0);
        }else{
            return null;
        }
        
    }
    
    public synchronized void resolveCall(String call,boolean success){
        if(success)
            servicesSucces.add(call);
        else
             servicesFail.add(call);
        
        exec_calls++;
        float progression = (exec_calls*100)/total_calls;
        System.out.print("\rProgression: "+progression+"%");
    }

    public ArrayList<String> getServicesFail() {
        return servicesFail;
    }

    public ArrayList<String> getServicesSucces() {
        return servicesSucces;
    }
    
    
    
    
  
}
