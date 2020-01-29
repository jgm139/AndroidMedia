package es.ua.eps.androidmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // Referencia a los botones del layout
    private Button btn_reproductor_audio;
    private Button btn_reproductor_video;
    private Button btn_captura_video;
    private Button btn_text_to_speech;
    private Button btn_reconocimiento_habla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btn_reproductor_audio = findViewById(R.id.btn_reproductor_audio);
        this.btn_reproductor_video = findViewById(R.id.btn_reproductor_video);
        this.btn_captura_video = findViewById(R.id.btn_captura_video);
        this.btn_text_to_speech = findViewById(R.id.btn_text_to_speech);
        this.btn_reconocimiento_habla = findViewById(R.id.btn_reconocimiento_habla);

        this.btn_reproductor_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReproductorAudio.class);
                startActivity(intent);
            }
        });

        this.btn_reproductor_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReproductorVideo.class);
                startActivity(intent);
            }
        });

        this.btn_captura_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CapturaVideo.class);
                startActivity(intent);
            }
        });

        this.btn_text_to_speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TextToSpeechActivity.class);
                startActivity(intent);
            }
        });

        this.btn_reconocimiento_habla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SpeechRecognizerActivity.class);
                startActivity(intent);
            }
        });
    }
}
