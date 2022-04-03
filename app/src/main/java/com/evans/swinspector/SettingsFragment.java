package com.evans.swinspector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    SettingsHandler settingsHandler;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        this.settingsHandler = new SettingsHandler(getContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Button btn_export = (Button) view.findViewById(R.id.btn_settings_export);
        Button btn_import = (Button) view.findViewById(R.id.btn_settings_import);
        Button btn_settings_save = (Button) view.findViewById(R.id.btn_settings_save);
        TextView tv_settings_flat = (TextView) view.findViewById(R.id.tv_settings_flat);
        EditText et_settings_flat = (EditText) view.findViewById(R.id.et_settings_flat);
        CheckBox cb_settings_flat = (CheckBox) view.findViewById(R.id.cb_settings_flat);
        TextView tv_settings_set = (TextView) view.findViewById(R.id.tv_settings_set);
        EditText et_settings_set = (EditText) view.findViewById(R.id.et_settings_set);
        CheckBox cb_settings_set = (CheckBox) view.findViewById(R.id.cb_settings_set);

        ActivityResultLauncher<Intent> importWeights = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    importJson(data.getData());
                }

            }
        });

        btn_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ExportActivity.class);
                startActivity(intent);
            }
        });

        btn_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                importWeights.launch(intent);
            }
        });

        if(settingsHandler.reduceFlats()) {
            cb_settings_flat.setChecked(true);
            tv_settings_flat.setVisibility(View.VISIBLE);
            et_settings_flat.setVisibility(View.VISIBLE);
            et_settings_flat.setText(String.valueOf(settingsHandler.getFlatWeight()));
        }

        if(settingsHandler.includeSet()) {
            cb_settings_set.setChecked(true);
            tv_settings_set.setVisibility(View.VISIBLE);
            et_settings_set.setVisibility(View.VISIBLE);
            et_settings_set.setText(String.valueOf(settingsHandler.getSetWeight()));
        }

        cb_settings_flat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb_settings_flat.isChecked()) {
                    tv_settings_flat.setVisibility(View.VISIBLE);
                    et_settings_flat.setVisibility(View.VISIBLE);
                } else {
                    tv_settings_flat.setVisibility(View.GONE);
                    et_settings_flat.setVisibility(View.GONE);
                }
            }
        });

        cb_settings_set.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb_settings_set.isChecked()) {
                    tv_settings_set.setVisibility(View.VISIBLE);
                    et_settings_set.setVisibility(View.VISIBLE);
                } else {
                    tv_settings_set.setVisibility(View.GONE);
                    et_settings_set.setVisibility(View.GONE);
                }
            }
        });

        btn_settings_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean failed = false;
                if(cb_settings_flat.isChecked()) {
                    try {
                        double flatWeight = Double.parseDouble(et_settings_flat.getText().toString());
                        if(flatWeight <= 1) {
                            settingsHandler.writeReduceFlats(true);
                            settingsHandler.modifyFlatWeight(flatWeight);
                        } else {
                            failed = true;
                        }

                    }catch(Exception ex) {
                        failed = true;
                    }
                }else {
                    settingsHandler.writeReduceFlats(false);
                    settingsHandler.modifyFlatWeight(0.0);
                }
                if(cb_settings_set.isChecked()) {
                    try{
                        double flatWeight = Double.parseDouble(et_settings_set.getText().toString());
                        if(flatWeight <= 1) {
                            settingsHandler.writeSet(true);
                            settingsHandler.modifySetWeight(flatWeight);
                        } else {
                            failed = true;
                        }

                    }catch(Exception ex) {
                        failed = true;
                    }
                }else {
                    settingsHandler.writeSet(false);
                    settingsHandler.modifySetWeight(0.0);
                }

                if(failed) {
                    Toast.makeText(getContext(), "There was an issue saving the weights. Please enter a number between 0 and 1.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Settings applied.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    public void importJson(Uri uri) {
        String json;
        try {
            InputStream is = getContext().getContentResolver().openInputStream(uri);
            byte[] isArray = new byte[is.available()];
            is.read(isArray);
            is.close();
            json = new String(isArray);
            Intent intent = new Intent(getContext(), ImportActivity.class);
            intent.putExtra("import", json);
            startActivity(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}