package com.evans.swinspector;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MonsterViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonsterViewFragment extends Fragment {

    RecyclerView monsterRecycler;
    CheckBox isOwned;
    CheckBox modifiedMonsters;
    GridLayout filter;
    ImageView expand;
    EditText ev_monster_view_search;
    AppDatabase db;
    List<Monster> monsters;
    Handler mHandler;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MonsterViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonsterViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MonsterViewFragment newInstance(String param1, String param2) {
        MonsterViewFragment fragment = new MonsterViewFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monster_view, container, false);
        monsterRecycler = (RecyclerView)  view.findViewById(R.id.rv_monster);
        isOwned = (CheckBox) view.findViewById(R.id.cb_owned_monsters);
        modifiedMonsters = (CheckBox) view.findViewById(R.id.cb_modify_monster_modified);
        filter = (GridLayout) view.findViewById(R.id.gl_monsters_filter);
        expand = (ImageView) view.findViewById(R.id.iv_modify_monster_expand);
        ev_monster_view_search = (EditText) view.findViewById(R.id.ev_monster_view_search);
        mHandler = new Handler();
        db = Room.databaseBuilder(getContext(), AppDatabase.class, "inspector").allowMainThreadQueries().build();
        if(db.accountDao().getAll().size() == 0) {
            isOwned.setEnabled(false);
        }else{
            isOwned.setEnabled(true);
        }
        monsters = db.monsterDao().getAll();
        MonsterAdapter adapter = new MonsterAdapter(monsters);
        monsterRecycler.setAdapter(adapter);
        monsterRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        isOwned.setOnCheckedChangeListener(new CheckChangeListener(adapter));

        modifiedMonsters.setOnCheckedChangeListener(new CheckChangeListener(adapter));

        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(filter.getVisibility() == View.VISIBLE) {
                    filter.setVisibility(View.GONE);
                } else {
                    filter.setVisibility(View.VISIBLE);
                }

            }
        });

        ev_monster_view_search.addTextChangedListener(new DynamicSearch(adapter));
        db.close();
        return view;
    }

    public List<Monster> searchByText(String s) {
        List<Monster> temp = new ArrayList<>();

        for(Monster monster : db.monsterDao().getAll()) {
            if(monster.name.toLowerCase().contains(s.toLowerCase())) {
                temp.add(monster);
            }
        }

        return temp;
    }

    public List<Monster> searchByText(String s, List<Monster> monsters) {
        List<Monster> temp = new ArrayList<>();

        for(Monster monster : monsters) {
            if(monster.name.toLowerCase().contains(s.toLowerCase())) {
                if(modifiedMonsters.isChecked() && !monster.checkDefault()){
                    temp.add(monster);
                } else {
                    temp.add(monster);
                }

            }
        }

        return temp;
    }

    public List<Monster> getOwned(List<Monster> all) {
        List<AccountMonster> owned = db.accountDao().getAll().get(0).monsters;
        List<Monster> temp = new ArrayList<>();
        for(int i = 0; i < owned.size(); i++) {
            for(int j = 0; j < all.size(); j++){
                if(owned.get(i).getMaster_id() == all.get(j).unit_master_id){
                    if(!temp.contains(all.get(j))){
                        temp.add(all.get(j));
                    }
                }else if(all.get(j).second_awaken != null) {
                    if(owned.get(i).getMaster_id() == all.get(j).second_awaken){
                        if(!temp.contains(all.get(j))){
                            temp.add(all.get(j));
                        }
                    }
                }
            }
        }

        return temp;
    }

    public List<Monster> getModified(List<Monster> all) {
        List<Monster> temp = new ArrayList<>();

        for(Monster monster : all){
            if(!monster.checkDefault()){
                temp.add(monster);
            }
        }

        return temp;
    }


    public List<Monster> sort(List<Monster> monsters){
        Collections.sort(monsters, new Comparator<Monster>() {
            @Override
            public int compare(Monster monster, Monster t1) {
                return monster.unit_master_id - t1.unit_master_id;
            }
        });

        return monsters;
    }

    public class DynamicSearch implements TextWatcher {

        MonsterAdapter adapter;

        DynamicSearch(MonsterAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            mHandler.removeCallbacksAndMessages(null);

            mHandler.postDelayed(new Runnable() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void run() {
                    List<Monster> all = searchByText(editable.toString());
                    List<Monster> temp = new ArrayList<>();
                    if(isOwned.isChecked() && modifiedMonsters.isChecked()){
                        temp = getOwned(all);
                    }else if(modifiedMonsters.isChecked()) {
                        for(Monster monster : all) {
                            if(!monster.checkDefault()){
                                if(!temp.contains(monster)){
                                    temp.add(monster);
                                }
                            }
                        }

                        monsters.clear();
                        monsters.addAll(sort(temp));
                        adapter.notifyDataSetChanged();
                    }else if(isOwned.isChecked()) {
                        temp = getOwned(all);

                        monsters.clear();
                        monsters.addAll(sort(temp));
                        adapter.notifyDataSetChanged();
                    }else {
                        monsters.clear();
                        monsters.addAll(searchByText(editable.toString()));
                        adapter.notifyDataSetChanged();
                    }
                }
            }, 400);
        }
    }

    public class CheckChangeListener implements CompoundButton.OnCheckedChangeListener {

        MonsterAdapter adapter;

        public CheckChangeListener(MonsterAdapter adapter) {
            this.adapter = adapter;
        }
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            List<Monster> all;
            List<AccountMonster> owned = db.accountDao().getAll().get(0).monsters;
            List<Monster> temp = new ArrayList<>();
            if(isOwned.isChecked() && modifiedMonsters.isChecked()){
                if(ev_monster_view_search.getText().length() > 0) {
                    all = searchByText(ev_monster_view_search.getText().toString());
                }else {
                    all = db.monsterDao().getAll();
                }

                temp = getOwned(all);
                temp = getModified(temp);
                monsters.clear();
                monsters.addAll(sort(temp));
            }else if(modifiedMonsters.isChecked()) {
                if(ev_monster_view_search.getText().length() > 0) {
                    all = searchByText(ev_monster_view_search.getText().toString());
                }else {
                    all = db.monsterDao().getAll();
                }

                temp = getModified(all);
                monsters.clear();
                monsters.addAll(sort(temp));
            }else if(isOwned.isChecked()) {
                if(ev_monster_view_search.getText().length() > 0) {
                    all = searchByText(ev_monster_view_search.getText().toString());
                }else {
                    all = db.monsterDao().getAll();
                }

                temp = getOwned(all);
                monsters.clear();
                monsters.addAll(sort(temp));
            }else {
                if(ev_monster_view_search.getText().length() > 0) {
                    all = searchByText(ev_monster_view_search.getText().toString());
                }else {
                    all = db.monsterDao().getAll();
                }
                monsters.clear();
                monsters.addAll(all);
            }

            adapter.notifyDataSetChanged();
        }
    }
}