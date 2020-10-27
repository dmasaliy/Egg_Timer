package com.game.egg_timer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("SetTextI18n")

public class MainActivity extends AppCompatActivity {
    public static final Handler HANDLER = new Handler();
    TextView timeEt;
    Button start_stop;
    Button restart;
    boolean started;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start_stop = findViewById(R.id.start_stop);
        timeEt = findViewById(R.id.timeEt);
        restart = findViewById(R.id.restart);

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restart(view);
            }
        });

        start_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });
    }

    public void start() {
        if (started) {
            started = false;
            start_stop.setText(R.string.start);
            timeEt.setCursorVisible(true);

            countDownTimer.cancel();
        } else {
            started = true;
            start_stop.setText(R.string.stop);
            timeEt.setCursorVisible(false);

            String rawTime = timeEt.getText().toString();
            String[] arr = rawTime.split(":");

            long time = 60 * 1000;

            try {
                time = (Long.parseLong(arr[0]) * 60 * 1000) + (Long.parseLong(arr[1]) * 1000);
            } catch (Exception e) {
                timeEt.setText(R.string.default_time);
            }

            countDownTimer = new CountDownTimer(time, 1000) {

                @Override
                public void onTick(long l) {
                    long remaininSeconds = l / 1000;
                    long min = remaininSeconds / 60;
                    long seconds = remaininSeconds % 60;

                    timeEt.setText(min + ":" + (seconds < 10 ? "0" + seconds : seconds));
                }

                @Override
                public void onFinish() {
                    timeEt.setText(R.string.start_time);
                    start_stop.setText(R.string.start);

                    new AlertDialog.Builder(MainActivity.this).
                            setTitle(R.string.app_name).
                            setMessage(R.string.go_eat).show();

                    HANDLER.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            timeEt.setText(R.string.default_time);
                        }
                    },1500);
                }
            };

            countDownTimer.start();
        }
    }

    public void restart(View view) {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
}