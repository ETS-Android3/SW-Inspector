package com.evans.swinspector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ImportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "inspector").allowMainThreadQueries().build();
        String json = getIntent().getExtras().getString("import");
        Gson gson = new Gson();
        Type type = new TypeToken<List<Monster>>() {}.getType();
        List<Monster> monsters = gson.fromJson(json, type);

        RecyclerView rv_import = (RecyclerView) findViewById(R.id.rv_import);
        ImportAdapter adapter = new ImportAdapter(monsters);
        rv_import.setAdapter(adapter);
        rv_import.setLayoutManager(new LinearLayoutManager(this));

        Button btn_import = (Button) findViewById(R.id.btn_import);

        btn_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Monster> selected = adapter.getSelected();

                for(Monster update : selected) {
                    db.monsterDao().update(update.unit_master_id, update.atk, update.def, update.hp, update.spd, update.crit_rate, update.crit_damage, update.resistance, update.accuracy, update.slot_two_pref, update.slot_four_pref, update.slot_six_pref, update.set_pref);
                }
                Toast.makeText(ImportActivity.this, "Import finished.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}