package com.axlecho.jtsviewer.activity.detail;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.tab.JtsGtpTabAction;
import com.axlecho.jtsviewer.action.tab.JtsImgTabAction;
import com.axlecho.jtsviewer.module.JtsCollectionInfoModel;
import com.axlecho.jtsviewer.module.JtsRelatedTabModule;
import com.axlecho.jtsviewer.module.JtsRelatedVideoModule;
import com.axlecho.jtsviewer.module.JtsTabDetailModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.module.JtsThreadModule;
import com.axlecho.jtsviewer.network.JtsNetworkManager;
import com.axlecho.jtsviewer.network.JtsServer;
import com.axlecho.jtsviewer.untils.JtsConf;
import com.axlecho.jtsviewer.untils.JtsImageGetter;
import com.axlecho.jtsviewer.untils.JtsTagHandler;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.axlecho.jtsviewer.untils.JtsToolUnitls;
import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.bumptech.glide.Glide;
import com.pixplicity.htmlcompat.HtmlCompat;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import static com.pixplicity.htmlcompat.HtmlCompat.FROM_HTML_MODE_LEGACY;

/**
 * Created by Administrator on 2017/11/7.
 */

public class JtsDetailActivityController {
    private static final String TAG = "detail";
    private JtsDetailActivity activity;
    private JtsTabDetailModule detail;
    private JtsTabInfoModel info;


    private List<Disposable> disposables = new ArrayList<>();

