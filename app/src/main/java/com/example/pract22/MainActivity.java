package com.example.pract22;

import static com.example.pract22.R.id.btn_Restart;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.AdapterView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private GridAdapter mAdapter;

    final Context context = this;
    private Button Restart;
    private TextView mStepScreen;
    private Chronometer mTimeScreen;
    private Integer StepCount = 0; // кол-во ходов
    String TextToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView mGrid = (GridView) findViewById(R.id.field);
        mGrid.setNumColumns(6);
        mGrid.setEnabled(true);

        Restart = findViewById(R.id.btn_Restart);
        mTimeScreen = (Chronometer) findViewById(R.id.timeview);
        mStepScreen = (TextView)findViewById(R.id.stepview);


        mTimeScreen.start();

        // шрифт
     //   Typeface type = Typeface.createFromAsset(getAssets(),"my-font.ttf");
        mAdapter = new GridAdapter(this, 6, 6);
        mGrid.setAdapter(mAdapter);
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,int position, long id) {

                mAdapter.checkOpenCells ();
                mAdapter.openCell (position);

                StepCount ++;
                mStepScreen.setText (StepCount.toString());
                if (mAdapter.checkGameOver())
                {
                    mTimeScreen.stop();
                    String time = mTimeScreen.getText().toString();
                    String TextToast = "Игра закончена nХодов: " + StepCount.toString() + "nВремя: " + time;
                    Toast.makeText (getApplicationContext(), TextToast, Toast.LENGTH_SHORT).show();
                    showDialog();
                }
            }
        });
        Restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
            }
        });
    }
    @SuppressLint("SetTextI18n")
    private void restartGame() {
        GridView mGrid = (GridView) findViewById(R.id.field);
        mGrid.setNumColumns(6);
        mGrid.setEnabled(true);

       StepCount = 0;
        mStepScreen.setText(StepCount.toString());


        mTimeScreen.setBase(SystemClock.elapsedRealtime());
        mTimeScreen.start();


        mAdapter = new GridAdapter(this, 6, 6);
        mGrid.setAdapter(mAdapter);
    }
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Вы победили! Желаете начать с начала?");
        builder.setMessage(TextToast);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { restartGame(); }
                })
                .setNegativeButton("Нет, желаю выйти", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
    }

}