package com.axlecho.jtsviewer.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.module.JtsThreadCommentModule;
import com.axlecho.jtsviewer.module.JtsThreadModule;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */

public class JtsThreadListAdapter extends RecyclerView.Adapter<JtsThreadListAdapter.ThreadViewHolder> {

    private List<JtsThreadModule> threads;
    private Context context;

    public JtsThreadListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ThreadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_thread, parent, false);
        return new JtsThreadListAdapter.ThreadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ThreadViewHolder holder, int position) {
        holder.bindData(threads.get(position));
    }

    @Override
    public int getItemCount() {
        return threads.size();
    }

    public void addData(List<JtsThreadModule> threads) {
        this.threads = threads;
    }

    public class ThreadViewHolder extends ViewHolder {
        public ImageView avatar;
        public TextView auth;
        public TextView time;
        public TextView message;
        public TextView floor;
        public LinearLayout comments;

        public ThreadViewHolder(View itemView) {
            super(itemView);

            avatar = (ImageView) itemView.findViewById(R.id.thread_item_avatar);
            auth = (TextView) itemView.findViewById(R.id.thread_item_auth);
            time = (TextView) itemView.findViewById(R.id.thread_item_time);
            message = (TextView) itemView.findViewById(R.id.thread_item_message);
            floor = (TextView) itemView.findViewById(R.id.thread_item_floor);
            comments = (LinearLayout) itemView.findViewById(R.id.thread_item_comment_layout);
        }

        public void bindData(JtsThreadModule model) {
            Picasso.with(context).load(model.avatar).into(avatar);
            auth.setText(model.authi);
            time.setText(model.time);
            message.setText(model.message);
            floor.setText(model.floor);
            if (model.comments.size() > 0) {
                comments.removeAllViews();
                for (JtsThreadCommentModule commentModel : model.comments) {
                    bindComment(comments, commentModel);
                }
            } else {
                comments.setVisibility(View.GONE);
            }
        }

        public void bindComment(LinearLayout layout, JtsThreadCommentModule model) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_thread, layout, false);
            layout.addView(view);
            ImageView avatar = (ImageView) view.findViewById(R.id.thread_item_avatar);
            TextView auth = (TextView) view.findViewById(R.id.thread_item_auth);
            TextView time = (TextView) view.findViewById(R.id.thread_item_time);
            TextView message = (TextView) view.findViewById(R.id.thread_item_message);
            view.findViewById(R.id.thread_item_floor).setVisibility(View.GONE);

            Picasso.with(context).load(model.avatar).into(avatar);
            auth.setText(model.authi);
            time.setText(model.time);
            message.setText(model.message);
        }
    }
}
