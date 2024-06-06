package com.xuan.rungamesmulti;

import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ServerManager {
    private ServerSocket serverSocket=null;
    private Map<WebSocket, String> userMap=new HashMap<WebSocket, String>();

    private int userCount = 0;

    public String userName;
    public ServerManager(){

    }

    public void UserLogin(WebSocket socket){
        if (socket!=null) {
            userCount++;
            Log.i("TAGTest", String.valueOf(userCount));

            if(userCount % 2 == 1){
                userName = "left";
                userMap.put(socket, userName);
                Log.i("TAGTest","LOGIN:"+userName);
                SendMessageToUser(userName,userName);
            }else if(userCount % 2 ==0){
                userName = "right";
                userMap.put(socket, userName);
                Log.i("TAGTest","LOGIN:"+userName);
                SendMessageToUser(userName,userName);
            }
        }
    }

    public void UserLeave(WebSocket socket){
        if (userMap.containsKey(socket)) {
            String userName=userMap.get(socket);
            Log.i("TAGTest","Leave:"+userName);
            userMap.remove(socket);
            SendMessageToAll(userName+"...Leave...");
        }
    }

    public void SendMessageToUser(WebSocket socket, String message){
        if (socket!=null) {
            socket.send(message);
        }
    }

    public void SendMessageToUser(String userName, String message){
        Set<WebSocket> ketSet=userMap.keySet();
        for(WebSocket socket : ketSet){
            String name=userMap.get(socket);
            if (name!=null) {
                if (name.equals(userName)) {
                    socket.send(message);
                    break;
                }
            }
        }
    }

    public void SendMessageToAll(String message){
        Set<WebSocket> ketSet=userMap.keySet();
        for(WebSocket socket : ketSet){
            String name=userMap.get(socket);
            if (name!=null) {
                socket.send(message);
            }
        }
    }

    /**
     * 启动websocket服务
     * @param port
     * @return
     */
    public boolean Start(int port){

        if (port<0) {
            Log.i("TAGTest","Port error...");
            return false;
        }

        WebSocketImpl.DEBUG=false;  // 禁用调试模式
        try {
            serverSocket=new ServerSocket(this,port);
            serverSocket.start();
            Log.i("TAGTest","Start ServerSocket Success...");
            return true;
        } catch (Exception e) {
            Log.i("TAGTest","Start Failed...");
            e.printStackTrace();
            return false;
        }
    }

    public boolean Stop(){
        try {
            serverSocket.stop();
            Log.i("TAGTest","Stop ServerSocket Success...");
            return true;
        } catch (Exception e) {
            Log.i("TAGTest","Stop ServerSocket Failed...");
            e.printStackTrace();
            return false;
        }
    }


}
