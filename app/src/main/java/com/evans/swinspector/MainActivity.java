package com.evans.swinspector;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    AppDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottom_nav = (BottomNavigationView) findViewById(R.id.bottom_nav);
        NavHostFragment hostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = null;
        if (hostFragment != null) {
            navController = hostFragment.getNavController();
            NavigationUI.setupWithNavController(bottom_nav, navController);
        }else {
            Toast.makeText(getApplicationContext(), "There was an issue creating the navigation panel.", Toast.LENGTH_SHORT).show();
        }

        try {
            initializeDatabase();
        }catch(Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
        checkStoragePermission();
        checkOverlayPermission();
    }

    public void checkOverlayPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getApplicationContext().startService(new Intent(getApplicationContext(), InspectorService.class));
        } else if (Settings.canDrawOverlays(getApplicationContext())) {
            getApplicationContext().startService(new Intent(getApplicationContext(), InspectorService.class));
        } else {
            checkOverlayPermission();
            Toast.makeText(getApplicationContext(), "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkStoragePermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int storageCheck = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if(storageCheck < 0) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initializeDatabase() {
        String monsterListJson = "";
        List<Monster> massInsert = new ArrayList<>();
        //Get the parser for monster id to name
        try {
            InputStream monsterNamesStream = getApplicationContext().getAssets().open("allMonsters.json");
            byte[] monsterArray = new byte[monsterNamesStream.available()];
            monsterNamesStream.read(monsterArray);
            monsterNamesStream.close();
            monsterListJson = new String(monsterArray);
            JSONArray monsters = new JSONArray(monsterListJson);
            for(int i = 0; i < monsters.length(); i++) {
                JSONObject monster = monsters.getJSONObject(i);
                Monster insert;
                if(!monsters.getJSONObject(i).isNull("second_awaken")) {
                    insert = new Monster(
                            monster.getString("name"),
                            monster.getInt("master_unit_id"),
                            monster.getInt("atk"),
                            monster.getInt("def"),
                            monster.getInt("hp"),
                            monster.getInt("speed"),
                            monster.getInt("crit_rate"),
                            monster.getInt("crit_dmg"),
                            monster.getInt("resistance"),
                            monster.getInt("accuracy"),
                            null,
                            null,
                            null,
                            null,
                            monster.getInt("second_awaken"));
                    massInsert.add(insert);
                }else {
                    insert = new Monster(
                            monster.getString("name"),
                            monster.getInt("master_unit_id"),
                            monster.getInt("atk"),
                            monster.getInt("def"),
                            monster.getInt("hp"),
                            monster.getInt("speed"),
                            monster.getInt("crit_rate"),
                            monster.getInt("crit_dmg"),
                            monster.getInt("resistance"),
                            monster.getInt("accuracy"),
                            null,
                            null,
                            null,
                            null,
                            null);
                    massInsert.add(insert);
                }
            }
            Log.e("monster", massInsert.size() + "");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "inspector").addMigrations(AppDatabase.MIGRATION_1_2, AppDatabase.MIGRATION_2_3, AppDatabase.MIGRATION_3_4).fallbackToDestructiveMigrationOnDowngrade().allowMainThreadQueries().build();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(massInsert.size() > 0){
                    if(db.monsterDao().getAll().size() != massInsert.size()) {
                        db.monsterDao().insertAsync(massInsert);
                    }
                }
            }
        });
        try{
            thread.start();
        }catch(Exception ex) {
            ex.printStackTrace();
        }


        db.close();
    }
}