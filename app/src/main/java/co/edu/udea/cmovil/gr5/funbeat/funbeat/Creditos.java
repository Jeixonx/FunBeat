package co.edu.udea.cmovil.gr5.funbeat.funbeat;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class Creditos extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditos);
        tv1 = (TextView) findViewById(R.id.textViewCreditos1);

        mediaPlayer = MediaPlayer.create(this, R.raw.riseup);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        Animation creditroll= AnimationUtils.loadAnimation(this, R.anim.animationcredits);
        //para que los creditos se muevan como en las peliculas
        tv1.setText("Creado por:\n\n\n" +
                "" +
                "Jeison Triana Amaya\n" +
                "Imran Mirza Orozco\n" +
                "" + "\n\n" +
                "Universidad de Antioquia");
        tv1.startAnimation(creditroll);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
    }
}
