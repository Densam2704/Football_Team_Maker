package com.example.lab4;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Comparator;

public class Footballer implements Parcelable, Serializable {
    String name;
    String team = Constants.NO_TEAM;
    boolean isChecked =false;


    public Footballer(String name, boolean isChecked) {
        this.name = name;
        this.isChecked = isChecked;
    }

    public Footballer() {
    }

    public Footballer(String name) {
        this.name = name;
    }


    protected Footballer(Parcel in) {
        name = in.readString();

    }

    public static final Creator<Footballer> CREATOR = new Creator<Footballer>() {
        @Override
        public Footballer createFromParcel(Parcel in) {
            return new Footballer(in);
        }

        @Override
        public Footballer[] newArray(int size) {
            return new Footballer[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public boolean check() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        //parcel.writeBoolean(isChecked);


    }
    public static final Comparator<Footballer> COMPARE_BY_TEAM = new Comparator<Footballer>() {
        @Override
        public int compare(Footballer f1, Footballer f2) {

            //Сравниваем и по названию команды и по имени игрока
            //Значение сравнения по команде имеет больший вес
            return f1.getTeam().compareTo(f2.getTeam())*100 + f1.getName().compareTo(f2.getName());
        }
    };
}
