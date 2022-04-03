package com.evans.swinspector;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class ModifyMonsterDialog extends Dialog implements View.OnClickListener {
    Monster monster;
    public ModifyMonsterDialog(@NonNull Context context, Monster monster) {
        super(context);
        this.monster = monster;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.modify_monster_dialog);
        TextView tv_modify_monster_name = findViewById(R.id.tv_modify_monster_name);
        GridLayout gl_slot_two_pref = findViewById(R.id.gl_slot_two_pref);
        GridLayout gl_slot_four_pref = findViewById(R.id.gl_slot_four_pref);
        GridLayout gl_slot_six_pref = findViewById(R.id.gl_slot_six_pref);
        EditText ev_modify_monster_atk = (EditText) findViewById(R.id.ev_modify_atk);
        ev_modify_monster_atk.setOnFocusChangeListener(new focusChange());
        if(monster.atk != 0) {
            ev_modify_monster_atk.setText(String.valueOf(monster.atk));
        }
        EditText ev_modify_monster_def = (EditText) findViewById(R.id.ev_modify_def);
        ev_modify_monster_def.setOnFocusChangeListener(new focusChange());
        if(monster.def != 0) {
            ev_modify_monster_def.setText(String.valueOf(monster.def));
        }
        EditText ev_modify_monster_hp = (EditText) findViewById(R.id.ev_modify_hp);
        ev_modify_monster_hp.setOnFocusChangeListener(new focusChange());
        if(monster.hp != 0) {
            ev_modify_monster_hp.setText(String.valueOf(monster.hp));
        }
        EditText ev_modify_monster_speed = (EditText) findViewById(R.id.ev_modify_spd);
        ev_modify_monster_speed.setOnFocusChangeListener(new focusChange());
        if(monster.spd != 0) {
            ev_modify_monster_speed.setText(String.valueOf(monster.spd));
        }
        EditText ev_modify_monster_crit_rate = (EditText) findViewById(R.id.ev_modify_crit_rate);
        ev_modify_monster_crit_rate.setOnFocusChangeListener(new focusChange());
        if(monster.crit_rate != 0) {
            ev_modify_monster_crit_rate.setText(String.valueOf(monster.crit_rate));
        }
        EditText ev_modify_monster_crit_dmg = (EditText) findViewById(R.id.ev_modify_crit_dmg);
        ev_modify_monster_crit_dmg.setOnFocusChangeListener(new focusChange());
        if(monster.crit_damage != 0) {
            ev_modify_monster_crit_dmg.setText(String.valueOf(monster.crit_damage));
        }
        EditText ev_modify_monster_resistance = (EditText) findViewById(R.id.ev_modify_resistance);
        ev_modify_monster_resistance.setOnFocusChangeListener(new focusChange());
        if(monster.resistance != 0) {
            ev_modify_monster_resistance.setText(String.valueOf(monster.resistance));
        }
        EditText ev_modify_monster_accuracy = (EditText) findViewById(R.id.ev_modify_accuracy);
        ev_modify_monster_accuracy.setOnFocusChangeListener(new focusChange());
        if(monster.accuracy != 0) {
            ev_modify_monster_accuracy.setText(String.valueOf(monster.accuracy));
        }
        LinearLayout spinnerGrid = findViewById(R.id.ll_set_spinners);
        Button btn_modify_monster_dismiss = findViewById(R.id.btn_view_monster_dismiss);
        Button btn_modify_monster_save = findViewById(R.id.btn_modify_monster_save);
        Button btn_add_set_spinner = findViewById(R.id.btn_add_set_spinner);
        Spinner spinner = findViewById(R.id.set_spinner);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(), R.array.sets, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if(monster.set_pref != null && monster.set_pref.size() > 0){
            for(int i = 0; i < monster.set_pref.size(); i++){
                if(i == 0) {
                    spinner.setSelection(setToInt(monster.set_pref.get(i)));
                }else {
                    Spinner spin = new Spinner(getContext());
                    spin.setAdapter(adapter);
                    spin.setSelection(setToInt(monster.set_pref.get(i)));
                    spinnerGrid.addView(spin, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                }
            }
        }

        if(monster.slot_two_pref != null && monster.slot_two_pref.size() > 0){
            for(Substat.StatType stat : monster.slot_two_pref){
                for(int i = 0; i < gl_slot_two_pref.getChildCount(); i++) {
                    if(stat.toString().equals(gl_slot_two_pref.getChildAt(i).getTag())){
                        CheckBox cb_stat = (CheckBox) gl_slot_two_pref.getChildAt(i);
                        cb_stat.setChecked(true);
                    }
                }
            }
        }

        if(monster.slot_four_pref != null && monster.slot_four_pref.size() > 0){
            for(Substat.StatType stat : monster.slot_four_pref){
                for(int i = 0; i < gl_slot_four_pref.getChildCount(); i++) {
                    if(stat.toString().equals(gl_slot_four_pref.getChildAt(i).getTag())){
                        CheckBox cb_stat = (CheckBox) gl_slot_four_pref.getChildAt(i);
                        cb_stat.setChecked(true);
                    }
                }
            }
        }

        if(monster.slot_six_pref != null && monster.slot_six_pref.size() > 0){
            for(Substat.StatType stat : monster.slot_six_pref){
                for(int i = 0; i < gl_slot_six_pref.getChildCount(); i++) {
                    if(stat.toString().equals(gl_slot_six_pref.getChildAt(i).getTag())){
                        CheckBox cb_stat = (CheckBox) gl_slot_six_pref.getChildAt(i);
                        cb_stat.setChecked(true);
                    }
                }
            }
        }

        btn_modify_monster_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btn_modify_monster_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    int atk = Integer.parseInt(ev_modify_monster_atk.getText().toString());
                    int def = Integer.parseInt(ev_modify_monster_def.getText().toString());
                    int hp = Integer.parseInt(ev_modify_monster_hp.getText().toString());
                    int spd = Integer.parseInt(ev_modify_monster_speed.getText().toString());
                    int crit_rate = Integer.parseInt(ev_modify_monster_crit_rate.getText().toString());
                    int crit_dmg = Integer.parseInt(ev_modify_monster_crit_dmg.getText().toString());
                    int resistance = Integer.parseInt(ev_modify_monster_resistance.getText().toString());
                    int accuracy = Integer.parseInt(ev_modify_monster_accuracy.getText().toString());

                    List<Substat.StatType> slot_two_pref = null;
                    List<Substat.StatType> slot_four_pref = null;
                    List<Substat.StatType> slot_six_pref = null;
                    List<Rune.Set> preferredSets = new ArrayList<>();
                    if(getSubsFromGrid(gl_slot_two_pref) != null){
                        slot_two_pref = getSubsFromGrid(gl_slot_two_pref);
                    }
                    if(getSubsFromGrid(gl_slot_four_pref) != null){
                        slot_four_pref = getSubsFromGrid(gl_slot_four_pref);
                    }
                    if(getSubsFromGrid(gl_slot_six_pref) != null){
                        slot_six_pref = getSubsFromGrid(gl_slot_six_pref);
                    }
                    for(int i = 0; i < spinnerGrid.getChildCount(); i++) {
                        Spinner value = (Spinner) spinnerGrid.getChildAt(i);
                        if(!value.getSelectedItem().toString().toUpperCase().equals("NONE")) {
                            Rune.Set set = Rune.Set.valueOf(value.getSelectedItem().toString().toUpperCase());
                            if(!preferredSets.contains(set)){
                                preferredSets.add(Rune.Set.valueOf(value.getSelectedItem().toString().toUpperCase()));
                            }
                        }
                    }

                    if(atk >= 0 && atk <= 5 && def >= 0 && def <= 5 && hp >= 0 && hp <= 5 && spd >= 0 && spd <= 5 && crit_rate >= 0 && crit_rate <= 5 && crit_dmg >= 0 && crit_dmg <= 5 && resistance >= 0 && resistance <= 5  && accuracy >= 0 && accuracy <= 5) {
                        Monster update = new Monster(
                                monster.name,
                                monster.unit_master_id,
                                atk,
                                def,
                                hp,
                                spd,
                                crit_rate,
                                crit_rate,
                                resistance,
                                accuracy,
                                slot_two_pref,
                                slot_four_pref,
                                slot_six_pref,
                                preferredSets,
                                monster.second_awaken
                                );
                        AppDatabase db = Room.databaseBuilder(getContext(), AppDatabase.class, "inspector").build();
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                db.monsterDao().update(update.unit_master_id, update.atk, update.def, update.hp, update.spd, update.crit_rate, update.crit_damage, update.resistance, update.accuracy, update.slot_two_pref, update.slot_four_pref, update.slot_six_pref, update.set_pref);
                                dismiss();
                            }
                        });
                        thread.start();
                    }else{
                        Toast.makeText(getContext(), "Values must be within 0-5", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(getContext(), "Values are incorrect. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_add_set_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner spin = new Spinner(getContext());
                if(spinnerGrid.getChildCount() < 4) {
                    spin.setAdapter(adapter);
                    spinnerGrid.addView(spin, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                }else {
                    Toast.makeText(getContext(), "You can only have 3 preferred sets.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_modify_monster_name.setText(monster.name);
    }

    @Override
    public void onClick(View view) {

    }

    public int setToInt(Rune.Set set) {
        if(set == Rune.Set.ACCURACY) {
            return 1;
        }else if(set == Rune.Set.BLADE) {
            return 2;
        }else if(set == Rune.Set.DESPAIR) {
            return 3;
        }else if(set == Rune.Set.DESTROY) {
            return 4;
        }else if(set == Rune.Set.DETERMINATION) {
            return 5;
        }else if(set == Rune.Set.ENDURE) {
            return 6;
        }else if(set == Rune.Set.ENERGY) {
            return 7;
        }else if(set == Rune.Set.ENHANCE) {
            return 8;
        }else if(set == Rune.Set.FATAL) {
            return 9;
        }else if(set == Rune.Set.FIGHT) {
            return 10;
        }else if(set == Rune.Set.FOCUS) {
            return 11;
        }else if(set == Rune.Set.GUARD) {
            return 12;
        }else if(set == Rune.Set.NEMESIS) {
            return 13;
        }else if(set == Rune.Set.RAGE) {
            return 14;
        }else if(set == Rune.Set.REVENGE) {
            return 15;
        }else if(set == Rune.Set.SHIELD) {
            return 16;
        }else if(set == Rune.Set.SWIFT) {
            return 17;
        }else if(set == Rune.Set.TOLERANCE) {
            return 18;
        }else if(set == Rune.Set.VAMPIRE) {
            return 19;
        }else if(set == Rune.Set.VIOLENT) {
            return 20;
        }else if(set == Rune.Set.WILL) {
            return 21;
        }else {
            return 0;
        }
    }

    public List<Substat.StatType> getSubsFromGrid(GridLayout grid) {
        List<Substat.StatType> stats = new ArrayList<>();
        for(int i = 0; i < grid.getChildCount(); i++) {
            CheckBox cb_value = (CheckBox) grid.getChildAt(i);
            if(cb_value.isChecked()) {
                stats.add(Substat.StatType.valueOf(cb_value.getTag().toString()));
            }
        }
        if(stats.size() > 0) {
            return stats;
        }
        return null;
    }
    public class focusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean b) {
            EditText cast = (EditText) view;
            cast.setSelection(cast.getText().length());
        }
    }
}
