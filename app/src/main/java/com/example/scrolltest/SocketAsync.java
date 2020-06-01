package com.example.scrolltest;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketAsync extends AsyncTask<String, String, Boolean> {

    private final static String TAG = "socketAsync";
    private Socket socket;
    private View gazePointer;
    private Button scrollDown, scrollUp;

    public SocketAsync(View gazePointer, Button scrollDown, Button scrollUp) {
        this.gazePointer = gazePointer;
        this.scrollDown = scrollDown;
        this.scrollUp = scrollUp;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            Thread.sleep(1000);
            String IP = strings[0];
            int PORT = Integer.parseInt(strings[1]);
            Log.d(TAG, IP + ":" + PORT);
            Log.d(TAG, "onImageAvailable: 소켓 연결 대기중 IP: " + IP + " PORT: " + PORT);
            socket = new Socket(IP, PORT);
            Log.d(TAG, "onImageAvailable: 소켓 연결 성공 IP: " + IP + " PORT: " + PORT);

            DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream inStream = new DataInputStream(socket.getInputStream());
            // 데이터를 모아뒀다가 한줄씩 출력하기 위해 BufferedReader 사용
            BufferedReader br = new BufferedReader(new InputStreamReader(inStream));

            Log.d(TAG, "WHILE");
            while (!isCancelled()) {
                Thread.sleep(1000);

                Log.d(TAG, "READ");
                // 서버에서 받은 데이터를 한줄씩 읽음
                String msg = br.readLine();
                publishProgress(msg);
                Log.d(TAG, "doInBackground: msg: " + msg);
            }

        } catch (Exception e) {

            Log.e("SERVER_TAG", String.valueOf(e));
            e.printStackTrace();

        } finally {
            try {
                Log.d(TAG, "doInBackground: 소켓 종료");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void onProgressUpdate(String... values) {
        Log.d(TAG, "onProgressUpdate: 실행");
        Log.d(TAG, "onProgressUpdate: values[0]: " + values[0]);
        String[] coordinates = values[0].split("/");
        Log.d(TAG, "onProgressUpdate: x값: " + coordinates[0]);
        Log.d(TAG, "onProgressUpdate: y값: " + coordinates[1]);
        int x = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);
        int click = Integer.parseInt(coordinates[2]);
        Log.d(TAG, "onProgressUpdate: x 좌표: " + x + " y 좌표: " + y + " click: " + click);
        gazePointer.setX(x);
        gazePointer.setY(y);
        if (click == 1) {
//                EconUtils.gazeTouchMotion(webView,x,y, MotionEvent.ACTION_DOWN);
//                EconUtils.gazeTouchMotion(webView,x,y,MotionEvent.ACTION_UP);
        }

//        if (x > 800) {
            scrollDown.callOnClick();
//        } else {
//            scrollUp.callOnClick();
//        }

        super.onProgressUpdate(values);
    }


    @Override
    public void onPostExecute(Boolean aBoolean) {
        Log.d(TAG, "onPostExecute: 실행");
        try {
            Log.d(TAG, "onPostExecute: 소켓 종료");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.onPostExecute(aBoolean);
    }
}
