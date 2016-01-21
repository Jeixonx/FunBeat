package co.edu.udea.cmovil.gr5.funbeat.funbeat;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class Ayuda extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);
        mediaPlayer = MediaPlayer.create(this, R.raw.techno);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
    }

}
