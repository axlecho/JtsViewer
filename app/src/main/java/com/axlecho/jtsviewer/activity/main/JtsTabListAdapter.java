package com.axlecho.jtsviewer.activity.main;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.axlecho.jtsviewer.activity.base.JtsBaseRecycleViewAdapter;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class JtsTabListAdapter extends JtsBaseRecycleViewAdapter<JtsTabInfoModel> {

    private static final String TAG = JtsTabListAdapter.class.getSimpleName();


    public JtsTabListAdapter(Context context) {
        super(context, null);
        this.context = context;
    }

    @Override
    @NonNull
    public TabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tab, parent, false);
        return new TabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!(holder instanceof TabViewHolder)) {
            JtsViewerLog.w(TAG, "holder is not a valid holder");
            return;
        }
        final TabViewHolder viewHolder = (TabViewHolder) holder;
        JtsTabInfoModel model = data.get(position);
        viewHolder.setData(model);
    }

    class TabViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView author;
        private TextView time;
        private TextView reply;
        private TextView watch;
        private ImageView type;
        private TextView uper;
        private ImageView avatar;
        private View cardView;

        public TabViewHolder(View view) {
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

        public void setData(JtsTabInfoModel model) {
            title.setText(model.title);
            author.setText(model.author);
            time.setText(model.time);
            reply.setText(context.getResources().getString(R.string.reply) + ":" + model.reply);
            watch.setText(context.getResources().getString(R.string.watch) + ":" + model.watch);
            uper.setText(model.uper);
            this.setType(model.type);

            TextDrawable defaultDrawable = TextDrawable.builder()
                    .beginConfig().height(48).width(48).bold().endConfig()
                    .buildRect(model.title.substring(0, 1), context.getResources().getColor(R.color.colorPrimary));
            Glide.with(context).load(model.avatar).error(defaultDrawable).into(avatar);

            cardView.setOnClickListener(new BaseItemClickListener(model, avatar));
            cardView.setOnLongClickListener(new BaseItemLongClickListener(model));
        }

        private void setType(String tabType) {
            if (tabType.equals("GTP谱")) {
                type.setImageResource(R.mipmap.ic_gtp);
            } else if (tabType.equals("和弦谱")) {
                type.setImageResource(R.mipmap.ic_chord);
            } else if (tabType.equals("图片谱")) {
                type.setImageResource(R.mipmap.ic_img);
            } else if (tabType.equals("PDF谱")) {
                type.setImageResource(R.mipmap.ic_pdf);
            }
        }
    }
}
