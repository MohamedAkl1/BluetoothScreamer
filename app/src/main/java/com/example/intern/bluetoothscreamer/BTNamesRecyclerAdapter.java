package com.example.intern.bluetoothscreamer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BTNamesRecyclerAdapter  extends RecyclerView.Adapter<BTNamesRecyclerAdapter.ViewHolder>{

    private List<String> names;
    private LayoutInflater mLayoutInflater;

    public BTNamesRecyclerAdapter(Context context, Set<String> names1) {

        names = new ArrayList<>();
        this.names.addAll(names1);
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mLayoutInflater.inflate(R.layout.bt_name_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bTNameTextView.setText(names.get(i));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }
    
    class ViewHolder extends RecyclerView.ViewHolder{

        TextView bTNameTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            bTNameTextView = itemView.findViewById(R.id.bt_device_name_tv);
        }
    }
}
