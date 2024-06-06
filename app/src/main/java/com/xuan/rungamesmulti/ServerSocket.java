package com.xuan.rungamesmulti;

import android.app.Instrumentation;
import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class ServerSocket extends WebSocketServer {

    final private ServerManager _serverManager;
    String[] usrMessage;
    String usr;
    String consoleMessage;

    public int isOn = 0;

    public ServerSocket(ServerManager serverManager, int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
        _serverManager=serverManager;

    }

    /**
     * Websocket打开时自动登录
     * @param conn The <tt>WebSocket</tt> instance this event is occuring on.
     * @param handshake The handshake of the websocket instance
     */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {

        // username
        _serverManager.UserLogin(conn);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        _serverManager.UserLeave(conn);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
//        Log.i("TAGTest","收到消息啦:" + message);

//        SMessage = message.toString();
        usrMessage = message.split(":");
        usr = usrMessage[0]; // 获取当前用户
        consoleMessage = usrMessage[1];
        Log.i("TAGTest","当前用户" + usr + ",收到信息:" + consoleMessage);

        if(consoleMessage.equals("ok") ){
            isOn++;
            if(isOn == 2){
                _serverManager.SendMessageToAll("action");
                simulateKeystroke(23);
                Log.i("TAGTest","用户模型准备好了，可以进入游戏");

            }
        }
        if(usr.equals("first")){
            switch (consoleMessage) {
                case "right":
                    simulateKeystroke(32);
                    Log.i("TAGTest","1r" );
                    break;
                case "left":
                    simulateKeystroke(29);
                    Log.i("TAGTest","1l" );
                    break;
                case "up":
                    simulateKeystroke(51);
                    Log.i("TAGTest","1u" );
                    break;
                case "down":
                    simulateKeystroke(47);
                    Log.i("TAGTest","1d" );
                    break;
                default:
                    break;
            }
        }
        if(usr.equals("second")){
            switch (consoleMessage) {
                case "goGame":
                    simulateKeystroke(23);
                    Log.i("TAGTest","2位选手就位，可以进入游戏了");
                    break;
                case "right":
                    simulateKeystroke(22);
                    Log.i("TAGTest","2r" );
                    break;
                case "left":
                    simulateKeystroke(21);
                    Log.i("TAGTest","2l" );
                    break;
                case "up":
                    simulateKeystroke(19);
                    Log.i("TAGTest","2u" );
                    break;
                case "down":
                    simulateKeystroke(20);
                    Log.i("TAGTest","2d" );
                    break;
                default:
                    break;
            }
        }





    }

    /**
     * 加入遥控api
     */
    public  void simulateKeystroke(final int KeyCode) {
        new Thread(new Runnable() {

            public void run() {

                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendCharacterSync(KeyCode);
                } catch (Exception e) {
                    Log.i("TAGTest","发生错误");
                }
            }
        }).start();
    }
    @Override
    public void onError(WebSocket conn, Exception ex) {
        Log.i("TAG","错误Socket Exception:" + ex.toString());

    }

    @Override
    public void onStart() {

    }
}
