package com.evans.swinspector;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;

public class MonsterAdapter extends RecyclerView.Adapter<MonsterAdapter.ViewHolder>{

    private List<Monster> monsters;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public MonsterAdapter(List<Monster> monsters){ this.monsters = monsters; }

    @NonNull
    @Override
    public MonsterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View detailView = inflater.inflate(R.layout.monster_list_item, parent, false);

        MonsterAdapter.ViewHolder viewHolder = new MonsterAdapter.ViewHolder(detailView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MonsterAdapter.ViewHolder holder, int position) {
        Monster monster = monsters.get(position);
        TextView tv_monster_name = holder.itemView.findViewById(R.id.tv_monster_name);
        ImageView iv_monster_icon = holder.itemView.findViewById(R.id.iv_monster_icon);
        ImageView iv_edited = holder.itemView.findViewById(R.id.iv_edited);
        Button btn_modify_monster = holder.itemView.findViewById(R.id.btn_modify_monster);
        Context context = holder.itemView.getContext();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        AssetManager assets = context.getAssets();
        if(!monster.checkDefault()) {
            iv_edited.setVisibility(View.VISIBLE);
        } else {
            iv_edited.setVisibility(View.GONE);
        }
        ModifyMonsterDialog dialog = new ModifyMonsterDialog(context, monster);
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
                    iv_monster_icon.setImageBitmap(bitmap);
                }catch(Exception ex){
                    ex.printStackTrace();
                    Log.e("Error", ex.toString());
                }
            }
        }, 10);
        tv_monster_name.setText(monster.name);

        btn_modify_monster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                dialog.getWindow().setLayout((6 * width) / 7, (6 * height) / 7);
            }
        });
    }

    @Override
    public int getItemCount() {
        return monsters.size();
    }
}
