package com.axlecho.jtsviewer.activity.main;

import android.content.Context;

import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.network.JtsServer;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class JtsArtistScene extends BaseScene {


    private static final String TAG = "artist-scene";
    private int artistId;

    public JtsArtistScene(Context context, int artistId, String title) {
        super(context, title);
        this.artistId = artistId;
    }

    @Override
    public String getName() {
        return TAG;
    }

    @Override
    public void loadMore() {
        super.loadMore();
        JtsServer.getSingleton(context).getArtist(artistId, currentPage)
                .subscribe(new Consumer<List<JtsTabInfoModel>>() {
                    @Override
                    public void accept(List<JtsTabInfoModel> jtsTabInfoModels) throws Exception {
                        processLoadMore(jtsTabInfoModels);
                    }
                }, controller.getErrorHandler());
    }

    @Override
    public void refresh() {
        super.refresh();
        JtsServer.getSingleton(context).getArtist(artistId, currentPage).subscribe(new Consumer<List<JtsTabInfoModel>>() {
            @Override
            public void accept(List<JtsTabInfoModel> jtsTabInfoModels) throws Exception {
                processRefreah(jtsTabInfoModels);
            }
        }, controller.getErrorHandler());

    }
}
