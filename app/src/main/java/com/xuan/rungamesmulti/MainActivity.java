package com.xuan.rungamesmulti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {

    private ServerManager serverManager;
    private ImageView enCodeImage;
    private ConnectivityManager mConnectivityManager = null;
    private NetworkInfo mActiveNetInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serverManager=new ServerManager();
        serverManager.Start(8800);
//        Log.i("TAGTest","端口打开");
        // 打开websocket服务
        TextView textView = findViewById(R.id.btn_open_websocket);
        textView.setText("端口服务已打开");

        // 获取ip服务
        mConnectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);//获取系统的连接服务
        mActiveNetInfo = mConnectivityManager.getActiveNetworkInfo();//获取网络连接的信息

        // 二维码生成
        enCodeImage = (ImageView)findViewById(R.id.code_image) ;
        String context = getIPAddress();
        Bitmap codeBitmap = EncodingUtils.createQRCode(context,500,500);
        enCodeImage.setImageBitmap(codeBitmap);


    }
    /**
     * 两秒内连续两次返回键，销毁app
     * system.exit 销毁整个进程
     */
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                serverManager.Stop();
                // 关闭当前页面
                finish();
                // 彻底摧毁
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void goGame(View view){
        Intent intent = new Intent(this, RunGames.class);
//        Intent intent = new Intent(this, test.class);

        Log.i("TAGTest","游戏打开");
        startActivity(intent);
    }

    public String getIPAddress() {
        NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if ((info.getType() == ConnectivityManager.TYPE_MOBILE) || (info.getType() == ConnectivityManager.TYPE_WIFI) ){//当前使用2G/3G/4G网络
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                }
                catch (SocketException e) {
                    e.printStackTrace();
                }
            }
        }
        else { //当前无网络连接,请在设置中打开网络
            return null;
        }
        return null;
    }



}
