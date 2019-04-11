package com.tayaraak.quranbyheart;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    MediaPlayer player;
    Button playPauseBtn;
    Button stopBtn;
    Button fwdBtn;
    Button rwdBtn;
    SeekBar seekBar;
    Runnable runnable;
    Handler handler;
    TextView playerValueTv;
    TextView seekBarTv;

    String TAG = "Main Activity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playPauseBtn = (Button) findViewById(R.id.btnPlayPause);
        stopBtn = (Button) findViewById(R.id.btnStop);
        fwdBtn = (Button) findViewById(R.id.btnFor);
        rwdBtn = (Button) findViewById(R.id.btnRew);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        playerValueTv = (TextView) findViewById(R.id.PlayerValue);
        seekBarTv = (TextView) findViewById(R.id.seekBarValue);
        handler = new Handler();

        player = MediaPlayer.create(this, R.raw.alnajm);

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax(player.getDuration());
                changeSeekBar();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    Log.d(TAG, "onProgressChanged: " + progress);
                    player.seekTo(progress);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopPlayer();
                    }
                });

                if (player.isPlaying()){
                    player.pause();
                    playPauseBtn.setText("Play");
                }else {
                    player.start();
                    player.seekTo(seekBar.getProgress());
                    changeSeekBar();
                    playPauseBtn.setText("Pause");

                }
            }
        });



        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               stop();
            }
        });

        fwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player!=null){
                    player.seekTo(player.getCurrentPosition()+5000);
                }
            }
        });
        rwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player != null){
                    player.seekTo(player.getCurrentPosition()-5000);
                }
            }
        });

    }

    public void stop(){
        if (player != null){
            stopPlayer();
        }
    }

    public void stopPlayer(){
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }
    private void changeSeekBar() {
        if (player != null){
            seekBar.setProgress(player.getCurrentPosition());

            playerValueTv.setText( String.valueOf(player.getCurrentPosition()/1000));
            seekBarTv.setText(String.valueOf(seekBar.getProgress()/1000));
        }

        if (player !=null && player.isPlaying()){
            runnable = new Runnable() {
                @Override
                public void run() {
                    changeSeekBar();
                }
            };
            handler.postDelayed(runnable, 1000);

        }

    }
}
