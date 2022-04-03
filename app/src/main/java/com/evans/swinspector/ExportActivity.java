package com.evans.swinspector;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ExportActivity extends AppCompatActivity {

    AppDatabase db;
    RecyclerView export;
    String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        RecyclerView exportList = findViewById(R.id.rv_export);
        Button btn_export_all = findViewById(R.id.btn_export_all);
        Button btn_export_selected = findViewById(R.id.btn_export);

        db = Room.databaseBuilder(this, AppDatabase.class, "inspector").allowMainThreadQueries().build();
        List<Monster> monsters = new ArrayList<>();

        for(Monster monster : db.monsterDao().getAll()) {
            if(!monster.checkDefault()) {
                if(!monsters.contains(monster)) {
                    monsters.add(monster);
                }
            }
        }

        ExportAdapter adapter = new ExportAdapter(monsters);
        exportList.setAdapter(adapter);
        exportList.setLayoutManager(new LinearLayoutManager(this));

        ActivityResultLauncher<Intent> saveWeights = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    exportJson(data.getData());
                }

            }
        });

        btn_export_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch(i){
                            case DialogInterface.BUTTON_POSITIVE:
                                json = new Gson().toJson(monsters);
                                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setType("*/*");
                                intent.putExtra(Intent.EXTRA_TITLE, "weights.json");
                                saveWeights.launch(intent);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialogInterface.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ExportActivity.this);
                builder.setMessage("Export " + monsters.size() + " monsters?").setPositiveButton("Yes", dialogClick).setNegativeButton("No", dialogClick).show();
            }
        });

        btn_export_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Monster> export = adapter.getSelectedMonsters();
                if(export.size() > 0) {
                    json = new Gson().toJson(export);
                    Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("*/*");
                    intent.putExtra(Intent.EXTRA_TITLE, "weights.json");
                    saveWeights.launch(intent);
                } else {
                    Toast.makeText(ExportActivity.this, "Please select monster(s) before trying to export.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void exportJson(Uri uri) {
        try {
            OutputStream output = getContentResolver().openOutputStream(uri);
            output.write(json.getBytes());
            output.flush();
            output.close();
            Toast.makeText(ExportActivity.this, "Monsters exported.", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}