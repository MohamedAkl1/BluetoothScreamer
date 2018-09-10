package com.android.akl.bluetoothscreamer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.akl.bluetoothscreamer.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Mohamed Akl on 8/22/2018.
 */
public class WelcomeRecyclerAdapter extends RecyclerView.Adapter<WelcomeRecyclerAdapter.WelcomeViewHolder> {

    private List<String> names;
    private LayoutInflater mLayoutInflater;
    private static List<String> selectedNames, notSelectedNames;
    private Context mContext;

    public WelcomeRecyclerAdapter(Context context,Set<String> devicesNames){
        names = new ArrayList<>();
        this.names.addAll(devicesNames);
        this.mLayoutInflater = LayoutInflater.from(context);
        selectedNames = new ArrayList<>();
        this.mContext = context;
        notSelectedNames = new ArrayList<>(devicesNames);
    }

    @NonNull
    @Override
    public WelcomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mLayoutInflater.inflate(R.layout.welcome_recycler_item,viewGroup,false);
        return new WelcomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WelcomeViewHolder welcomeViewHolder, int i) {
        welcomeViewHolder.pairedName.setText(names.get(i));
        Toast.makeText(mContext,"added "+names.get(i),Toast.LENGTH_SHORT).show();
        welcomeViewHolder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(welcomeViewHolder.addButton.getText().toString().trim().equals(mContext.getResources().getString(R.string.welcome_dialog_add_button))){
                    selectedNames.add(names.get(welcomeViewHolder.getAdapterPosition()));
                    Toast.makeText(mContext,"added", Toast.LENGTH_SHORT).show();
                    welcomeViewHolder.addButton.setText(R.string.added);
                    notSelectedNames.remove(names.get(welcomeViewHolder.getAdapterPosition()));
                }else {
                    welcomeViewHolder.addButton.setText(R.string.add);
                    welcomeViewHolder.addButton.setEnabled(true);
                    Toast.makeText(mContext,"Removed",Toast.LENGTH_SHORT).show();
                    selectedNames.remove(welcomeViewHolder.getAdapterPosition());
                    notSelectedNames.add(names.get(welcomeViewHolder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    class WelcomeViewHolder extends RecyclerView.ViewHolder{

        TextView pairedName;
        Button addButton;

        WelcomeViewHolder(@NonNull View itemView) {
            super(itemView);

            pairedName = itemView.findViewById(R.id.welcome_dialog_bt_name_tv);
            addButton = itemView.findViewById(R.id.welcome_add_button);
        }
    }

    public static List<String> getChosenNames(){
        return selectedNames;
    }

    public static List<String> getUnchoosenNames(){
        return notSelectedNames;
    }
}
