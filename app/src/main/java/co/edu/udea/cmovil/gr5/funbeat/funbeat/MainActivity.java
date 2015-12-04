package co.edu.udea.cmovil.gr5.funbeat.funbeat;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;



public class MainActivity extends AppCompatActivity /*implements View.OnTouchListener*/ {

    //definiciones utiles para el soundpool
    private  int mStream1 = 0;
    private  int mStream2 = 0;
    final static int LOOP_1_TIME = 0;
    final static int LOOP_3_TIMES = 2;
    final static int SOUND_FX_01 = 1;
    final static int SOUND_FX_02 = 2;

    //para arrancar y parar el metronomo
    private boolean  isTimerOn = false;

    //ultima vez que hizo tick el metronomo
    long ultimotiempo = 0;
    long penultimoTiempo = 0;

    //velocidad del metronomo, y tamaño del buffer para la dificultad, en este momento se modifican desde la pantalla.
    int velocidad;
    int buffer;

    //variables para mover el personaje en pantalla
    ImageView personaje;
    private ViewGroup mRrootLayout;
    private int _xDelta;
    private int _yDelta;

    private TextView muestraX;
    private TextView muestraY;

    int ticks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Timer[] timer = new Timer[2];


        //inicializacion del soundpool
        final SoundPool mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        final AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        final HashMap mSoundPoolMap = new HashMap();

        mSoundPoolMap.put(1, mSoundPool.load(this, R.raw.bluefoot, 1));
        mSoundPoolMap.put(2, mSoundPool.load(this, R.raw.snare, 1));
        mSoundPoolMap.put(3, mSoundPool.load(this, R.raw.jamblock, 1));



        //definicion logica de la gui
        final Button boton1 = (Button) findViewById(R.id.button);
        final Button boton2 = (Button) findViewById(R.id.button2);
        final Button botonMetronomo = (Button) findViewById(R.id.button3);
        final Button botonBuffer = (Button) findViewById(R.id.button4);
        final TextView muestratiempo = (TextView) findViewById(R.id.textView);
        final TextView tiempoPresion = (TextView) findViewById(R.id.textView2);
        final TextView tiempoMin = (TextView) findViewById(R.id.textView3);
        final TextView tiempoMax = (TextView) findViewById(R.id.textView4);
        final TextView muestraMilis = (TextView) findViewById(R.id.textView5);
        final EditText editBuffer = (EditText) findViewById(R.id.editText2);
        final Button jugar1 = (Button) findViewById(R.id.button5);
        final Button conf = (Button) findViewById(R.id.button6);
        //para arrastrar el personaje en pantalla
        muestraX = (TextView) findViewById(R.id.textViewX);
        muestraY = (TextView) findViewById(R.id.textViewY);
        personaje = (ImageView) findViewById(R.id.imageView);
        mRrootLayout = (ViewGroup) findViewById(R.id.root);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(250,250);
        personaje.setLayoutParams(layoutParams);

        /*para usar con el implements, para manejar el evento desde afuera descomentar esta linea, el implements y el metodo onTouch al final. */
        //personaje.setOnTouchListener(this);


        buffer = Integer.parseInt(editBuffer.getText().toString());


        boton1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

                        mSoundPool.stop(0);
                        mStream2 = mSoundPool.play((Integer) mSoundPoolMap.get(SOUND_FX_02), streamVolume, streamVolume, 1, LOOP_1_TIME, 1f);

                        tiempoMin.setText("" + (ultimotiempo + velocidad - buffer));
                        tiempoPresion.setText("" + System.currentTimeMillis());
                        tiempoMax.setText("" + (ultimotiempo + velocidad + buffer));

