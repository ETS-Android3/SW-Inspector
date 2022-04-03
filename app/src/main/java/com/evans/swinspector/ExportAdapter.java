package com.evans.swinspector;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExportAdapter extends RecyclerView.Adapter{

    private List<Monster> monsters;
    private List<Monster> selected;

    public ExportAdapter(List<Monster> monsters) {
        this.monsters = monsters;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public ExportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.export_list_item, parent, false);
        selected = new ArrayList<>();

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Monster monster = monsters.get(position);
        Context context = holder.itemView.getContext();
        AssetManager assets = context.getAssets();
        TextView tv_name = holder.itemView.findViewById(R.id.tv_import_name);
        ImageView iv_icon = holder.itemView.findViewById(R.id.iv_import_icon);
        CheckBox cb_export = holder.itemView.findViewById(R.id.cb_import_select);

        tv_name.setText(monster.name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cb_export.isChecked()){
                    cb_export.setChecked(false);
                    if(selected.contains(monster)) {
                        selected.remove(monster);
                    }
                }else {
                    cb_export.setChecked(true);
                    if(!selected.contains(monster)){
                        selected.add(monster);
                    }
                }
            }
        });

        Handler mHandler = new Handler();

        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is;
                    Bitmap bitmap;
                    is = assets.open("monsters/" + monster.unit_master_id + ".png");
                    bitmap = BitmapFactory.decodeStream(is);
                    iv_icon.setImageBitmap(bitmap);
                }catch(Exception ex){
                    ex.printStackTrace();
                    Log.e("Error", ex.toString());
                }
            }
        }, 10);
    }

    @Override
    public int getItemCount() {
        return monsters.size();
    }

    public List<Monster> getSelectedMonsters() { return selected; }
}
