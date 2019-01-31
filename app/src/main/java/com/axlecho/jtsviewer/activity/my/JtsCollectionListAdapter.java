package com.axlecho.jtsviewer.activity.my;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.module.JtsCollectionInfoModel;
import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class JtsCollectionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = JtsCollectionListAdapter.class.getSimpleName();
    private List<JtsCollectionInfoModel> modules;
    private Context context;
    private List<JtsCollectionListAdapter.OnItemClickListener> clickListenerList;
    private List<JtsCollectionListAdapter.OnItemLongClickListener> longClickListenerList;

    public JtsCollectionListAdapter(Context context, List<JtsCollectionInfoModel> modules) {
        this.context = context;
        this.modules = modules;
        clickListenerList = new ArrayList<>();
        longClickListenerList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tab, parent, false);
        return new JtsCollectionListAdapter.JtsCollectionListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!(holder instanceof JtsCollectionListAdapter.JtsCollectionListViewHolder)) {
            JtsViewerLog.w(TAG, "holder is not a valid holder");
            return;
        }

        final JtsCollectionListAdapter.JtsCollectionListViewHolder viewHolder = (JtsCollectionListAdapter.JtsCollectionListViewHolder) holder;
        final JtsCollectionInfoModel module = modules.get(position);
        viewHolder.setData(module);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (JtsCollectionListAdapter.OnItemClickListener listener : clickListenerList) {
                    listener.onItemClick(module);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return modules.size();
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
            title = (TextView) view.findViewById(R.id.tab_item_title);
            author = (TextView) view.findViewById(R.id.tab_item_author);
            time = (TextView) view.findViewById(R.id.tab_item_time);
            reply = (TextView) view.findViewById(R.id.tab_item_reply);
            watch = (TextView) view.findViewById(R.id.tab_item_watch);
            type = (ImageView) view.findViewById(R.id.tab_item_type);
            uper = (TextView) view.findViewById(R.id.tab_item_uper);
            avatar = (ImageView) view.findViewById(R.id.tab_item_avatar);
            cardView = view.findViewById(R.id.tab_item_view);
        }


        public void setData(JtsCollectionInfoModel model) {
            this.title.setText(model.title);
            this.time.setText(model.time);
            this.reply.setText(model.comments);
            this.watch.setText(model.subscribe);
            this.author.setText(model.uper);
            TextDrawable defaultDrawable = TextDrawable.builder()
                    .beginConfig().height(48).width(48).bold().endConfig()
                    .buildRect(model.title.substring(0, 1), context.getResources().getColor(R.color.colorPrimary));
            Glide.with(context).load(model.avatar).error(defaultDrawable).into(avatar);        }
    }

    public void addOnItemLongClickListener(JtsCollectionListAdapter.OnItemLongClickListener listener) {
        this.longClickListenerList.add(listener);
    }

    public void addOnItemClickListener(JtsCollectionListAdapter.OnItemClickListener listener) {
        this.clickListenerList.add(listener);
    }

    public interface OnItemClickListener {
        void onItemClick(JtsCollectionInfoModel module);

        void onItemAvatarClick(JtsCollectionInfoModel module, View shareView);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(JtsCollectionInfoModel module);
    }
}