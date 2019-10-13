package com.androidperformance.gameprofile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button bt;

    //Server
    Socket            mSocket;
    OutputStream      mOutputStream;
    InputStream       mInputStream;

    InputStreamReader mInputStreamReader ;
    BufferedReader    mBufReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = findViewById(R.id.button);
        bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        new Thread() {
            @Override
            public void run() {
                try {
                    //1.创建监听指定服务器地址以及指定服务器监听的端口号
                    mSocket = new Socket(InetAddress.getLocalHost(), 6666);
                    //2.拿到客户端的socket对象的输出流发送给服务器数据
                    mOutputStream = mSocket.getOutputStream();
                    //写入要发送给服务器的数据
                    mOutputStream.write("helloWorld".getBytes());
                    mOutputStream.flush();
                    mSocket.shutdownOutput();
                    //拿到socket的输入流，这里存储的是服务器返回的数据
                    mInputStream = mSocket.getInputStream();
                    //解析服务器返回的数据
                    mInputStreamReader = new InputStreamReader(mInputStream);
                    mBufReader = new BufferedReader(mInputStreamReader);
                    String s = null;
                    final StringBuffer sb = new StringBuffer();
                    while ((s = mBufReader.readLine()) != null) {
                        sb.append(s);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("Gracker", sb.toString());
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        mInputStreamReader.close();
                        mBufReader.close();
                        mInputStream.close();
                        mOutputStream.close();
                        mSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
