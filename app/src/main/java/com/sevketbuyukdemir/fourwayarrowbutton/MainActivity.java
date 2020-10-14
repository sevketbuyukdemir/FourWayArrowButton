package com.sevketbuyukdemir.fourwayarrowbutton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    FourWayArrow fourWayArrow;
    ImageView imageView;
    int image_x = 0;
    int image_y = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        fourWayArrow = findViewById(R.id.four_way_arrow);
        fourWayArrow.setOnFourWayArrowTouchListener(new FourWayArrow.OnFourWayArrowTouchListener() {
            @Override
            public void onStartTrackingTouch(FourWayArrow fourWayArrow, int WHICH_BUTTON) {
                move(WHICH_BUTTON);
            }

            @Override
            public void onMoveTrackingTouch(FourWayArrow fourWayArrow, int WHICH_BUTTON) {
                move(WHICH_BUTTON);
            }

            @Override
            public void onStopTrackingTouch(FourWayArrow fourWayArrow, int WHICH_BUTTON) {
                move(WHICH_BUTTON);
            }
        });
    }

    public void move(int BUTTON_PRESSED){
        if (BUTTON_PRESSED == FourWayArrow.UP_BUTTON_PRESSED) {
            if (image_y == 0) {
                System.out.println("Already top!!");
            } else {
                image_y--;
            }
        } else if (BUTTON_PRESSED == FourWayArrow.RIGHT_BUTTON_PRESSED) {
            if(image_x == 2000){
                System.out.println("Already right");
            } else {
                image_x++;
            }
        } else if (BUTTON_PRESSED == FourWayArrow.BOTTOM_BUTTON_PRESSED) {
            if (image_y == 2000) {
                System.out.println("Already top!!");
            } else {
                image_y++;
            }
        } else if (BUTTON_PRESSED == FourWayArrow.LEFT_BUTTON_PRESSED) {
            if(image_x == 0){
                System.out.println("Already right");
            } else {
                image_x--;
            }
        }
        imageView.setX(image_x);
        imageView.setY(image_y);
    }
}