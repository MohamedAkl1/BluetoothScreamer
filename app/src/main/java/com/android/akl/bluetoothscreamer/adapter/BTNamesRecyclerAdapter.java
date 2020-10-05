package com.android.akl.bluetoothscreamer.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.akl.bluetoothscreamer.R;

import java.util.ArrayList;
import java.util.List;

public class BTNamesRecyclerAdapter  extends RecyclerView.Adapter<BTNamesRecyclerAdapter.ViewHolder>{

    private List<String> names;
    private LayoutInflater mLayoutInflater;

    public BTNamesRecyclerAdapter(Context context, List<String> names1) {

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
        Button mButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            bTNameTextView = itemView.findViewById(R.id.bt_device_name_tv);
            mButton = itemView.findViewById(R.id.edit_device_edit_text);
        }
    }
}
