package com.axlecho.jtsviewer.activity.main;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.tab.JtsGetTabAction;
import com.axlecho.jtsviewer.action.tab.JtsParseTabAction;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.untils.JtsViewerLog;

import java.util.List;

public class JtsTabListAdapter extends RecyclerView.Adapter<JtsTabListAdapter.TabViewHolder> {

    private static final String TAG = JtsTabListAdapter.class.getSimpleName();
    private List<JtsTabInfoModel> content;
    private Context context;

    public JtsTabListAdapter(Context context, List<JtsTabInfoModel> content) {
        this.context = context;
        this.content = content;
    }

    @Override
    public TabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_tab, parent, false);
        return new TabViewHolder(view);
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


    class TabViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

            View cardView = view.findViewById(R.id.tab_item_cardview);
            cardView.setOnClickListener(this);
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

        @Override
        public void onClick(View v) {


            JtsTabInfoModel model = content.get(getAdapterPosition());
            JtsViewerLog.d(TAG, model.url);
            JtsGetTabAction action = new JtsGetTabAction();
            action.setKey(JtsBaseAction.CONTEXT_KEY, context);
            action.setKey(JtsGetTabAction.URL_KEY, model.url);
            action.execute();
        }
    }
}
