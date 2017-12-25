package com.axlecho.jtsviewer;

import android.content.Context;

import com.axlecho.jtsviewer.module.JtsTabDetailModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.network.JtsServer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by axlecho on 17-12-20.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)


public class NetworkUnitTest {
    private Context context;
    private JtsServer server;

    @Before
    public void setup() {
        ShadowLog.stream = System.out;
        context = RuntimeEnvironment.application.getApplicationContext();
        server = JtsServer.getSingleton(context);
    }

    @Test
    public void testNewTab() {
        server.getNewTab(1).subscribe(new Consumer<List<JtsTabInfoModel>>() {
            @Override
            public void accept(List<JtsTabInfoModel> list) throws Exception {
                System.out.println(list);
                Assert.assertEquals(50, list.size());
            }
        }, errorHandler);
    }

    @Test
    public void testDetail() {
        server.getDetail(1317048).subscribe(new Consumer<JtsTabDetailModule>() {
            @Override
            public void accept(JtsTabDetailModule jtsTabDetailModule) throws Exception {
                System.out.println(jtsTabDetailModule);
                Assert.assertEquals(3, jtsTabDetailModule.threadList.size());
            }
        }, errorHandler);
    }

    @Test
    public void testDaily() {
        server.getHotTab(1).subscribe(new Consumer<List<JtsTabInfoModel>>() {
            @Override
            public void accept(List<JtsTabInfoModel> list) throws Exception {
                System.out.println(list);
                Assert.assertEquals(50, list.size());
            }
        }, errorHandler);
    }

    @Test
    public void testArtist() {
        server.getArtist(19301).subscribe(new Consumer<List<JtsTabInfoModel>>() {
            @Override
            public void accept(List<JtsTabInfoModel> list) throws Exception {
                System.out.println(list);
                // Assert.assertEquals(50, list.size());
            }
        }, errorHandler);
    }

    @Test
    public void testSearch() {
        server.search("love", 1).subscribe(new Consumer<List<JtsTabInfoModel>>() {
            @Override
            public void accept(List<JtsTabInfoModel> jtsTabInfoModels) throws Exception {
                System.out.println(jtsTabInfoModels);
                System.out.println(server.getSearchKey());
            }
        }, errorHandler);
    }

    @Test
    public void testSearchEx() {
        server.search("love", 1).subscribe(new Consumer<List<JtsTabInfoModel>>() {
            @Override
            public void accept(List<JtsTabInfoModel> jtsTabInfoModels) throws Exception {
                searchMore(2);
                System.out.println(server.getSearchKey());
            }
        }, errorHandler);
    }

    public void searchMore(int page) {
        server.search("love", page).subscribe(new Consumer<List<JtsTabInfoModel>>() {
            @Override
            public void accept(List<JtsTabInfoModel> jtsTabInfoModels) throws Exception {
                System.out.println(jtsTabInfoModels);
            }
        }, errorHandler);
    }

    private Consumer<Throwable> errorHandler = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            // System.out.println(throwable.getMessage());
            throwable.printStackTrace();
            // throw new Exception(throwable);
        }
    };
}
