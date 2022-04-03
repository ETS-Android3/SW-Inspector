package com.evans.swinspector;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.util.ChartUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverviewFragment extends Fragment {

    ScrollView scrollView;
    Button btn_scan;
    TextView tv_accountName;
    HorizontalBarChart horizontalBarChart;
    AppDatabase db;
    ActivityResultLauncher<Intent> requestJSON;
    ProgressBar pbAccount;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OverviewFragment newInstance(String param1, String param2) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestJSON = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Uri uri = data.getData();
                            StringBuilder stringBuilder = new StringBuilder();
                            try {
                                ContentResolver resolver = getContext().getContentResolver();
                                MimeTypeMap mime = MimeTypeMap.getSingleton();
                                if(mime.getExtensionFromMimeType(resolver.getType(uri)).equals("json")) {
                                    InputStream is = resolver.openInputStream(uri);
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                                    String line;

                                    while((line = reader.readLine()) != null) {
                                        stringBuilder.append(line);
                                    }
                                }else {
                                    stringBuilder = null;
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "File not valid.", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "File contained invalid JSON", Toast.LENGTH_SHORT).show();
                            }

                            try{
                                if(stringBuilder != null) {
                                    exportAccount(stringBuilder.toString());
                                }else {
                                    btn_scan.setEnabled(true);
                                    btn_scan.setText("Scan");
                                    Toast.makeText(getContext(), "Invalid file chosen.", Toast.LENGTH_SHORT).show();
                                }

                            }catch(Exception ex) {
                                Toast.makeText(getContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                                Log.e("Error", ex.toString());
                            }

                        } else {
                            btn_scan.setEnabled(true);
                            btn_scan.setText("Scan");
                        }
                    }
                });
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        scrollView = (ScrollView) view.findViewById(R.id.scroll_view);
        Button btn_createWidget = (Button) view.findViewById(R.id.btn_create_widget);
        tv_accountName = (TextView) view.findViewById(R.id.tv_account_name);
        horizontalBarChart = (HorizontalBarChart) view.findViewById(R.id.rune_horizontal);

        try{
            db = Room.databaseBuilder(getContext(), AppDatabase.class, "inspector").addMigrations(AppDatabase.MIGRATION_3_4).allowMainThreadQueries().build();
        }catch(Exception e){
            Log.e("Error", e.toString());
        }

        btn_scan = (Button) view.findViewById(R.id.btn_scan);
        pbAccount = (ProgressBar) view.findViewById(R.id.pb_account_import);

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View view) {
                btn_scan.setEnabled(false);
                btn_scan.setText("Importing...");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                try{
                    requestJSON.launch(intent);
                }catch(Exception ex) {
                    Toast.makeText(getContext(), ex.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_createWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    getContext().startService(new Intent(getContext(), InspectorService.class));
                } else if (Settings.canDrawOverlays(getContext())) {
                    getContext().startService(new Intent(getContext(), InspectorService.class));
                } else {
                    checkOverlayPermission();
                    Toast.makeText(getContext(), "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show();
                }
            }
        });

        db.close();

        updateAccountUI();
        // Inflate the layout for this fragment
        return view;
    }

    public void checkOverlayPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!Settings.canDrawOverlays(getContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(intent);
            } else {
                getContext().startService(new Intent(getContext(), InspectorService.class));
            }
        } else {
            Toast.makeText(getContext(), "You do not have the correct Android version to run the overlay.", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkStoragePermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int storageCheck = getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if(storageCheck < 0) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            }
        }
    }

    public void exportAccount(String readJson) {
        String monsterListJson = "";
        //Get the parser for monster id to name
        try {
            InputStream monsterNamesStream = getContext().getAssets().open("monsters.json");
            byte[] monsterArray = new byte[monsterNamesStream.available()];
            monsterNamesStream.read(monsterArray);
            monsterNamesStream.close();
            monsterListJson = new String(monsterArray);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONObject object = new JSONObject(readJson);
            HashMap<Integer, String> monsterMap = new Gson().fromJson(monsterListJson, new TypeToken<HashMap<Integer, String>>() {}.getType());
            String name = object.getJSONObject("wizard_info").get("wizard_name").toString();
            JSONArray monsters = object.getJSONArray("unit_list");
            JSONArray runes = object.getJSONArray("runes");

            List<AccountMonster> accountMonsters = new ArrayList<>();
            List<Rune> accountRunes = new ArrayList<>();

            //Convert monster master ID to name.
            for(int i = 0; i < monsters.length(); i++) {
                JSONArray monsterRunes = monsters.getJSONObject(i).getJSONArray("runes");
                for(int counter = 0; counter < monsterRunes.length(); counter++) {
                    accountRunes.add(toRune(monsterRunes.getJSONObject(counter)));
                }
                int shortened = Integer.parseInt(monsters.getJSONObject(i).getString("unit_master_id").substring(0, 3));
                if(monsterMap.containsKey(monsters.getJSONObject(i).getInt("unit_master_id"))){
                    accountMonsters.add(new AccountMonster(monsters.getJSONObject(i).getInt("unit_master_id"), monsterMap.get(monsters.getJSONObject(i).getInt("unit_master_id"))));
                }
            }

            for(int i = 0; i < runes.length(); i++) {
                accountRunes.add(toRune(runes.getJSONObject(i)));
            }

            Account account = new Account(name, accountMonsters);

            db = Room.databaseBuilder(getContext(), AppDatabase.class, "inspector").build();

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        if(!(db.accountDao().getAll().size() > 0)) {
                            db.accountDao().insertAll(account);
                        } else {
                            db.accountDao().delete(db.accountDao().getAll().get(0));
                            db.accountDao().insertAll(account);
                        }

                        if(db.runeDao().getAllAccountRune().size() > 0) {
                            db.runeDao().deleteAccountRunes();
                            db.runeDao().insertAsync(accountRunes);
                        } else {
                            db.runeDao().insertAsync(accountRunes);
                        }
                        updateAccountUI();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btn_scan.setEnabled(true);
                                btn_scan.setText("Scan");
                                Toast.makeText(getContext(), "Importing finished!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }catch(Exception ex){
                        Toast.makeText(getContext(), "There was a problem importing account data.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            thread.start();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.close();
    }

    public Rune toRune(JSONObject jsonRune) {
        try {
            List<Substat> stats = new ArrayList<>();
            long mid = jsonRune.getLong("rune_id");
            int slot = jsonRune.getInt("slot_no");
            int upgrade = jsonRune.getInt("upgrade_curr");
            int rank = jsonRune.getInt("rank");
            int naturalStar = jsonRune.getInt("class");
            Rune.Grade grade = Rune.IntToGrade(jsonRune.getInt("rank"));
            Rune.Set set = Rune.IntToSet(jsonRune.getInt("set_id"));
            JSONArray priEffect = jsonRune.getJSONArray("pri_eff");
            JSONArray prefixEffect = jsonRune.getJSONArray("prefix_eff");
            JSONArray secEff = jsonRune.getJSONArray("sec_eff");
            Rune.Type type = Rune.Type.REGULAR;

            if(rank > 10) {
                type = Rune.Type.ANCIENT;
            }

            for(int i = 0; i < secEff.length(); i++) {
                if(secEff.getJSONArray(i).getInt(3) > 0) {
                    stats.add(new Substat(secEff.getJSONArray(i).getInt(0), secEff.getJSONArray(i).getInt(1) + secEff.getJSONArray(i).getInt(3)));
                } else {
                    stats.add(new Substat(secEff.getJSONArray(i).getInt(0), secEff.getJSONArray(i).getInt(1)));
                }
            }
            if(prefixEffect.getInt(0) != 0) {
                Rune rune = new Rune(mid, grade, set, slot, upgrade, type, naturalStar, new Substat(priEffect.getInt(0), priEffect.getInt(1)), new Substat(prefixEffect.getInt(0), prefixEffect.getInt(1)), stats, true);
                return rune;
            } else {
                Rune rune = new Rune(mid, grade, set, slot, upgrade, type, naturalStar, new Substat(priEffect.getInt(0), priEffect.getInt(1)), null, stats, true);
                return rune;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateAccountUI() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(db.accountDao().getAll().size() > 0 && db.runeDao().getAllAccountRune().size() > 0){
                    Account account = db.accountDao().getAll().get(0);
                    List<SetCount> accountSets = db.runeDao().countAllSets();
                    try{
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pbAccount.setVisibility(View.VISIBLE);
                                tv_accountName.setText("Welcome back " + account.name + "!");
                                tv_accountName.setVisibility(View.VISIBLE);
                                if(accountSets.size() > 0) {
                                    ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                                    for(int i = 0; i < accountSets.size(); i++) {
                                        ArrayList<BarEntry> testSet = new ArrayList<>();
                                        testSet.add(new BarEntry(i, accountSets.get(i).count));
                                        BarDataSet barDataSet = new BarDataSet(testSet, accountSets.get(i).set.toString());
                                        barDataSet.setColor(ChartUtils.pickColor());
                                        CustomFormatter format = new CustomFormatter(accountSets.get(i).set.toString());
                                        barDataSet.setValueFormatter(format);
                                        dataSets.add(barDataSet);
                                    }
                                    BarData barData = new BarData(dataSets);
                                    horizontalBarChart.setData(barData);
                                    horizontalBarChart.getXAxis().setEnabled(false);
                                    horizontalBarChart.getAxisLeft().setDrawGridLines(false);
                                    horizontalBarChart.getAxisLeft().setEnabled(false);
                                    horizontalBarChart.getAxisRight().setDrawGridLines(false);
                                    horizontalBarChart.setDescription(null);
                                    horizontalBarChart.getLegend().setEnabled(false);
                                    horizontalBarChart.setScaleEnabled(false);
                                    horizontalBarChart.invalidate();
                                    horizontalBarChart.setVisibility(View.VISIBLE);
                                    pbAccount.setVisibility(View.GONE);
                                }
                            }
                        });
                    }catch(Exception ex) {

                    }

                    db.close();
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pbAccount.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        thread.start();


    }

    class CustomFormatter extends ValueFormatter {
        String label;
        public CustomFormatter(String label) {
            this.label = label;
        }

        @Override
        public String getFormattedValue(float value) {
            return String.format("%.0f", value) + " " + label;
        }
    }
}