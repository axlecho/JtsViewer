package com.axlecho.jtsviewer.activity.base;

import android.graphics.Canvas;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class JtsDragAndDrapCallBack extends ItemTouchHelper.SimpleCallback {
    private RecyclerView.Adapter<?> adapter;
    private List data;
    private JtsBaseRecycleViewAdapter.OnDataEditListener listener;

    public JtsDragAndDrapCallBack(RecyclerView.Adapter<?> adapter, List list, JtsBaseRecycleViewAdapter.OnDataEditListener listener) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
                ItemTouchHelper.START | ItemTouchHelper.END);
        this.adapter = adapter;
        this.data = list;
        this.listener = listener;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int from = viewHolder.getAdapterPosition();
        int to = target.getAdapterPosition();

        if (from < to) {
            for (int i = from; i <= to - 1; i++) {
                Collections.swap(data, i, i + 1);
            }
        } else {
            for (int i = from; i >= to + 1; i--) {
                Collections.swap(data, i, i - 1);
            }
        }
        adapter.notifyItemMoved(from, to);
        if (listener != null) {
            listener.onItemMoved(data.get(to), from, to);
        }
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();
        Object model = data.remove(pos);
        adapter.notifyItemRemoved(pos);
        if (listener != null) {
            listener.onItemSwiped(model,pos);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            float alpha = 1 - Math.abs(dX) / viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        }
    }
}
