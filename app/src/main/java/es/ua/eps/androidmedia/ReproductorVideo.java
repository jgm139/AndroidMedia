package es.ua.eps.androidmedia;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class ReproductorVideo extends AppCompatActivity {

    // Referencia a los botones del layout
    private Button btn_play_video;
    private Button btn_pause_video;
    private Button btn_stop_video;

    // Tiempo y duración del duration
    private TextView duration;
    private ProgressBar progressBar;
    private int time = 0;

    // VideoView
    private VideoView superficie;
    private boolean isPaused = false;

    private MyAsync myAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor_video);

        this.btn_play_video = findViewById(R.id.btn_play_video);
        this.btn_pause_video = findViewById(R.id.btn_pause_video);
        this.btn_stop_video = findViewById(R.id.btn_stop_video);
        this.duration = findViewById(R.id.tiempo);
        this.progressBar = findViewById(R.id.progressBar);
        this.superficie = findViewById(R.id.superficie_video);

        duration.setText("Duración 0");
        progressBar.setProgress(0);
        progressBar.setMax(100);

        this.btn_play_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_play_video.setEnabled(false);
                btn_stop_video.setEnabled(true);
                btn_pause_video.setEnabled(true);

                if (isPaused) {
                    superficie.start();
                } else {
                    superficie.setKeepScreenOn(true);
                    superficie.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.cat));
                    myAsync = (MyAsync) new MyAsync().execute();
                }

                isPaused = false;
            }
        });

        this.btn_stop_video.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btn_play_video.setEnabled(true);
                btn_stop_video.setEnabled(false);
                btn_pause_video.setEnabled(true);

                myAsync.cancel(true);
                superficie.stopPlayback();

                duration.setText("Duración 0");
            }
        });

        this.btn_pause_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(superficie.isPlaying()) {
                    btn_play_video.setEnabled(true);
                    btn_stop_video.setEnabled(true);
                    btn_pause_video.setEnabled(false);

                    superficie.pause();
                    isPaused = true;
                }
            }
        });

        superficie.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                superficie.start();
                time = superficie.getDuration();
                int segundos = (time / 1000) % 60;
                int minutos = (time / 1000) / 60;
                duration.setText("Duración " + minutos + ":" + segundos);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myAsync != null) {
            myAsync.cancel(true);
        }
    }

    private class MyAsync extends AsyncTask<Void, Integer, Void> {
        int current = 0;

        @Override
        protected Void doInBackground(Void... params) {
            time = superficie.getDuration();

            do {
                current = superficie.getCurrentPosition();
                try {
                    publishProgress((int) (current * 100 / time));
                    if(progressBar.getProgress() >= 100){
                        break;
                    }
                } catch (Exception e) {

                }
            } while (progressBar.getProgress() <= 100);

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
            progressBar.setProgress(0);
        }
    }
}
