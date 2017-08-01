package com.axlecho.jtsviewer.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.module.CacheModule;

import java.util.List;

public class CacheViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<CacheModule> modules;

    public CacheViewAdapter(Context context, List<CacheModule> modules) {
        this.context = context;
        this.modules = modules;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cache, parent, false);
        return new CacheViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CacheViewHolder) holder).contentView.setText(modules.get(position).fileName);
    }

    @Override
    public int getItemCount() {
        return modules.size();
    }


    private class CacheViewHolder extends RecyclerView.ViewHolder {
        public TextView contentView;

        public CacheViewHolder(View view) {
            super(view);
            this.contentView = (TextView) view.findViewById(R.id.cache_item_content);
        }
    }
}
