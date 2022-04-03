package com.evans.swinspector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RuneHistoryAdapter  extends RecyclerView.Adapter<RuneHistoryAdapter.ViewHolder>{
    SettingsHandler settingsHandler;
    private List<Rune> mRunes = new ArrayList<>();

    public RuneHistoryAdapter(List<Rune> runes) {
        mRunes = runes;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView runeDetails;
        public ViewHolder(View itemView) {
            super(itemView);

            this.runeDetails = (TextView) itemView.findViewById(R.id.rune_list_text);
        }

        public TextView getRuneDetails() {
            return this.runeDetails;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        settingsHandler = new SettingsHandler(parent.getContext().getSharedPreferences(parent.getContext().getString(R.string.preference_file_key), Context.MODE_PRIVATE));
        View detailView = inflater.inflate(R.layout.history_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(detailView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.getRuneDetails().setText(mRunes.get(position).toString() + "\n" + String.format("%.2f%%/%.2f%%", mRunes.get(position).getEfficiency(settingsHandler.reduceFlats(), settingsHandler.getFlatWeight()).get(0), mRunes.get(position).getEfficiency(settingsHandler.reduceFlats(), settingsHandler.getFlatWeight()).get(1)) + "\n");
    }

    @Override
    public int getItemCount() {
        return mRunes.size();
    }
}
