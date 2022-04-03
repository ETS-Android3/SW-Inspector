package com.evans.swinspector;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class ViewMonsterDialog extends Dialog implements View.OnClickListener {
    Monster monster;
    public ViewMonsterDialog(@NonNull Context context, Monster monster) {
        super(context);
        this.monster = monster;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_monster_dialog);
        TextView tv_view_name = (TextView) findViewById(R.id.tv_view_name);
        tv_view_name.setText(monster.name);
        TextView tv_view_monster_atk_value = (TextView) findViewById(R.id.tv_view_atk_value);
        if(monster.atk != 0) {
            tv_view_monster_atk_value.setText(String.valueOf(monster.atk));
        }
        TextView tv_view_monster_def_value = (TextView) findViewById(R.id.tv_view_def_value);
        if(monster.def != 0) {
            tv_view_monster_def_value.setText(String.valueOf(monster.def));
        }
        TextView tv_view_monster_hp_value = (TextView) findViewById(R.id.tv_view_hp_value);
        if(monster.hp != 0) {
            tv_view_monster_hp_value.setText(String.valueOf(monster.hp));
        }
        TextView tv_view_monster_spd_value = (TextView) findViewById(R.id.tv_view_spd_value);
        if(monster.spd != 0) {
            tv_view_monster_spd_value.setText(String.valueOf(monster.spd));
        }
        TextView tv_view_monster_crit_rate_value = (TextView) findViewById(R.id.tv_view_crit_rate_value);
        if(monster.crit_rate != 0) {
            tv_view_monster_crit_rate_value.setText(String.valueOf(monster.crit_rate));
        }
        TextView tv_view_monster_crit_dmg_value = (TextView) findViewById(R.id.tv_view_crit_dmg_value);
        if(monster.crit_damage != 0) {
            tv_view_monster_crit_dmg_value.setText(String.valueOf(monster.crit_damage));
        }
        TextView tv_view_monster_resistance_value = (TextView) findViewById(R.id.tv_view_resistance_value);
        if(monster.resistance != 0) {
            tv_view_monster_resistance_value.setText(String.valueOf(monster.resistance));
        }
        TextView tv_view_monster_accuracy_value = (TextView) findViewById(R.id.tv_view_accuracy_value);
        if(monster.accuracy != 0) {
            tv_view_monster_accuracy_value.setText(String.valueOf(monster.accuracy));
        }

        LinearLayout setGrid = findViewById(R.id.ll_set_spinners);
        if(monster.set_pref != null && monster.set_pref.size() > 0) {
            for(Rune.Set set : monster.set_pref) {
                TextView tv_set = new TextView(getContext());
                tv_set.setText(set.toString());
                setGrid.addView(tv_set, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
            }
        }

        GridLayout grid_slot_two_pref = (GridLayout) findViewById(R.id.gl_slot_two_pref);
        GridLayout grid_slot_four_pref = (GridLayout) findViewById(R.id.gl_slot_four_pref);
        GridLayout grid_slot_six_pref = (GridLayout) findViewById(R.id.gl_slot_six_pref);

        if(monster.slot_two_pref != null && monster.slot_two_pref.size() > 0) {
            for(Substat.StatType stat : monster.slot_two_pref){
                for(int i = 0; i < grid_slot_two_pref.getChildCount(); i++) {
                    if(stat.toString().equals(grid_slot_two_pref.getChildAt(i).getTag())) {
                        CheckBox cb_slot_pref = (CheckBox) grid_slot_two_pref.getChildAt(i);
                        cb_slot_pref.setChecked(true);
                    }
                }
            }
        }

        if(monster.slot_four_pref != null && monster.slot_four_pref.size() > 0) {
            for(Substat.StatType stat : monster.slot_four_pref){
                for(int i = 0; i < grid_slot_four_pref.getChildCount(); i++) {
                    if(stat.toString().equals(grid_slot_four_pref.getChildAt(i).getTag())) {
                        CheckBox cb_slot_pref = (CheckBox) grid_slot_four_pref.getChildAt(i);
                        cb_slot_pref.setChecked(true);
                    }
                }
            }
        }

        if(monster.slot_six_pref != null && monster.slot_six_pref.size() > 0) {
            for(Substat.StatType stat : monster.slot_six_pref){
                for(int i = 0; i < grid_slot_six_pref.getChildCount(); i++) {
                    if(stat.toString().equals(grid_slot_six_pref.getChildAt(i).getTag())) {
                        CheckBox cb_slot_pref = (CheckBox) grid_slot_six_pref.getChildAt(i);
                        cb_slot_pref.setChecked(true);
                    }
                }
            }
        }

        Button btn_dismiss = findViewById(R.id.btn_view_monster_dismiss);

        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
