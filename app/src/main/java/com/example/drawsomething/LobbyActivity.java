package com.example.drawsomething;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.drawsomething.bean.GuessingGameTable;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.MessageFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LobbyActivity extends AppCompatActivity {
    private final static String TAG=MainActivity.class.getSimpleName();
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        this.userId = getIntent().getIntExtra("user_id",0);
        setupHall();
    }

    private void setupHall(){
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = "http://10.62.16.240:8080/DrawSomethingAPI/Table";
        final Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(),"Error in get table",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(@NotNull final Call call, @NotNull Response response) throws IOException {
                String context = response.body().string();
                Log.i(TAG, "onResponse: "+context);
                Gson gson = new Gson();
                final GuessingGameTable[] tables = gson.fromJson(context, GuessingGameTable[].class);

                runOnUiThread(new Runnable() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void run() {
                        LinearLayout linearLayout = findViewById(R.id.hall);
                        linearLayout.removeAllViews();
                        for (int i = 0; i < tables.length; i++) {
                            final GuessingGameTable guessingGameTable = tables[i];
                            LinearLayout oneLine = new LinearLayout(getApplicationContext());
                            oneLine.setOrientation(LinearLayoutCompat.HORIZONTAL);
                            linearLayout.addView(oneLine);
                            View view = getLayoutInflater().inflate(R.layout.table, null);

                            Button user_1 = (Button) view.findViewById(R.id.user_1);
                            Button user_2 = (Button) view.findViewById(R.id.user_2);
                            Button user_3 = (Button) view.findViewById(R.id.user_3);
                            Button user_4 = (Button) view.findViewById(R.id.user_4);
                            if (guessingGameTable.getUser_1() > 0) {
                                user_1.setBackground(ActivityCompat.getDrawable(getApplicationContext(), R.mipmap.people));
                                user_1.setEnabled(false);
                            } else {
                                user_1.setBackground(ActivityCompat.getDrawable(getApplicationContext(), R.mipmap.down));
                                user_1.setEnabled(true);
                                user_1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        joinTable(1, guessingGameTable);
                                    }
                                });
                            }
                            if (guessingGameTable.getUser_2() > 0) {
                                user_2.setBackground(ActivityCompat.getDrawable(getApplicationContext(), R.mipmap.people));
                                user_1.setEnabled(false);
                            } else {
                                user_2.setBackground(ActivityCompat.getDrawable(getApplicationContext(), R.mipmap.down));
                                user_1.setEnabled(true);
                                user_2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        joinTable(2, guessingGameTable);
                                    }
                                });
                            }
                            if (guessingGameTable.getUser_3() > 0) {
                                user_3.setBackground(ActivityCompat.getDrawable(getApplicationContext(), R.mipmap.people));
                                user_1.setEnabled(false);
                            } else {
                                user_3.setBackground(ActivityCompat.getDrawable(getApplicationContext(), R.mipmap.down));
                                user_1.setEnabled(true);
                                user_3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        joinTable(3, guessingGameTable);
                                    }
                                });
                            }
                            if (guessingGameTable.getUser_4() > 0) {
                                user_4.setBackground(ActivityCompat.getDrawable(getApplicationContext(), R.mipmap.people));
                                user_1.setEnabled(false);
                            } else {
                                user_4.setBackground(ActivityCompat.getDrawable(getApplicationContext(), R.mipmap.down));
                                user_1.setEnabled(true);
                                user_4.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        joinTable(4, guessingGameTable);
                                    }
                                });
                            }
                            oneLine.addView(view);
                        }
                    }
                });

            }
        });
    }

    private void joinTable(int place,GuessingGameTable guessingGameTable){
        final Intent intent = new Intent(getApplicationContext(),GameActivity.class);
        intent.putExtra("tableId",guessingGameTable.getId());
        intent.putExtra("place",place);
        String url =  MessageFormat.format("http://10.62.16.240:8080/DrawSomethingAPI/JoinTable?id_table={0}&user={1}&id={2}",guessingGameTable.getId(),place,userId);
        OkHttpClient okHttpClient=new OkHttpClient();
        final Request request=new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String content=response.body().string();
                Log.i(TAG, "onResponse: "+content);
                startActivity(intent);
            }
        });
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable(){
        public void run(){
            setupHall();
            handler.postDelayed(this,1000);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}
