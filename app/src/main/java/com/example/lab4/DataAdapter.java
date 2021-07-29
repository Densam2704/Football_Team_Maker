package com.example.lab4;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Comparator;

public class DataAdapter extends RecyclerView.Adapter {

    public static final String TAG = "DataAdapter";
    private ArrayList<Footballer> footballers = new ArrayList<>();
    private int viewType;
    private Context mContext;

    public DataAdapter(Context mContext, ArrayList<Footballer> footballers, int viewType) {
        this.footballers = footballers;
        this.mContext = mContext;
        this.viewType = viewType;
    }

    @Override
    public int getItemViewType(int position) {
        return this.viewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (this.viewType == Constants.VIEW_TYPE_1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footballer_item, parent, false);
            ViewHolder1 holder1 = new ViewHolder1(view);
            return holder1;
        } else if (this.viewType == Constants.VIEW_TYPE_2) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_footballer_in_team, parent, false);
            ViewHolder2 holder2 = new ViewHolder2(view);
            return holder2;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final int position_copy = position;
        Log.i(TAG, "onBindViewHolder: called");

        if (this.viewType == Constants.VIEW_TYPE_1) {

            ViewHolder1 holder1 = (ViewHolder1) holder;
            holder1.tvName.setText(footballers.get(position).getName());
            holder1.cbIsPresent.setChecked(footballers.get(position).check());
            holder1.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(TAG, "onClick" + footballers.get(position_copy).getName());
                    Toast.makeText(mContext, footballers.get(position_copy).getName(), Toast.LENGTH_SHORT).show();
                }
            });
            holder1.cbIsPresent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    try {
                        footballers.get(position_copy).setChecked(b);
                    } catch (Exception e) {
                        Log.i(TAG, "onCheckedChanged: Exception caught. Doesn't exist anymore");
                        return;
                    }

                }
            });
        } else if (this.viewType == Constants.VIEW_TYPE_2) {

            ViewHolder2 holder2 = (ViewHolder2) holder;
            holder2.tvTeam.setText(footballers.get(position).getTeam());
            holder2.tvName.setText(footballers.get(position).getName());
            if (footballers.get(position).getTeam().equals(Constants.TEAM1)) {
                holder2.parentLayout.setBackgroundResource(R.color.colorBackgroundTeam1);
                holder2.tvName.setBackgroundResource(R.color.colorBackgroundTeam1);
                holder2.tvTeam.setBackgroundResource(R.color.colorBackgroundTeam1);
            } else if (footballers.get(position).getTeam().equals(Constants.TEAM2)) {
                holder2.parentLayout.setBackgroundResource(R.color.colorBackgroundTeam2);
                holder2.tvName.setBackgroundResource(R.color.colorBackgroundTeam2);
                holder2.tvTeam.setBackgroundResource(R.color.colorBackgroundTeam2);
            }
        }

    }

    @Override
    public int getItemCount() {
        return footballers.size();
    }

    public void removeAt(int position) {
        footballers.remove(position);
        notifyDataSetChanged();
    }

    public void updateAt(int position, Footballer footballer) {
        footballers.remove(position);
        footballers.add(position, footballer);
        notifyDataSetChanged();
    }

    public void add(Footballer footballer) {
        footballers.add(footballer);
        notifyDataSetChanged();

    }

    public ArrayList<Footballer> getFootballers() {
        return footballers;
    }


    class ViewHolder1 extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView tvName;
        CheckBox cbIsPresent;
        LinearLayout parentLayout;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            cbIsPresent = itemView.findViewById(R.id.checkBox);
            parentLayout = itemView.findViewById(R.id.parentLayout);
            parentLayout.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo contextMenuInfo) {

            contextMenu.setHeaderTitle("Что вы хотите сделать?");
            contextMenu.add(getAdapterPosition(), R.id.action_delete, 0, "Удалить");
            contextMenu.add(getAdapterPosition(), R.id.action_change, 0, "Изменить");
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvTeam;
        ConstraintLayout parentLayout;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.textViewName);
            tvTeam = itemView.findViewById(R.id.textViewTeam);
            parentLayout = itemView.findViewById(R.id.parentLayoutWithTeams);
        }

    }
}


