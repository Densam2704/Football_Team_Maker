package com.example.lab4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    
    private ArrayList<Footballer>footballers=new ArrayList<>();
    private RecyclerView recyclerView;
    private DataAdapter adapter;
    
    private SharedPreferences sharedPreferences;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.lab4", Context.MODE_PRIVATE);
        getSupportActionBar().setTitle("Список футболистов");
        initRecyclerView();
        readFromSharedPreferences();

    }

    @Override
    protected void onStop() {
        super.onStop();
        clearSharedPreferences();
        writeToSharedPreferences();
    }

    private void readFromSharedPreferences() {
        int i =0;
        while (sharedPreferences.contains("footballer"+i)){
            adapter.add(new Footballer(sharedPreferences.getString("footballer"+i,"empty")));
            i++;
        }
    }

    private void clearSharedPreferences(){
        int i = 0;
        while (sharedPreferences.contains("footballer"+i)){
            sharedPreferences.edit().remove("footballer"+i).apply();
            i++;
        }
    }

    private void writeToSharedPreferences(){
        footballers=adapter.getFootballers();

        for (int i=0;i<footballers.size();i++){
            sharedPreferences.edit().putString("footballer"+i,footballers.get(i).getName()).apply();
        }
    }


    private void initRecyclerView(){
        Log.i(TAG, "initRecyclerView: init");
        recyclerView= findViewById(R.id.recyclerViewFootballers);
        adapter = new DataAdapter(this,footballers,Constants.VIEW_TYPE_1);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, RecyclerView.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_drawable));
        recyclerView.addItemDecoration(dividerItemDecoration);

    }

    public void onBtnAdd(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Введите имя");
        //Объект для получение шаблона окна
        LayoutInflater inflater = LayoutInflater.from(this);
        //Получаем шаблон окна register_window в переменную registerWindow
        View dialogWindow =  inflater.inflate(R.layout.dialog_window,null);
        //Устанавливаем шаблон для всплывающего окна
        dialog.setView(dialogWindow);
        final EditText editText = dialogWindow.findViewById(R.id.etTextField);

        dialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (TextUtils.isEmpty(editText.getText().toString())){
                    Toast.makeText(MainActivity.this,
                            "Пустое поле ввода",Toast.LENGTH_SHORT).show();
                    return;
                }
                adapter.add(new Footballer(editText.getText().toString(),true));

            }
        });
        dialog.show();

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        super.onContextItemSelected(item);
        final MenuItem copyItem = item;
        switch (item.getItemId())
        {

            //Выбор удаления
            case R.id.action_delete:
                adapter.removeAt(item.getGroupId());
                Toast.makeText(this,"Игрок удален",Toast.LENGTH_SHORT).show();
                return true;

            //Выбор изменения
            case R.id.action_change:

                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Введите имя");
                //Объект для получение шаблона окна
                LayoutInflater inflater = LayoutInflater.from(this);
                //Получаем шаблон окна register_window в переменную registerWindow
                View dialogWindow =  inflater.inflate(R.layout.dialog_window,null);
                //Устанавливаем шаблон для всплывающего окна
                dialog.setView(dialogWindow);
                final EditText editText = dialogWindow.findViewById(R.id.etTextField);

                editText.setSelectAllOnFocus(true);
                editText.setText(footballers.get(copyItem.getGroupId()).getName());

                dialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (TextUtils.isEmpty(editText.getText().toString())){
                            Toast.makeText(MainActivity.this,
                                    "Пустое поле ввода",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        adapter.updateAt(copyItem.getGroupId(),
                                new Footballer(editText.getText().toString(),
                                        footballers.get(copyItem.getGroupId()).check()));
                        Toast.makeText(MainActivity.this,
                                "Игрок изменен",Toast.LENGTH_SHORT).show();

                    }
                });
                dialog.show();

                return true;
            default:
                return false;
        }

    }

    public void onBtnMakeTeams(View view) {
       // footballers=adapter.getFootballers();
        Intent intent = new Intent(MainActivity.this,TeamsActivity.class);
        int countPresent=0;
        //Перебираем футболистов и отправляем только тех, кто присутствует
        for (int i=0;i<footballers.size();i++){
            Footballer footballer = footballers.get(i);
            if(footballer.check()){
                intent.putExtra ("footballer"+countPresent,footballer.getName());
                countPresent++;
            }
        }
        Log.i(TAG, "onBtnMakeTeams: footballers size = "+footballers.size());
        Log.i(TAG, "onBtnMakeTeams: countPresent "+String.valueOf(countPresent));
        intent.putExtra("countPresent",countPresent);
        startActivity(intent);
    }
}