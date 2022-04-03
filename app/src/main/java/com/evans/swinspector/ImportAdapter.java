package com.evans.swinspector;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ImportAdapter extends RecyclerView.Adapter{

    private List<Monster> monsters;
    private List<Monster> selected;

    public ImportAdapter(List<Monster> monsters) {
        this.monsters = monsters;
        this.selected = monsters;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.import_list_item, parent, false);

        ImportAdapter.ViewHolder holder = new ImportAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Monster monster = monsters.get(position);
        Context context = holder.itemView.getContext();
        AssetManager assets = context.getAssets();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        TextView tv_name = holder.itemView.findViewById(R.id.tv_import_name);
        ImageView iv_icon = holder.itemView.findViewById(R.id.iv_import_icon);
        CheckBox cb_import = holder.itemView.findViewById(R.id.cb_import_select);
        Button btn_view = holder.itemView.findViewById(R.id.btn_import_view);
        cb_import.setChecked(true);
        tv_name.setText(monster.name);

        ViewMonsterDialog dialog = new ViewMonsterDialog(context, monster);

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                dialog.getWindow().setLayout((6 * width) / 7, (6 * height) / 7);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cb_import.isChecked()) {
                    cb_import.setChecked(false);
                    if(selected.contains(monster)) {
                        selected.remove(monster);
                    }
                } else {
                    cb_import.setChecked(true);
                    if(!selected.contains(monster)) {
                        selected.add(monster);
                    }
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dialog.show();
                dialog.getWindow().setLayout((6 * width) / 7, (6 * height) / 7);
                return false;
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
                }
            }
        }, 10);
    }

    @Override
    public int getItemCount() {
        return monsters.size();
    }

    public List<Monster> getSelected() { return selected; }
}
