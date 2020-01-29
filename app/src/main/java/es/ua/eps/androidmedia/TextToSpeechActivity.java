package es.ua.eps.androidmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Locale;

public class TextToSpeechActivity extends AppCompatActivity {

    // Referencia a los botones y el texto del layout
    private RadioButton radio_spanish;
    private RadioButton radio_english;
    private EditText editText;
    private Button btn_read;

    // TextToSpeech
    private static int TTS_DATA_CHECK = 1;
    private TextToSpeech tts = null;
    private boolean ttsIsInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech);

        this.radio_spanish = findViewById(R.id.radio_spanish);
        this.radio_english = findViewById(R.id.radio_english);
        this.editText = findViewById(R.id.editText);
        this.btn_read = findViewById(R.id.btn_read);

        this.radio_spanish.setChecked(true);

        this.btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak(editText.getText().toString());
            }
        });

        this.radio_spanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_spanish.setChecked(true);
                radio_english.setChecked(false);
                tts.setLanguage(new Locale("es","",""));
            }
        });

        this.radio_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_spanish.setChecked(false);
                radio_english.setChecked(true);
                tts.setLanguage(new Locale("en","",""));
            }
        });

        Intent intent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intent, TTS_DATA_CHECK);
    }

    @TargetApi(Build.VERSION_CODES.DONUT)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TTS_DATA_CHECK) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            ttsIsInit = true;
                            Locale loc = new Locale("es","","");
                            if (tts.isLanguageAvailable(loc)
                                    >= TextToSpeech.LANG_AVAILABLE)
                                tts.setLanguage(loc);
                            tts.setPitch(0.8f);
                            tts.setSpeechRate(1.1f);
                        }
                    }
                });
            } else {
                Intent installVoice = new Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installVoice);
            }
        }

    }

    private void speak(String texto) {
        if (tts != null && ttsIsInit) {
            tts.speak(texto, TextToSpeech.QUEUE_ADD, null);
        }
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }

}
