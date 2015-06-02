/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.internalservicecontrollerbatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author gabriele.derossi
 */
public class ServerCall implements Runnable {

    private int index;
    private CallList list;

    public ServerCall(int _index, CallList _list) {
        index = _index;
        list = _list;
    }

    @Override
    public void run() {
        String url = list.getCallToPerform();
        try {
            HttpClient client = HttpClientBuilder.create().build();
            if (url != null) {
                
                //HttpPost post = new HttpPost(url);
                //StringEntity entity = new StringEntity("");
                //post.setEntity(entity);
                
                
                HttpGet request = new HttpGet(url);
                long start = System.currentTimeMillis();
                HttpResponse response = client.execute(request);
                long end = System.currentTimeMillis();
                //System.out.println("Response Code : "+ response.getStatusLine().getStatusCode() + " index: " +index + " start: " + start + " end: " +end+ " thread name: "+ Thread.currentThread().getName());
                double r = Math.random();
                long wait = Math.round(r * 250);
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");
                String printout="Server Call: "+ url+ " Response Code : "+ response.getStatusLine().getStatusCode() + " Response content: "+responseString;
                list.resolveCall(printout, true);
                Thread.sleep(wait);
            }
        } catch (Exception ex) {
            Logger.getLogger(ServerCall.class.getName()).log(Level.SEVERE, null, ex);
            list.resolveCall("ERROR PERFORMING: "+url, false);
        }

    }

}
