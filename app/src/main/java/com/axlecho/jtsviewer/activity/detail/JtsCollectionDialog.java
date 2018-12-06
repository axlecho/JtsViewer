package com.axlecho.jtsviewer.activity.detail;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class JtsCollectionDialog extends Dialog {
    public JtsCollectionDialog(@NonNull Context context) {
        super(context);
    }

    public JtsCollectionDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected JtsCollectionDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


}
