package com.axlecho.jtsviewer.activity.main;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;

import java.util.List;

public class JtsTabListAdapter extends RecyclerView.Adapter<JtsTabListAdapter.TabViewHolder> {

    private List<JtsTabInfoModel> content;
    private Context context;
    private JtsTabListAdapter adapter;

    public JtsTabListAdapter(Context context, List<JtsTabInfoModel> content) {
        this.context = context;
        this.content = content;
    }

    @Override
    public TabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_tab, parent, false);
        TabViewHolder holder = new TabViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(TabViewHolder holder, int position) {
        JtsTabInfoModel model = content.get(position);
        holder.setData(model);
    }

    @Override
    public int getItemCount() {
        if (content == null) {
            return 0;
        }

        return content.size();
    }

    class TabViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView author;
        private TextView time;
        private TextView reply;
        private TextView watch;
        private TextView type;
        private TextView uper;
        private TextView avatar;

        public TabViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tab_item_title);
            author = (TextView) view.findViewById(R.id.tab_item_author);
            time = (TextView) view.findViewById(R.id.tab_item_time);
            reply = (TextView) view.findViewById(R.id.tab_item_reply);
            watch = (TextView) view.findViewById(R.id.tab_item_watch);
            type = (TextView) view.findViewById(R.id.tab_item_type);
            uper = (TextView) view.findViewById(R.id.tab_item_uper);
            avatar = (TextView) view.findViewById(R.id.tab_item_avatar);
        }

        public void setData(JtsTabInfoModel model) {
            title.setText(model.title);
            author.setText(model.author);
            time.setText(model.time);
            reply.setText(model.reply);
            watch.setText(model.watch);
            type.setText(model.type);
            uper.setText(model.uper);
            avatar.setText(model.avatar);
        }
    }
}