    private Consumer<Throwable> errorHandler = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            throwable.printStackTrace();
            activity.showError(JtsTextUnitls.getErrorMessageFromException(activity, throwable));
        }
    };


    public void attachToActivity(JtsDetailActivity activity) {
        this.activity = activity;
        this.bindTabInfo();
    }

    public void bindTabInfo() {
        JtsTabInfoModel model = (JtsTabInfoModel) activity.getIntent().getSerializableExtra("tabinfo");
        JtsViewerLog.d(TAG, "[bindTabInfo] " + model);

        TextDrawable defaultDrawable = TextDrawable.builder()
                .beginConfig().height(300).width(300).bold().endConfig()
                .buildRect(model.title.substring(0, 1), activity.getResources().getColor(R.color.colorPrimary));
        Glide.with(activity).load(JtsTextUnitls.getResizePicUrl(model.avatar, 300, 300)).fitCenter()
                .error(defaultDrawable).into(activity.avatar);

        activity.title.setText(model.title);
        activity.author.setText(model.author);
        activity.type.setText(model.type);
        activity.otherActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.initPopMenu();
                initPopMenuAction();
                activity.popMenu();
            }
        });


    }

    public void detachFromActivity() {
        JtsNetworkManager.getInstance(activity).cancelAll();

        for (Disposable disposable : disposables) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
        disposables.clear();
    }

    public void initPopMenuAction() {
        activity.popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_open_in_other_app:
                        openInOtherApp();
                        break;
                    case R.id.action_refresh:
                        getTabDetail();
                        break;
                }
                return true;
            }
        });
    }

    public void showCollectionDialog() {
        final ProgressDialog loadingProgressDialog;
        loadingProgressDialog = new ProgressDialog(activity);
        loadingProgressDialog.setTitle(null);
        loadingProgressDialog.setMessage(activity.getResources().getString(R.string.login_tip));
        loadingProgressDialog.show();
        Disposable disposable = JtsServer.getSingleton(activity).getCollection().subscribe(new Consumer<List<JtsCollectionInfoModel>>() {
            @Override
            public void accept(List<JtsCollectionInfoModel> jtsCollectionInfoModels) throws Exception {
                loadingProgressDialog.dismiss();
                showCollectionDialog(jtsCollectionInfoModels);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                activity.showMessage(throwable.getMessage());
            }
        });
        disposables.add(disposable);
    }

    public void showCollectionDialog(final List<JtsCollectionInfoModel> collectionInfoModels) {
        List<String> itemList = new ArrayList<>();
        for (JtsCollectionInfoModel model : collectionInfoModels) {
            itemList.add(model.title);
        }
        AlertDialog.Builder listDialog = new AlertDialog.Builder(activity);
        listDialog.setTitle(activity.getResources().getString(R.string.title_collection));
        listDialog.setItems(itemList.toArray(new String[0]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                favorite(collectionInfoModels.get(which));
            }
        });
        listDialog.show();
    }

    public void favorite(JtsCollectionInfoModel collection) {
        final ProgressDialog loadingProgressDialog;
        loadingProgressDialog = new ProgressDialog(activity);
        loadingProgressDialog.setTitle(null);
        loadingProgressDialog.setMessage(activity.getResources().getString(R.string.login_tip));
        loadingProgressDialog.show();
        final long tabKey = JtsTextUnitls.getTabKeyFromUrl(info.url);
        final long collectionId = JtsTextUnitls.getCollectionIdFromUrl(collection.url);
        Disposable disposable = JtsServer.getSingleton(activity).favorite(collectionId, tabKey, detail.formhash)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        activity.showMessage(s);
                        loadingProgressDialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        activity.showMessage(throwable.getMessage());
                        loadingProgressDialog.dismiss();
                    }
                });
        disposables.add(disposable);
    }

    public void getTabDetail() {
        this.info = (JtsTabInfoModel) activity.getIntent().getSerializableExtra("tabinfo");
        long tabKey = JtsTextUnitls.getTabKeyFromUrl(info.url);
        Disposable disposable = JtsServer.getSingleton(activity).getDetail(tabKey)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        activity.hideError();
                        activity.startLoading();
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        activity.stopLoading();
                    }
                })
                .subscribe(new Consumer<JtsTabDetailModule>() {
                    @Override
                    public void accept(JtsTabDetailModule jtsTabDetailModule) throws Exception {
                        processDetail(jtsTabDetailModule);
                    }
                }, errorHandler);
        disposables.add(disposable);
    }


    public void processDetail(JtsTabDetailModule detail) {
        this.detail = detail;
        this.bindInfo(detail);
        this.bindComments(detail.threadList);
        this.bindRelated();
        activity.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processTabPlay();
            }
        });
        activity.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCollectionDialog();
            }
        });
    }

    public void bindComments(List<JtsThreadModule> threads) {
        final int maxShowCount = 2;
        if (threads == null || threads.size() == 0) {
            activity.comment.setText(R.string.no_comments);
            return;
        } else if (threads.size() <= maxShowCount) {
            activity.comment.setText(R.string.no_more_comments);
            activity.commentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toCommentsActivity();
                }
            });
        } else {
            activity.comment.setText(R.string.more_comment);
            activity.commentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toCommentsActivity();
                }
            });
        }

        int length = Math.min(maxShowCount, threads.size());
        for (int i = 0; i < length; i++) {
            JtsThreadModule comment = threads.get(i);
            View view = activity.getLayoutInflater().inflate(R.layout.item_thread, activity.commentLayout, false);
            activity.commentLayout.addView(view, i);

            ImageView avatar = view.findViewById(R.id.thread_item_avatar);
            TextView auth = view.findViewById(R.id.thread_item_auth);
            TextView time = view.findViewById(R.id.thread_item_time);
            TextView message = view.findViewById(R.id.thread_item_message);
            TextView floor = view.findViewById(R.id.thread_item_floor);
            if (comment.avatar == null) {
                comment.avatar = activity.getResources().getString(R.string.unknown_author);
            }
            TextDrawable defaultDrawable = TextDrawable.builder()
                    .beginConfig().height(48).width(48).bold().endConfig()
                    .buildRect(comment.authi.substring(0, 1), activity.getResources().getColor(R.color.colorPrimary));
            Glide.with(activity).load(comment.avatar).error(defaultDrawable).into(avatar);
            auth.setText(comment.authi);
            time.setText(comment.time);
            floor.setText(comment.floor);
            JtsViewerLog.d("html", comment.message);
            message.setText(HtmlCompat.fromHtml(activity, comment.message, FROM_HTML_MODE_LEGACY,
                    new JtsImageGetter(message),
                    new JtsTagHandler(activity, message, comment.message)));
            // message.setMovementMethod(LinkMovementMethod.getInstance());
            // message.setClickable(false);
        }
    }


    public void bindInfo(JtsTabDetailModule detail) {
        if (detail.info != null) {
            activity.info.setText(HtmlCompat.fromHtml(activity, detail.info, FROM_HTML_MODE_LEGACY));
        } else {
            activity.info.setText(activity.getResources().getText(R.string.no_info));
        }
        if (detail.lyric != null) {
            activity.lyric.setText(HtmlCompat.fromHtml(activity, detail.lyric, FROM_HTML_MODE_LEGACY));
        } else {
            activity.lyric.setText(activity.getResources().getText(R.string.no_lyric));
        }
    }

    public void bindRelated() {
        bindRelatedTabs(detail.relatedTabs);
        bindRelatedVideos(detail.relatedVideos);
    }


    public void bindRelatedTabs(List<JtsRelatedTabModule> tabs) {
        if (tabs == null || tabs.size() == 0) {
            return;
        }

        LinearLayout root = activity.findViewById(R.id.below_header);
        View view = activity.getLayoutInflater().inflate(R.layout.tab_detail_related, root, false);
        root.addView(view);

        LinearLayout parent = view.findViewById(R.id.detail_related);
        for (final JtsRelatedTabModule tab : tabs) {
            View itemView = activity.getLayoutInflater().inflate(R.layout.widget_bookmark_item, parent, false);
            TextView title = itemView.findViewById(R.id.bookmark_item_title);
            title.setText(tab.title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JtsTabInfoModel model = new JtsTabInfoModel();
                    model.title = tab.title;
                    model.url = tab.url;
                    model.author = activity.getResources().getString(R.string.unknown_author);
                    model.time = activity.getResources().getString(R.string.unknown_time);
                    model.reply = "";
                    model.watch = "";
                    model.type = activity.getResources().getString(R.string.unlogin_tip);
                    model.uper = activity.getResources().getString(R.string.unknown_uper);
                    model.avatar = "";
                    Intent intent = new Intent();
                    intent.putExtra("tabinfo", model);
                    intent.setClass(activity, JtsDetailActivity.class);
                    activity.startActivity(intent);
                }
            });
            parent.addView(itemView);
        }

    }

    public void bindRelatedVideos(List<JtsRelatedVideoModule> videos) {
        if(videos == null || videos.size() == 0) {
            return;
        }

        LinearLayout root = activity.findViewById(R.id.below_header);
        View view = activity.getLayoutInflater().inflate(R.layout.tab_detail_related, root, false);
        root.addView(view);

        LinearLayout parent = view.findViewById(R.id.detail_related);
        for (final JtsRelatedVideoModule video : videos) {
            View itemView = activity.getLayoutInflater().inflate(R.layout.widget_bookmark_item, parent, false);
            TextView title = itemView.findViewById(R.id.bookmark_item_title);
            title.setText(video.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JtsToolUnitls.openUrl(activity,JtsConf.HOST_URL + video.url);
                }
            });

            parent.addView(itemView);
        }

    }

    public void toCommentsActivity() {
        Intent intent = new Intent(activity, JtsCommentsActivity.class);
        intent.putExtra("info", info);
        intent.putExtra("detail", detail);
        activity.startActivity(intent);


    }


    public void processTabPlay() {

        JtsBaseAction action;
        long gid = JtsTextUnitls.getTabKeyFromUrl(info.url);
        if (detail.gtpUrl != null) {
            action = new JtsGtpTabAction(activity, gid, detail.gtpUrl, info);
        } else if (detail.imgUrls != null && detail.imgUrls.size() != 0) {
            action = new JtsImgTabAction(activity, gid, detail.imgUrls);
        } else {
            action = new JtsBaseAction() {
                @Override
                public void processAction() {
                    activity.showMessage(R.string.error_comment_null);
                }
            };
        }
        action.execute();
    }


    private void openInOtherApp() {
        JtsToolUnitls.openUrl(activity, JtsConf.HOST_URL + info.url);
    }


}
