package com.tayaraak.quranbyheart;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MediaPlayer player;

    Button btnPlayPause;
    Button btnStop;
    Button btnFor;
    Button btnRew;
    Button btnStart;
    Button btnEnd;
    Button btnRepeat;

    SeekBar seekBar;

    Runnable runnable;
    Handler handler;

    TextView tvCurrentPosition;
    TextView tvTotalDuration;
    TextView startTv;
    TextView endTv;

    int startPosition = 0;
    int endPosition = 0;
    boolean repeat = false;
    boolean isRepeating = false;


    String TAG = "Main Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlayPause = (Button) findViewById(R.id.btnPlayPause);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnFor = (Button) findViewById(R.id.btnFor);
        btnRew = (Button) findViewById(R.id.btnRew);
        btnRepeat = (Button) findViewById(R.id.btnRepeat);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        startTv = (TextView) findViewById(R.id.startTv);
        endTv = (TextView) findViewById(R.id.endTv);
        btnStart = (Button) findViewById(R.id.startBtn);
        btnEnd = (Button) findViewById(R.id.endBtn);
        tvCurrentPosition = (TextView) findViewById(R.id.tvCurrentPosition);
        tvTotalDuration = (TextView) findViewById(R.id.tvTotalDuration);
        handler = new Handler();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (player != null) {
                        player.seekTo(progress);
                        changeSeekBar();
                    }
                }
                else {
                    if (repeat && player.getCurrentPosition() >= endPosition){
                        player.seekTo(startPosition);
                }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnPlayPause.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnFor.setOnClickListener(this);
        btnRew.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnEnd.setOnClickListener(this);
        btnRepeat.setOnClickListener(this);


    }

    public void stopPlayer() {
        if (player != null) {
            seekBar.setProgress(0);
            player.seekTo(0);
            tvCurrentPosition.setText(String.valueOf(0));
            player.release();


            player = null;
            btnPlayPause.setText("Play");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.btnPlayPause):
                playPause();

                break;
            case (R.id.btnStop):
                stopPlayer();

                break;
            case (R.id.btnFor):
                if (player != null) {
                    player.seekTo(player.getCurrentPosition() + 5000);
                }
                break;
            case (R.id.btnRew):
                if (player != null) {
                    player.seekTo(player.getCurrentPosition() - 5000);
                }
                break;
            case (R.id.startBtn):
                if (player != null){
                   startPosition = player.getCurrentPosition();
                   startTv.setText(String.valueOf(startPosition));
                }
                break;
            case (R.id.endBtn):
            if (player != null){
                endPosition = player.getCurrentPosition();
                endTv.setText(String.valueOf(endPosition));
            }
            break;
            case (R.id.btnRepeat):
            repeatSegment();

            break;
        }


    }

    private void playPause(){

        if (player == null) {
            player = MediaPlayer.create(this, R.raw.salat);
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    seekBar.setMax(player.getDuration());
                    tvTotalDuration.setText(String.valueOf(player.getDuration()/1000));
                }
            });
            player.start();
            changeSeekBar();
            btnPlayPause.setText("Pause");
        }else
        if (player.isPlaying()) {
            player.pause();
            btnPlayPause.setText("Play");
        } else {
            player.start();
            btnPlayPause.setText("Pause");

        }

    }
    private void changeSeekBar() {
        if (player != null) {
            seekBar.setProgress(player.getCurrentPosition());
            tvCurrentPosition.setText("" + player.getCurrentPosition()/1000);



        }

        if (player != null && player.isPlaying()) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    changeSeekBar();
                }
            };
            handler.postDelayed(runnable, 1000);

        }

    }

    private void repeatSegment (){


        if (player != null){

            if (repeat) {
                repeat = false;
                startTv.setText("");
                endTv.setText("");
            } else {
                repeat = true;
                player.seekTo(startPosition);
            }



        }
    }
}
