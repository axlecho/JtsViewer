package com.axlecho.jtsviewer.activity.main;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.squareup.picasso.Picasso;

import java.util.List;

public class JtsTabListAdapter extends RecyclerView.Adapter<JtsTabListAdapter.TabViewHolder> {

    private static final String TAG = JtsTabListAdapter.class.getSimpleName();
    private List<JtsTabInfoModel> content;
    private Context context;

    public JtsTabListAdapter(Context context, List<JtsTabInfoModel> content) {
        this.context = context;
        this.content = content;
    }

    public void addData(List<JtsTabInfoModel> content) {
        this.content.addAll(content);
    }

    public void clearData() {
        this.content.clear();
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

    public JtsTabInfoModel findTabInfoByGid(long gid) {
        for (JtsTabInfoModel model : content) {
            long modelGid = JtsTextUnitls.getTabKeyFromUrl(model.url);
            if (gid == modelGid) {
                return model;
            }
        }

        return null;
    }


    class TabViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView author;
        private TextView time;
        private TextView reply;
        private TextView watch;
        private ImageView type;
        private TextView uper;
        private ImageView avatar;

        public TabViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tab_item_title);
            author = (TextView) view.findViewById(R.id.tab_item_author);
            time = (TextView) view.findViewById(R.id.tab_item_time);
            reply = (TextView) view.findViewById(R.id.tab_item_reply);
            watch = (TextView) view.findViewById(R.id.tab_item_watch);
            type = (ImageView) view.findViewById(R.id.tab_item_type);
            uper = (TextView) view.findViewById(R.id.tab_item_uper);
            avatar = (ImageView) view.findViewById(R.id.tab_item_avatar);

            View cardView = view.findViewById(R.id.tab_item_view);
            cardView.setOnClickListener(this);
        }

        public void setData(JtsTabInfoModel model) {
            title.setText(model.title);
            author.setText(model.author);
            time.setText(model.time);
            reply.setText(context.getResources().getString(R.string.reply) + ":" + model.reply);
            watch.setText(context.getResources().getString(R.string.watch) + ":" + model.watch);
            uper.setText(model.uper);
            this.setType(model.type);

            Picasso.with(context).load(model.avatar).error(R.drawable.ic_launcher).into(avatar);
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

        @Override
        public void onClick(View v) {
            JtsTabInfoModel model = content.get(getAdapterPosition());
            JtsViewerLog.d(TAG, model.url);
            MainActivityController.getInstance().startDetailActivity(model,avatar);
        }
    }
}
