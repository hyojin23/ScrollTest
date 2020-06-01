package com.example.scrolltest;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.print.PrinterId;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private Button scrollDown, scrollUp, autoScroll;
    private String IP = "192.168.35.159";
    private int PORT = 8700;
    private View gazePointer;
    private final static String TAG = "MainActivity";
    private boolean autoScrollClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.web_view);
        scrollDown = findViewById(R.id.scroll_down);
        scrollUp = findViewById(R.id.scroll_up);
        autoScroll = findViewById(R.id.auto_scroll);
        gazePointer = findViewById(R.id.gazePointer);

        webView.setWebViewClient(new WebViewClient());

        // 자바스크립트 허용
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.youtube.com");

        // 아래로 스크롤하는 애니메이션
        scrollDown.setOnClickListener(new View.OnClickListener() {
            int bottomTo = 0;

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 아래로 스크롤 버튼 클릭됨");
                Log.d(TAG, "onClick: webView.getScrollY(): " + webView.getScrollY());
                bottomTo = webView.getScrollY() + 850;
                // ofInt(애니메이션 적용 대상, 애니메이션 종류, 애니메이션 시작점(스크롤 출발위치), 애니메이션 끝점(스크롤 도착위치))
                ObjectAnimator anim = ObjectAnimator.ofInt(webView, "scrollY", webView.getScrollY(), bottomTo);
                anim.setDuration(1000).start();
            }
        });

        // 위로 스크롤하는 애니메이션
        scrollUp.setOnClickListener(new View.OnClickListener() {
            int upTo = 0;

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 위로 스크롤 버튼 클릭됨");
                Log.d(TAG, "onClick: webView.getScrollY(): " + webView.getScrollY());

                // 현재 스크롤 위치에 따라 스크롤 도착지점을 다르게 만듦. 스크롤 도착지점이 마이너스가 되면
                // webView.getScrollY()이 마이너스가 되어 scroll down을 여러번해야 스크롤이 내려가게됨
                if (webView.getScrollY() > 850) {
                    upTo = webView.getScrollY() - 850;
                } else {
                    upTo = 0;
                }

                ObjectAnimator anim = ObjectAnimator.ofInt(webView, "scrollY", webView.getScrollY(), upTo);
                anim.setDuration(1000).start();
            }

        });

        // 자동스크롤 버튼을 눌렀을 때
        autoScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocketAsync socketAsync = new SocketAsync(gazePointer, scrollDown, scrollUp);
                // 다시 클릭할 때
                if (autoScrollClicked) {
                    Log.d(TAG, "onClick: autoScroll 다시 클릭함");
                    socketAsync.cancel(true);
                    Log.d(TAG, "onClick: 취소여부: " + socketAsync.isCancelled());
                // 처음 클릭할 때
                } else {
                    socketAsync.execute(IP, PORT + "");
                    gazePointer.setVisibility(View.VISIBLE);

                    float x = gazePointer.getX();
                    float y = gazePointer.getY();
                    autoScrollClicked = true;
                }
            }
        });

    }


    // 뒤로가기 버튼 눌렀을 때
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            Log.d(TAG, "onBackPressed: 웹뷰 안에서 뒤로 감");
            webView.goBack();
        } else {
            Log.d(TAG, "onCreate: 뒤로 못감");
            super.onBackPressed();
        }
    }
}
