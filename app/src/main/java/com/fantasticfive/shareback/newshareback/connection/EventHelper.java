package com.fantasticfive.shareback.newshareback.connection;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.fantasticfive.shareback.newshareback.Constants;
import com.fantasticfive.shareback.newshareback.ShareBucket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by sagar on 21/7/16.
 */
public class EventHelper implements EventsPhysical.Callback{

    int eventId = 1;
    Context context;
    public EventHelper(Context context){
        this.context = context;
    }

    public void sendEvent(String eventName, String eventFile, int eventValue, ArrayList<InetAddress> clients){
        //Encode data
        JSONObject main = new JSONObject();
        try {
            main.put(Constants.JSON_EVENT_ID, eventId);
            main.put(Constants.JSON_EVENT_NAME, eventName);
            main.put(Constants.JSON_EVENT_FILE, eventFile);
            main.put(Constants.JSON_EVENT_PAGE, eventValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //-- Encode data

        //Send Data
        EventsPhysical sender = new EventsPhysical(this);
        sender.sendEvent(main, eventId++, clients);
        //-- Send Data
    }

    @Override
    public void onEventSent(JSONObject errMsg, int failedMsgs) {
        Toast.makeText(context, "Message sent. Failed:"+failedMsgs, Toast.LENGTH_SHORT).show();
        Log.e("My Tag", errMsg.toString());
    }
}
