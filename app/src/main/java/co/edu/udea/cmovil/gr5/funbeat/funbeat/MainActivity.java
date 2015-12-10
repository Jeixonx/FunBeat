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
import java.util.Random;
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

    //variables gui

    Button boton1;
    Button boton2;
    Button botonMetronomo;
    Button botonBuffer;
    TextView muestratiempo;
    TextView tiempoPresion;
    TextView tiempoMin;
    TextView tiempoMax;
    TextView muestraMilis;
    EditText velo;
    EditText editBuffer;
    Button jugar1;
    Button conf;
    ImageView blue;
    ImageView green;
    TextView combo;
    TextView vidas;
    TextView puntos;
    TextView nivel;

    int ticks;
    boolean esperando;
    boolean esperando2;
    boolean esperandoblue;
    boolean esperandoblue2;
    boolean acierto;
    boolean sgtnivel = false;
    int comboint = 1;
    int vidasint = 3;
    int puntosint = 0;
    int nivelint = 1;
    int tickslvl = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Timer[] timer = new Timer[2];


        //inicializacion del soundpool
        final SoundPool mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        final AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        final HashMap mSoundPoolMap = new HashMap();

        mSoundPoolMap.put(1, mSoundPool.load(this, R.raw.bluefoot, 1));
        mSoundPoolMap.put(2, mSoundPool.load(this, R.raw.snare, 1));
        mSoundPoolMap.put(3, mSoundPool.load(this, R.raw.hihat, 1));
        mSoundPoolMap.put(4, mSoundPool.load(this, R.raw.go, 1));
        mSoundPoolMap.put(5, mSoundPool.load(this, R.raw.meeb, 1));
        mSoundPoolMap.put(6, mSoundPool.load(this, R.raw.boo, 1));
        mSoundPoolMap.put(7, mSoundPool.load(this, R.raw.gameover, 1));
        mSoundPoolMap.put(8, mSoundPool.load(this, R.raw.hurry, 1));


        //definicion logica de la gui
        boton1 = (Button) findViewById(R.id.button);
        boton2 = (Button) findViewById(R.id.button2);
        botonMetronomo = (Button) findViewById(R.id.button3);
        botonBuffer = (Button) findViewById(R.id.button4);
        muestratiempo = (TextView) findViewById(R.id.textView);
        tiempoPresion = (TextView) findViewById(R.id.textView2);
        tiempoMin = (TextView) findViewById(R.id.textView3);
        tiempoMax = (TextView) findViewById(R.id.textView4);
        muestraMilis = (TextView) findViewById(R.id.textView5);
        velo = (EditText) findViewById(R.id.editText);
        final EditText editBuffer = (EditText) findViewById(R.id.editText2);
        final Button jugar1 = (Button) findViewById(R.id.button5);
        conf = (Button) findViewById(R.id.button6);
        blue = (ImageView) findViewById(R.id.imgblue);
        green = (ImageView) findViewById(R.id.imggreen);
        combo = (TextView) findViewById(R.id.combotxt);
        vidas = (TextView) findViewById(R.id.vidastxt);
        puntos = (TextView) findViewById(R.id.puntostxt);
        nivel = (TextView) findViewById(R.id.niveltxt);

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
                                        velo.setVisibility(View.VISIBLE);
                                        personaje.setVisibility(View.VISIBLE);
                                        //jugar1.setVisibility(View.INVISIBLE);
                                        conf.setVisibility(View.INVISIBLE);
                                    }

                                }

        );

        jugar1.setOnClickListener(new View.OnClickListener(){

             @Override
             public void onClick(View v) {

                 jugar1.setVisibility(View.INVISIBLE);
                 conf.setVisibility(View.INVISIBLE);
                 //boton1.setVisibility(View.VISIBLE);
                 //boton2.setVisibility(View.VISIBLE);
                 personaje.setVisibility(View.VISIBLE);
                 jugar1.setVisibility(View.INVISIBLE);
                 conf.setVisibility(View.INVISIBLE);
                 blue.setVisibility(View.VISIBLE);
                 green.setVisibility(View.VISIBLE);

                 boton1.setVisibility(View.INVISIBLE);
                 boton2.setVisibility(View.INVISIBLE);
                 botonMetronomo.setVisibility(View.INVISIBLE);
                 botonBuffer.setVisibility(View.INVISIBLE);
                 muestratiempo.setVisibility(View.INVISIBLE);
                 tiempoPresion.setVisibility(View.INVISIBLE);
                 tiempoMin.setVisibility(View.INVISIBLE);
                 tiempoMax.setVisibility(View.INVISIBLE);
                 muestraMilis.setVisibility(View.INVISIBLE);
                 editBuffer.setVisibility(View.INVISIBLE);
                 velo.setVisibility(View.INVISIBLE);

                 blue.setVisibility(View.VISIBLE);
                 green.setVisibility(View.VISIBLE);
                 combo.setVisibility(View.VISIBLE);
                 vidas.setVisibility(View.VISIBLE);
                 puntos.setVisibility(View.VISIBLE);

                 blue.setImageResource(R.drawable.bblue);
                 green.setImageResource(R.drawable.bgreen);


                 final Integer[] ticksInactivo = {0};
                 final Random r = new Random();
                 final int[] r1 = new int[2];
                 r1[0] = r.nextInt(5 - 2 + 1) + 2;
                 //int i1 = r.nextInt(max - min + 1) + min;

                 if (!isTimerOn || sgtnivel) {


                     //para cambiar la velocidad
                     //60,000 ms (1 minute) / Tempo (BPM) = Delay Time in ms for quarter-note beats


                     velocidad = 60000 / (Integer.parseInt(velo.getText().toString()) + (nivelint * 10));
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

                                     tickslvl++;
                                     if(tickslvl>=50){
                                         nivelint++;
                                         sgtnivel = true;
                                         mSoundPool.stop(0);
                                         mStream2 = mSoundPool.play((Integer) mSoundPoolMap.get(8), streamVolume, streamVolume, 1, LOOP_1_TIME, 1f);
                                         nivel.setText("nivel "+nivelint);
                                         timer[0].cancel();
                                         jugar1.setVisibility(View.VISIBLE);
                                         tickslvl = 0;


                                     }

                                     if (vidasint <= 0){ //GAME OVER
                                         timer[0].cancel();
                                         mSoundPool.stop(0);
                                         mStream2 = mSoundPool.play((Integer) mSoundPoolMap.get(7), streamVolume, streamVolume, 1, LOOP_1_TIME, 1f);
                                         ponerInvisibles();
                                         jugar1.setVisibility(View.VISIBLE);
                                         conf.setVisibility(View.VISIBLE);
                                         isTimerOn = false;
                                         vidasint = 3;
                                         comboint = 1;
                                         puntosint = 0;

                                     }
                                     ticksInactivo[0]++;
                                     if(r1[0] == ticksInactivo[0]){



                                         mSoundPool.stop(0);
                                         mStream2 = mSoundPool.play((Integer) mSoundPoolMap.get(3), streamVolume, streamVolume, 1, LOOP_1_TIME, 1f);
                                         ticksInactivo[0] = 0;
                                         r1[0] = r.nextInt(5 - 2 + 1) + 2;
                                         r1[1] = r.nextInt(2 - 1 + 1) + 1;

                                         if(r1[1]==1) {
                                             personaje.setImageResource(R.drawable.iconogo);
                                             mSoundPool.stop(0);
                                             mStream2 = mSoundPool.play((Integer) mSoundPoolMap.get(4), streamVolume, streamVolume, 1, LOOP_1_TIME, 1f);
                                             esperando = true;
                                             esperando2 = true;
                                             green.setImageResource(R.drawable.bblack);

                                         }else{
                                             personaje.setImageResource(R.drawable.iconomeep);
                                             mSoundPool.stop(0);
                                             mStream2 = mSoundPool.play((Integer) mSoundPoolMap.get(5), streamVolume, streamVolume, 1, LOOP_1_TIME, 1f);
                                             esperandoblue = true;
                                             esperandoblue2 = true;
                                             blue.setImageResource(R.drawable.bblack);
                                         }



                                     }else {

                                         mSoundPool.stop(0);
                                         mStream2 = mSoundPool.play((Integer) mSoundPoolMap.get(3), streamVolume, streamVolume, 1, LOOP_1_TIME, 1f);

                                         //personaje.setImageResource(R.drawable.iconomedio);
                                         if(!esperando2 && !esperandoblue2 && (esperando || esperandoblue ) && !acierto){
                                             vidasint--;
                                             vidas.setText(""+vidasint);
                                         }
                                         if (!esperando2) {
                                             esperando = false;
                                         }
                                         esperando2 = false;

                                         if(!esperandoblue2){
                                             esperandoblue = false;
                                         }
                                         esperandoblue2 = false;

                                         green.setImageResource(R.drawable.bgreen);
                                         blue.setImageResource(R.drawable.bblue);

                                     }



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

        blue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED




                        blue.setImageResource(R.drawable.byellow);
                        float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

                        mSoundPool.stop(0);
                        mStream2 = mSoundPool.play((Integer) mSoundPoolMap.get(SOUND_FX_02), streamVolume, streamVolume, 1, LOOP_1_TIME, 1f);

                        tiempoMin.setText("" + (ultimotiempo + velocidad - buffer));
                        tiempoPresion.setText("" + System.currentTimeMillis());
                        tiempoMax.setText("" + (ultimotiempo + velocidad + buffer));

                        if (System.currentTimeMillis() > ultimotiempo + velocidad - buffer && System.currentTimeMillis() < ultimotiempo + velocidad + buffer && esperandoblue) {
                            //acierto
                            personaje.setImageResource(R.drawable.icono);
                            comboint++;
                            acierto = true;
                            puntosint = puntosint + (comboint*10);
                        } else {
                            if (System.currentTimeMillis() > penultimoTiempo + velocidad - buffer && System.currentTimeMillis() < penultimoTiempo + velocidad + buffer && esperandoblue) {
                                personaje.setImageResource(R.drawable.icono);
                                comboint++;
                                acierto = true;
                                puntosint = puntosint + (comboint*10);
                            } else {
                                //fallo
                                personaje.setImageResource(R.drawable.iconotriste);

                                mSoundPool.stop(0);
                                mStream2 = mSoundPool.play((Integer) mSoundPoolMap.get(6), streamVolume, streamVolume, 1, LOOP_1_TIME, 1f);
                                comboint = 0;
                                //vidasint--;
                                acierto = false;

                            }
                        }
                        combo.setText("x" + comboint);
                        vidas.setText("" + vidasint);
                        puntos.setText("" + puntosint);

                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        blue.setImageResource(R.drawable.bblue);
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });


        green.setOnTouchListener(new View.OnTouchListener() {
                                     @Override
                                     public boolean onTouch(View v, MotionEvent event) {

                                         switch (event.getAction()) {
                                             case MotionEvent.ACTION_DOWN:
                                                 // PRESSED
                                                 green.setImageResource(R.drawable.byellow);

                                                 float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                                                 streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

                                                 mSoundPool.stop(0);
                                                 mStream2 = mSoundPool.play((Integer) mSoundPoolMap.get(SOUND_FX_01), streamVolume, streamVolume, 1, LOOP_1_TIME, 1f);

                                                 tiempoMin.setText("" + (ultimotiempo + velocidad - buffer));
                                                 tiempoPresion.setText("" + System.currentTimeMillis());
                                                 tiempoMax.setText("" + (ultimotiempo + velocidad + buffer));
                                                 if (System.currentTimeMillis() > ultimotiempo + velocidad - buffer && System.currentTimeMillis() < ultimotiempo + velocidad + buffer && esperando) {
                                                     //acierto
                                                     personaje.setImageResource(R.drawable.icono2);
                                                     comboint++;
                                                     acierto = true;
                                                     puntosint = puntosint + (comboint*10);
                                                 } else {
                                                     if (System.currentTimeMillis() > penultimoTiempo + velocidad - buffer && System.currentTimeMillis() < penultimoTiempo + velocidad + buffer && esperando) {
                                                         personaje.setImageResource(R.drawable.icono2);
                                                         comboint++;
                                                         acierto = true;
                                                         puntosint = puntosint + (comboint*10);
                                                     } else {
                                                         //fallo
                                                         personaje.setImageResource(R.drawable.iconotriste);

                                                         mSoundPool.stop(0);
                                                         mStream2 = mSoundPool.play((Integer) mSoundPoolMap.get(6), streamVolume, streamVolume, 1, LOOP_1_TIME, 1f);
                                                         comboint = 0;
                                                        // vidasint--;
                                                         acierto = false;
                                                     }
                                                 }
                                                 combo.setText("x" + comboint);
                                                 vidas.setText("" + vidasint);
                                                 puntos.setText("" + puntosint);

                                                 return true; // if you want to handle the touch event
                                             case MotionEvent.ACTION_UP:
                                                 // RELEASED
                                                 green.setImageResource(R.drawable.bgreen);
                                                 return true; // if you want to handle the touch event
                                         }
                                         return false;
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
                                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
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
                                                if (ticks % 2 == 0) {
                                                    personaje.setImageResource(R.drawable.iconoo);
                                                } else {
                                                    personaje.setImageResource(R.drawable.iconoo2);
                                                }

                                            }
                                        });
                                    }
                                };
                                timer[1].schedule(myTask2, 100, 100);


                                break;

                            case MotionEvent.ACTION_MOVE:
                                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();

                                if (X < 120) {
                                    //_xDelta = 120 - layoutParams.leftMargin;
                                    //timer[1].cancel();
                                    personaje.setImageResource(R.drawable.paredizq);

                                } else {
                                    if (Y < 200) {
                                        // _yDelta = Y - layoutParams.topMargin;
                                        //timer[1].cancel();
                                        personaje.setImageResource(R.drawable.paredarr);

                                    }
                                }

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

    private void ponerInvisibles(){
        boton1.setVisibility(View.INVISIBLE);
        boton2.setVisibility(View.INVISIBLE);
        botonMetronomo.setVisibility(View.INVISIBLE);
        botonBuffer.setVisibility(View.INVISIBLE);
        muestratiempo.setVisibility(View.INVISIBLE);
        tiempoPresion.setVisibility(View.INVISIBLE);
        tiempoMin.setVisibility(View.INVISIBLE);
        tiempoMax.setVisibility(View.INVISIBLE);
        muestraMilis.setVisibility(View.INVISIBLE);
        velo.setVisibility(View.INVISIBLE);
//        editBuffer.setVisibility(View.INVISIBLE);


        blue.setVisibility(View.INVISIBLE);
        green.setVisibility(View.INVISIBLE);
        combo.setVisibility(View.INVISIBLE);
        vidas.setVisibility(View.INVISIBLE);
        puntos.setVisibility(View.INVISIBLE);


    }

/*
    public boolean onTouch(View view, MotionEvent event){
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout1.LayoutParams lParams = (
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

