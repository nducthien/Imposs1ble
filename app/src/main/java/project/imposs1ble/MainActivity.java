package project.imposs1ble;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends Activity implements Runnable {

    private static final long TIME_TRANTE = 1500;
    private Button btnLeft, btnRight, imgStart;
    private ImageView imgSquare, imgBall;
    private TextView tvScore;
    private int degree;
    private int mSquareColor;
    private int mBallColor;
    private Thread thread;
    private Handler handler;
    private int score;
    private boolean isPlay;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        isPlay = false;
        handler = new Handler();
        thread = new Thread(this);
        thread.start();
    }

    private void playAudio(int resource) {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }
        mediaPlayer = MediaPlayer.create(MainActivity.this, resource);
        mediaPlayer.start();
    }

    private void initGame() {
        degree = 0;
        mSquareColor = 0;
        mBallColor = 0;
        score = 0;
        isPlay = true;
        tvScore.setText("00");
        imgStart.setVisibility(View.INVISIBLE);
        imgSquare.clearAnimation();
    }

    private void startBallAnimation() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                mBallColor = new Random().nextInt(4);
                imgBall.setImageResource(R.drawable.ball_0 + mBallColor);
                TranslateAnimation translateAnimation =
                        new TranslateAnimation(
                                Animation.RELATIVE_TO_PARENT, 0.0f,
                                Animation.RELATIVE_TO_PARENT, 0.0f,
                                Animation.RELATIVE_TO_PARENT, 0.0f,
                                Animation.RELATIVE_TO_PARENT, 0.9f);
                translateAnimation.setDuration(TIME_TRANTE);
                imgBall.startAnimation(translateAnimation);
            }
        });
    }

    private void initView() {
        btnLeft = (Button) findViewById(R.id.btn_left);
        btnRight = (Button) findViewById(R.id.btn_right);
        imgSquare = (ImageView) findViewById(R.id.img_square);
        tvScore = (TextView) findViewById(R.id.tv_score);
        imgBall = (ImageView) findViewById(R.id.img_ball);
        imgStart = (Button) findViewById(R.id.img_start);
        imgStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initGame();
            }
        });
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "CLick Left"
                        , Toast.LENGTH_SHORT).show();
                clickLeft();
            }
        });
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "CLick Right"
                        , Toast.LENGTH_SHORT).show();
                clickRight();
            }
        });
    }

    private void clickRight() {
        RotateAnimation rotateAnimation =
                new RotateAnimation(degree, degree + 90
                        , Animation.RELATIVE_TO_SELF, 0.5f
                        , Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(250);
        rotateAnimation.setFillAfter(true);
        imgSquare.startAnimation(rotateAnimation);
        degree = degree + 90;
        mSquareColor--;
        if (mSquareColor < 0) {
            mSquareColor = 3;
        }
        switch (mSquareColor) {
            case 0:
                tvScore.setText("RED");
                break;
            case 1:
                tvScore.setText("YELLOW");
                break;
            case 2:
                tvScore.setText("GREEN");
                break;
            case 3:
                tvScore.setText("BLUE");
                break;
        }
    }

    private void clickLeft() {
        RotateAnimation rotateAnimation =
                new RotateAnimation(degree, degree - 90
                        , Animation.RELATIVE_TO_SELF, 0.5f
                        , Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(250);
        rotateAnimation.setFillAfter(true);
        imgSquare.startAnimation(rotateAnimation);
        degree = degree - 90;
        mSquareColor++;
        if (mSquareColor > 3) {
            mSquareColor = 0;
        }
        switch (mSquareColor) {
            case 0:
                tvScore.setText("RED");
                break;
            case 1:
                tvScore.setText("YELLOW");
                break;
            case 2:
                tvScore.setText("GREEN");
                break;
            case 3:
                tvScore.setText("BLUE");
                break;
        }
    }

    @Override
    public void run() {
        while (true) {
            if (isPlay == true) {
                startBallAnimation();
                try {
                    Thread.sleep(TIME_TRANTE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mBallColor == mSquareColor) {
                    score++;
                    playAudio(R.raw.point);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tvScore.setText("" + score);
                        }
                    });
                } else {
                    playAudio(R.raw.gameover);
                    isPlay = false;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tvScore.setText("Game over");
                            imgStart.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        }

    }
}
