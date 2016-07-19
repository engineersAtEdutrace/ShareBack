package com.fantasticfive.shareback.newshareback.connection;

import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import com.fantasticfive.shareback.newshareback.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by sagar on 19/7/16.
 */
public class SocketHelper {

    ServerSocket servSkt;

    Callback callback;
    public SocketHelper(Callback callback){
        this.callback = callback;
    }

    public void openSocket(int port){
        try {
            servSkt = new ServerSocket(port);

            //Sending Tokens
            while(true) {
                Socket skt = servSkt.accept();
                int token = callback.onRequestReceived(skt);
                boolean success = sendToken(skt, token);
                if (!success)
                    callback.onTokenSendFailed();
                else if(token <= 1) {
                    servSkt.close();
                    callback.onServerSocketClosed();
                }
            }
            //-- Sending Tokens

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeSocket(){
        try {
            servSkt.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receiveToken(NsdServiceInfo serviceInfo){
        String parentName = serviceInfo.getServiceName();
        InetAddress ip = serviceInfo.getHost();
        int port = serviceInfo.getPort();

        try {
            Socket skt = new Socket(ip, port);
            //Reading Response
            BufferedReader br = new BufferedReader(new InputStreamReader(skt.getInputStream()));
            String temp = "";
            String result = "";
            while((temp = br.readLine()) != null){
                if(temp.contains(Constants.END_OF_MSG)){
                    temp = temp.replace(Constants.END_OF_MSG, "");
                    result+=temp;
                    break;
                }
                result += temp;
            }
            //-- Reading Response

            String token = (new JSONObject(result)).getString(Constants.JSON_TOKEN_NO);
            return parentName + "." + token; //Returning new NSD Name
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "TOKEN_RECEIVE_ERROR";
    }

    private boolean sendToken(Socket skt, int token){   //For Server node
        //Sending Request
        JSONObject main = new JSONObject();
        try {
            main.put(Constants.JSON_TOKEN_NO, token);
            PrintWriter out = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(skt.getOutputStream())
                    ), true);
            out.println( main.toString() + Constants.END_OF_MSG );
            Log.e("My Tag", "Request: "+main.toString());
            out.flush();
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
        //-- Sending Request
    }

    public interface Callback{
        int onRequestReceived(Socket skt);
        void onTokenSendFailed();
        void onServerSocketClosed();
    }
}