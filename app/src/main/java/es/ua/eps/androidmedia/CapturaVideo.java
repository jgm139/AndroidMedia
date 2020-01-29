package es.ua.eps.androidmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

public class CapturaVideo extends AppCompatActivity implements SurfaceHolder.Callback {

    // Referencia a los botones del layout
    private Button btn_grabar;
    private Button btn_parar;

    // SurfaceView
    SurfaceView superficie;
    SurfaceHolder m_holder;
    MediaRecorder mediaRecorder;
    Camera mCamera;

    boolean preparado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captura_video);

        this.superficie = findViewById(R.id.superficie_captura);
        this.btn_parar = findViewById(R.id.parar);
        this.btn_grabar = findViewById(R.id.grabar);

        btn_grabar.setOnClickListener(new ManejadorBotonGrabar());
        btn_parar.setOnClickListener(new ManejadorBotonParar());

        // Petición de permisos al usuario
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA}, 29);
            } else {
                Log.d("Home", "Already granted access");
            }
        }

        mCamera = getCameraInstance();
        if(mCamera != null) {
            mCamera.setDisplayOrientation(90);
        } else {
            Log.d("CAMERA", "null");
        }

        mediaRecorder = new MediaRecorder();

        m_holder = superficie.getHolder();
        m_holder.addCallback(this);
        m_holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    protected Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
            Log.d("CAMERA", "null");
        }
        return c;
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    private class ManejadorBotonParar implements View.OnClickListener {
        public void onClick(View v) {
            btn_parar.setEnabled(false);
            btn_grabar.setEnabled(true);

            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
        }
    };

    private class ManejadorBotonGrabar implements View.OnClickListener {
        public void onClick(View v) {

            if(preparado) {
                btn_parar.setEnabled(true);
                btn_grabar.setEnabled(false);

                configurar(m_holder);

                try {
                    mediaRecorder.start();
                } catch (Exception e) {
                    Log.d("MEDIA_PLAYER", e.getMessage());
                }
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mediaRecorder.setPreviewDisplay(m_holder.getSurface());
        preparado = true;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mediaRecorder.release();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    private void configurar(SurfaceHolder holder) {
        try {

            // Inicializando el objeto MediaRecorder
            mediaRecorder = new MediaRecorder();

            if (mCamera != null) {
                mCamera.unlock();
                mediaRecorder.setCamera(mCamera);
            }

            // Configuración de las fuentes de entrada
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

            // Formato de salida
            // 3GPP media file format
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

            // Seleccionamos el codec de audio y vídeo
            //mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            //mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);// MPEG_4_SP
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"android_media_video.mp4");

            mediaRecorder.setOutputFile(file.getPath());

            mediaRecorder.setVideoSize(640, 480);
            mediaRecorder.setVideoFrameRate(16); //might be auto-determined due to lighting
            mediaRecorder.setVideoEncodingBitRate(3000000);

            mediaRecorder.setPreviewDisplay(holder.getSurface());

            mediaRecorder.prepare();

        } catch (IllegalArgumentException e) {
            Log.d("MEDIA_PLAYER", e.getMessage());
        } catch (IllegalStateException e) {
            Log.d("MEDIA_PLAYER", e.getMessage());
        } catch (IOException e) {
            Log.d("MEDIA_PLAYER", e.getMessage());
        }
    }
}
