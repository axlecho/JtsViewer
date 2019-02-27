package com.axlecho.jtsviewer.activity.my;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.module.JtsCollectionInfoModel;
import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.axlecho.jtsviewer.activity.base.JtsBaseRecycleViewAdapter;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class JtsCollectionListAdapter extends JtsBaseRecycleViewAdapter<JtsCollectionInfoModel> {
    private static final String TAG = JtsCollectionListAdapter.class.getSimpleName();

    public JtsCollectionListAdapter(Context context, List<JtsCollectionInfoModel> modules) {
        super(context, modules);
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tab, parent, false);
        return new JtsCollectionListAdapter.JtsCollectionListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!(holder instanceof JtsCollectionListAdapter.JtsCollectionListViewHolder)) {
            JtsViewerLog.w(TAG, "holder is not a valid holder");
            return;
        }

        final JtsCollectionListAdapter.JtsCollectionListViewHolder viewHolder = (JtsCollectionListAdapter.JtsCollectionListViewHolder) holder;
        final JtsCollectionInfoModel module = data.get(position);
        viewHolder.setData(module);
        viewHolder.cardView.setOnClickListener(new BaseItemClickListener(module, viewHolder.avatar));
        viewHolder.cardView.setOnLongClickListener(new BaseItemLongClickListener(module));
    }

    private class JtsCollectionListViewHolder extends RecyclerView.ViewHolder {
        public View cardView;
        private TextView title;
        private TextView author;
        private TextView time;
        private TextView reply;
        private TextView watch;
        private ImageView type;
        private TextView uper;
        private ImageView avatar;

        public JtsCollectionListViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tab_item_title);
            author = view.findViewById(R.id.tab_item_author);
            time = view.findViewById(R.id.tab_item_time);
            reply = view.findViewById(R.id.tab_item_reply);
            watch = view.findViewById(R.id.tab_item_watch);
            type = view.findViewById(R.id.tab_item_type);
            uper = view.findViewById(R.id.tab_item_uper);
            avatar = view.findViewById(R.id.tab_item_avatar);
            cardView = view.findViewById(R.id.tab_item_view);
        }

        public void setData(JtsCollectionInfoModel model) {
            this.title.setText(model.title);
            this.time.setText(model.time);
            this.reply.setText(model.comments);
            this.watch.setText(model.subscribe);
            this.uper.setText(model.uper);
            TextDrawable defaultDrawable = TextDrawable.builder()
                    .beginConfig().height(48).width(48).bold().endConfig()
                    .buildRect(model.title.substring(0, 1), context.getResources().getColor(R.color.colorPrimary));
            Glide.with(context).load(model.avatar).error(defaultDrawable).into(avatar);
        }
    }
}