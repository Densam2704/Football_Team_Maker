package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class TeamsActivity extends AppCompatActivity {

    private static final String TAG = "TeamsActivity";
    private ArrayList<Footballer> footballers = new ArrayList<>();
    private RecyclerView recyclerView;
    private DataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);
        getSupportActionBar().setTitle("Распределение по командам");
        Intent intent = getIntent();
        int count = intent.getIntExtra("countPresent", -1);
        Log.i(TAG, "onCreate: count = " + count);
        if (count < 0) {
            Toast.makeText(TeamsActivity.this, "Ошибка передачи данных",
                    Toast.LENGTH_SHORT).show();
        }
        if (count == 0) {
            Toast.makeText(TeamsActivity.this, "Не был отмечен ни один игрок",
                    Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < count; i++) {
            Footballer footballer = new Footballer(intent.getStringExtra("footballer" + i));
            footballers.add(footballer);
        }
        initRecyclerView();
        distributeFootballers();


    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewFootballersWithTeams);
        adapter = new DataAdapter(TeamsActivity.this, footballers, Constants.VIEW_TYPE_2);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(TeamsActivity.this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration
                (this, RecyclerView.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_drawable));
        recyclerView.addItemDecoration(dividerItemDecoration);

    }

    public void distributeFootballers() {

        //Число игроков
        int fullSize = adapter.getItemCount();
        //Первая половина игроков
        int half = fullSize / 2;
        //Нечетность числа игроков
        int odd = fullSize % 2;
        //Счетчик игроков
        int count = 0;

        //В первую команду
        while (count < half) {
            int rNum = (int) (Math.random() * fullSize);
            //Если еще не распределили
            if (!footballers.get(rNum).check()) {
                footballers.get(rNum).setTeam(Constants.TEAM1);
                footballers.get(rNum).setChecked(true);
                count++;
            }
        }
        //Во вторую команду
        while (count < fullSize) {
            int rNum = (int) (Math.random() * fullSize);
            //Если еще не распределили
            if (!footballers.get(rNum).check()) {
                //Если нечетное число игроков и это последний игрок распределения
                //Игрок будет в запасе
                if (odd == 1 && count == fullSize - 1) {
                    footballers.get(rNum).setTeam(Constants.RESERVE);
                } else {
                    footballers.get(rNum).setTeam(Constants.TEAM2);
                }
                footballers.get(rNum).setChecked(true);
                count++;
            }

        }
        Collections.sort(footballers, Footballer.COMPARE_BY_TEAM);
        adapter.notifyDataSetChanged();
    }
}