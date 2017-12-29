package com.axlecho.jtsviewer;

import android.content.Context;

import com.axlecho.jtsviewer.module.JtsTabDetailModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.network.JtsSchedulers;
import com.axlecho.jtsviewer.network.JtsSearchHelper;
import com.axlecho.jtsviewer.network.JtsServer;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.List;

import io.reactivex.Observable;

import static org.hamcrest.core.Is.is;

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
        server.setSchedulers(new MockSchedulers());
    }

    @Test
    public void testNewTab() {
        List<JtsTabInfoModel> result = server.getNewTab(1).blockingFirst();
        MatcherAssert.assertThat(result.size(), is(50));
    }

    @Test
    public void testDetail() {
        JtsTabDetailModule result = server.getDetail(1317048).blockingFirst();
        MatcherAssert.assertThat(result.threadList.size(), is(10));
    }

    @Test
    public void testDaily() {
        List<JtsTabInfoModel> result = server.getHotTab(1).blockingFirst();
        MatcherAssert.assertThat(result.size(), is(50));
    }

    @Test
    public void testArtist() {
        List<JtsTabInfoModel> result = server.getArtist(19301).blockingFirst();
        MatcherAssert.assertThat(result.size(), is(20));
    }

    @Test
    public void testSearch() {
        List<JtsTabInfoModel> result = server.search("love", 1).blockingFirst();
        MatcherAssert.assertThat(result.size(), is(20));
    }

    @Test
    public void testSearchEx() {
        server.search("love", 1).blockingSubscribe();
        List<JtsTabInfoModel> result = server.search("love", 2).blockingFirst();
        MatcherAssert.assertThat(result.size(), is(20));
        JtsSearchHelper.getSingleton().dump();
    }


    @Test
    public void testLogin() throws Exception {
        server.login("6b3db232", "http://www.jitashe.org/", "d39", "123456789", 2592000)
                .blockingSubscribe();
        server.getDetail(24285).blockingSubscribe();
    }

    @Test
    public void testPostCommentWithLogin() {
        server.login("6b3db232", "http://www.jitashe.org/", "d39", "123456789", 2592000).blockingSubscribe();
        JtsTabDetailModule detail = server.getDetail(24285).blockingFirst();
        String result = server.postComment(detail.fid, 24285, "66666666666666666666", detail.formhash).blockingFirst();
        MatcherAssert.assertThat(result.contains("没有权限"), is(false));
    }

    private class MockSchedulers extends JtsSchedulers {
        @Override
        public <T> Observable<T> switchSchedulers(Observable<T> a) {
            return a;
        }
    }
}
