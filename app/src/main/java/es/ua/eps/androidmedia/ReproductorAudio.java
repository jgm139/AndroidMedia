package es.ua.eps.androidmedia;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class ReproductorAudio extends AppCompatActivity {

    // Referencia a los botones del layout
    private Button btn_play_audio;
    private Button btn_pause_audio;
    private Button btn_stop_audio;

    // Media Player
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor_audio);

        mp = MediaPlayer.create(this, R.raw.final_fantasy_ix);

        inicializarBotones();
    }

    private void inicializarBotones() {
        btn_play_audio = findViewById(R.id.btn_play_audio);
        btn_pause_audio = findViewById(R.id.btn_pause_audio);
        btn_stop_audio = findViewById(R.id.btn_stop_audio);

        btn_play_audio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btn_pause_audio.setEnabled(true);
                btn_stop_audio.setEnabled(true);
                btn_play_audio.setEnabled(false);

                mp.start();

            }
        });

        btn_pause_audio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(mp.isPlaying()) {
                    btn_pause_audio.setEnabled(false);
                    btn_stop_audio.setEnabled(true);
                    btn_play_audio.setEnabled(true);
                    mp.pause();
                }

            }
        });

        btn_stop_audio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btn_pause_audio.setEnabled(false);
                btn_stop_audio.setEnabled(false);
                btn_play_audio.setEnabled(true);

                mp.stop();

                try {
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mp != null) {
            mp.release();
            mp = null;
        }
    }
}