                        if (System.currentTimeMillis() > ultimotiempo + velocidad - buffer && System.currentTimeMillis() < ultimotiempo + velocidad + buffer) {
                            //acierto
                            personaje.setImageResource(R.drawable.icono);
                        } else {
                            if (System.currentTimeMillis() > penultimoTiempo + velocidad - buffer && System.currentTimeMillis() < penultimoTiempo + velocidad + buffer) {
                                personaje.setImageResource(R.drawable.icono);
                            }else {
                                //fallo
                                personaje.setImageResource(R.drawable.iconotriste);
                            }
                        }


                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED

                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });

        boton2.setOnTouchListener(new View.OnTouchListener() {
                                      @Override
                                      public boolean onTouch(View v, MotionEvent event) {

                                          switch (event.getAction()) {
                                              case MotionEvent.ACTION_DOWN:
                                                  // PRESSED
                                                  float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                                                  streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

                                                  mSoundPool.stop(0);
                                                  mStream2 = mSoundPool.play((Integer) mSoundPoolMap.get(SOUND_FX_01), streamVolume, streamVolume, 1, LOOP_1_TIME, 1f);

                                                  tiempoMin.setText("" + (ultimotiempo + velocidad - buffer));
                                                  tiempoPresion.setText("" + System.currentTimeMillis());
                                                  tiempoMax.setText("" + (ultimotiempo + velocidad + buffer));
                                                  if (System.currentTimeMillis() > ultimotiempo + velocidad - buffer && System.currentTimeMillis() < ultimotiempo + velocidad + buffer) {
                                                      //acierto
                                                      personaje.setImageResource(R.drawable.icono2);
                                                  } else {
                                                      if (System.currentTimeMillis() > penultimoTiempo + velocidad - buffer && System.currentTimeMillis() < penultimoTiempo + velocidad + buffer) {
                                                          personaje.setImageResource(R.drawable.icono2);
                                                      } else {
                                                          //fallo
                                                          personaje.setImageResource(R.drawable.iconotriste);
                                                      }
                                                  }
                                                  return true; // if you want to handle the touch event
                                              case MotionEvent.ACTION_UP:
                                                  // RELEASED

                                                  return true; // if you want to handle the touch event
                                          }
                                          return false;
                                      }
                                  }

        );

            botonMetronomo.setOnClickListener(new View.OnClickListener()

                                              {
                                                  @Override
                                                  public void onClick(View v) {

                                                      if (!isTimerOn) {

                                                          EditText velo = (EditText) findViewById(R.id.editText);
                                                          //para cambiar la velocidad
                                                          //60,000 ms (1 minute) / Tempo (BPM) = Delay Time in ms for quarter-note beats
                                                          velocidad = 60000 / (Integer.parseInt(velo.getText().toString()));

                                                          muestraMilis.setText("milis: " + velocidad);


                                                          timer[0] = new Timer();
                                                          final TimerTask myTask = new TimerTask() {
                                                              @Override
                                                              public void run() {
                                                                  runOnUiThread(new Runnable() {
                                                                      @Override
                                                                      public void run() {
                                                                          //aqui cada segundo
                                                                          float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                                                                          streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

                                                                          mSoundPool.stop(0);
                                                                          mStream2 = mSoundPool.play((Integer) mSoundPoolMap.get(3), streamVolume, streamVolume, 1, LOOP_1_TIME, 1f);

                                                                          penultimoTiempo = ultimotiempo;
                                                                          ultimotiempo = System.currentTimeMillis();
                                                                          muestratiempo.setText("" + ultimotiempo);
                                                                      }
                                                                  });
                                                              }
                                                          };
                                                          timer[0].scheduleAtFixedRate(myTask, 1000, velocidad);
                                                          //timer.schedule(myTask, 1000, 1000);
                                                          isTimerOn = true;
                                                      } else {
                                                          timer[0].cancel();
                                                          isTimerOn = false;
                                                      }

                                                  }

                                              }

            );

            botonBuffer.setOnClickListener(new View.OnClickListener()

                                           {
                                               @Override
                                               public void onClick(View v) {
                                                   buffer = Integer.parseInt(editBuffer.getText().toString());
                                               }

                                           }

            );

        conf.setOnClickListener(new View.OnClickListener()

                                {
                                    @Override
                                    public void onClick(View v) {
                                        boton1.setVisibility(View.VISIBLE);
                                        boton2.setVisibility(View.VISIBLE);
                                        botonMetronomo.setVisibility(View.VISIBLE);
                                        botonBuffer.setVisibility(View.VISIBLE);
                                        muestratiempo.setVisibility(View.VISIBLE);
                                        tiempoPresion.setVisibility(View.VISIBLE);
                                        tiempoMin.setVisibility(View.VISIBLE);
                                        tiempoMax.setVisibility(View.VISIBLE);
                                        muestraMilis.setVisibility(View.VISIBLE);
                                        editBuffer.setVisibility(View.VISIBLE);
                                        final Button jugar1 = (Button) findViewById(R.id.button5);
                                        final Button conf = (Button) findViewById(R.id.button6);
                                    }

                                }

        );

        jugar1.setOnClickListener(new View.OnClickListener()

                                       {
                                           @Override
                                           public void onClick(View v) {
                                               jugar1.setVisibility(View.INVISIBLE);
                                               conf.setVisibility(View.INVISIBLE);
                                               boton1.setVisibility(View.VISIBLE);
                                               boton2.setVisibility(View.VISIBLE);
                                           }

                                       }

        );


        personaje.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        final int X = (int) event.getRawX();
                        final int Y = (int) event.getRawY();
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_DOWN:
                                RelativeLayout.LayoutParams lParams = (
                                        RelativeLayout.LayoutParams) view.getLayoutParams();
                                _xDelta = X - lParams.leftMargin;
                                _yDelta = Y - lParams.topMargin;
                                personaje.setImageResource(R.drawable.iconoo);
                                ticks = 0;
                                timer[1] = new Timer();
                                final TimerTask myTask2 = new TimerTask() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //aqui cada segundo

                                                ticks++;
                                                if(ticks % 2 == 0){
                                                    personaje.setImageResource(R.drawable.iconoo);
                                                }else {
                                                    personaje.setImageResource(R.drawable.iconoo2);
                                                }

                                            }
                                        });
                                    }
                                };
                                timer[1].schedule(myTask2, 100, 100);

                                break;
                            case MotionEvent.ACTION_MOVE:
                                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                                        .getLayoutParams();
                                layoutParams.leftMargin = X - _xDelta;
                                layoutParams.topMargin = Y - _yDelta;
                                layoutParams.rightMargin = -250;
                                layoutParams.bottomMargin = -250;
                                view.setLayoutParams(layoutParams);
                                muestraX.setText("X= " + X);
                                muestraY.setText("Y= " + Y);
                                break;
                            case MotionEvent.ACTION_UP:
                                timer[1].cancel();
                                personaje.setImageResource(R.drawable.iconoidle);

                                break;
                        }
                        mRrootLayout.invalidate();
                        return true;
                    }
                }
        );



    }
/*
    public boolean onTouch(View view, MotionEvent event){
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (
                        RelativeLayout.LayoutParams) view.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                personaje.setImageResource(R.drawable.iconoo);

                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;
                view.setLayoutParams(layoutParams);
                muestraX.setText("X= "+ X);
                muestraY.setText("Y= "+ Y);
                break;
            case MotionEvent.ACTION_UP:
                personaje.setImageResource(R.drawable.iconoidle);
                break;
        }
        mRrootLayout.invalidate();
        return true;
    }
    */

    }

