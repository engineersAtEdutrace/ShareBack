package com.fantasticfive.shareback.newshareback;

import android.view.View;

import com.fantasticfive.shareback.newshareback.beans.ShareBucketItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Created by sagar on 20/7/16.
 */
public class ShareBucket {

    String sessionId = "";
    String currentFile = "";
    String fileServerIp = "";
    int DEFAULT_PAGE = 1;
    LinkedHashMap<String, ShareBucketItem> openedFileSet = new LinkedHashMap<>();

    //Constructor for Instructor
    public ShareBucket(){

    }
    //-- Constructor for Instructor


    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void add(String file, ShareBucketItem item){
        openedFileSet.put(file, item);
    }

    public void popFile(String file) { openedFileSet.remove(file); }

    public void updatePageNo(String file, int pageNo){
        ShareBucketItem item  = openedFileSet.get(file);
        item.setPageNo(pageNo);
        openedFileSet.put(file, item);
    }

    public LinkedHashMap<String, Integer> getOpenedFileSet(){
        LinkedHashMap<String, Integer> set = new LinkedHashMap<String, Integer>();
        for(String filePath : openedFileSet.keySet()){
            ShareBucketItem item = openedFileSet.get(filePath);
            set.put(filePath, item.getPageNo());
        }
        return set;
    }

    public Collection<String> getFiles(){
        return openedFileSet.keySet();
    }

    public Collection<Integer> getPageNos(){
        Collection<Integer> pageNos = new ArrayList<>();
        Collection<String> files = openedFileSet.keySet();
        for(String s : files){
            ShareBucketItem item = openedFileSet.get(s);
            pageNos.add(item.getPageNo());
        }
        return pageNos;
    }

    public void setCurrentFile(String filePath, int pageNo){
        currentFile = filePath;
        updatePageNo(filePath, pageNo);
    }

    public void setCurrentFile(String filePath){
        currentFile = filePath;
    }

    public int getCurrFilePage(){
        int a = openedFileSet.get(currentFile).getPageNo();
        return  a;
    }

    public String getCurrentFile(){
        return currentFile;
    }

    public void createFromJSON(JSONObject main){
        //Decode JSON and create ShareBucket
        try {
            String sessionId = main.getString(Constants.JSON_FB_SESSION_ID);
            JSONArray arrFiles = main.getJSONArray(Constants.JSON_FILES);
            JSONArray arrPageNos = main.getJSONArray(Constants.JSON_PAGE_NOS);
            String currentFile = main.getString(Constants.JSON_CURR_FILE);
            String fileServerIp = main.getString(Constants.JSON_SERVER_IP);

            init();

            for(int i=0; i<arrFiles.length(); i++){

                ShareBucketItem item = new ShareBucketItem();
                item.setDownloadFlag(false);
                item.setPageNo(arrPageNos.getInt(i));
                item.setView(null);
                item.setFileName((new File(arrFiles.getString(i)).getName()));
                item.setFilePath(arrFiles.getString(i));
                item.setThumbnail(null);

                add(arrFiles.getString(i), item);
                //updatePageNo(arrFiles.getString(i) , arrPageNos.getInt(i) );
            }

            setSessionId(sessionId);
            setCurrentFile(currentFile);
            setFileServerIp(fileServerIp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //-- Decode JSON and create ShareBucket


        /*
        *
        * Remaining Code:-
         * Bucket created callback
        *
         */
    }

    public boolean isFileDownloaded(String filePath){
        return openedFileSet.get(filePath).getDownloadFlag();
    }

    public View getView(String filePath){
        return openedFileSet.get(filePath).getView();
    }

    public boolean contains(String filePath){
        return openedFileSet.containsKey(filePath);
    }

    public void setDownloadFlag( String filePath){
        openedFileSet.get(filePath).setDownloadFlag(true);
    }

    public void deleteData(){
        File f = new File(Constants.DIR_ROOT);
        deleteRecursive(f);
    }

    private void deleteRecursive(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteRecursive(f);
            }
        }
        file.delete();
    }

    private void init(){
        openedFileSet.clear();
    }

    public String getFileServerIp() {
        return fileServerIp;
    }

    public void setFileServerIp(String fileServerIp) {
        this.fileServerIp = fileServerIp;
    }

    public ShareBucketItem getItemAt(int index){
        ArrayList<String> al = new ArrayList<>(openedFileSet.keySet());
        ShareBucketItem item = openedFileSet.get(al.get(index));
        return item;
    }

    /*private boolean isStudent(){
        return ((callback != null) ? true : false);
    }*/
    /*public void testCode(){
        add("/folder/fist.pdf");
        add("/folder/tick.pdf");
        add("/folder/tock.pdf");
        add("/folder/clock.pdf");
        setCurrentFile("/folder/tock.pdf");
    }*/

    /*public interface ShareBucketCallback{
        void onPageChanged(String filePath, int pageNo);
        void onFileChanged(String filePath, int pageNo);
        void onFileAdded(String filePath);

    }*/
}
