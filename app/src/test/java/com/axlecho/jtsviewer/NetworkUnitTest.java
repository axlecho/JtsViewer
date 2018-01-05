package com.axlecho.jtsviewer;

import android.content.Context;
import android.text.TextUtils;

import com.axlecho.jtsviewer.module.JtsTabDetailModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.module.JtsThreadModule;
import com.axlecho.jtsviewer.module.JtsUserModule;
import com.axlecho.jtsviewer.module.JtsVersionInfoModule;
import com.axlecho.jtsviewer.network.JtsSchedulers;
import com.axlecho.jtsviewer.network.JtsSearchHelper;
import com.axlecho.jtsviewer.network.JtsServer;
import com.axlecho.jtsviewer.untils.JtsConf;

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
import io.reactivex.functions.Consumer;

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
    public void testLogin() throws Exception {
        String cookies = server.login("d39", "123456789").blockingFirst();
        MatcherAssert.assertThat("login cookie should not be null", !TextUtils.isEmpty(cookies));
    }

    @Test
    public void testGetUserInfo() {
        server.login("d39", "123456789").blockingSubscribe();
        JtsUserModule user = server.getUserInfo().blockingFirst();
        System.out.println(user);
        MatcherAssert.assertThat("check uid failed", user.uid, is(556355L));
        MatcherAssert.assertThat("check username failed", user.userName, is("d39"));
        MatcherAssert.assertThat("avatar is null", !TextUtils.isEmpty(user.avatarUrl));
    }

    // @Test
    public void testGetUserInfoWithoutLogin() {
        JtsUserModule user = server.getUserInfo().blockingFirst();
        System.out.println(user);
        MatcherAssert.assertThat("check uid failed", user.uid, is(0L));
        MatcherAssert.assertThat("check username failed", TextUtils.isEmpty(user.userName));
    }



    @Test
    public void testNewTab() {
        List<JtsTabInfoModel> result = server.getNewTab(1).blockingFirst();
        MatcherAssert.assertThat(result.size(), is(50));
    }

    @Test
    public void testTread() {
        List<JtsThreadModule> result = server.getThread(9440, 2).blockingFirst();
        MatcherAssert.assertThat(result.size(), is(10));
        System.out.println(result);
    }

    @Test
    public void testDaily() {
        List<JtsTabInfoModel> result = server.getHotTab(1).blockingFirst();
        MatcherAssert.assertThat(result.size(), is(50));
    }

    @Test
    public void testArtist() {
        List<JtsTabInfoModel> result = server.getArtist(19301, 1).blockingFirst();
        MatcherAssert.assertThat(result.size(), is(20));

        result = server.getArtist(19301, 2).blockingFirst();
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
    public void testDetail() {
        JtsTabDetailModule result = server.getDetail(9440).blockingFirst();
        MatcherAssert.assertThat(result.threadList.size(), is(10));
        System.out.println(result);

        result = server.getDetail(172292).blockingFirst();
        MatcherAssert.assertThat(result.threadList.size(), is(10));
        System.out.println(result);

        result = server.getDetail(1319358).blockingFirst();
        MatcherAssert.assertThat(result.threadList.size(), is(2));
        System.out.println(result);
    }

    // @Test
    public void testPostComment() {
        JtsTabDetailModule detail = server.getDetail(24285L).blockingFirst();
        String result = server.postComment(detail.fid, 24285, "谢谢楼主分享", detail.formhash).blockingFirst();
        MatcherAssert.assertThat("comment without login", result.equals(JtsConf.STATUS_FAILED));

        server.login("d39", "123456789").blockingSubscribe();
        detail = server.getDetail(24285L).blockingFirst();
        result = server.postComment(detail.fid, 24285, "谢谢楼主分享", detail.formhash).blockingFirst();
        MatcherAssert.assertThat("comment with login", result.equals(JtsConf.STATUS_SUCCESSED));
    }

    // @Test
    // failed with CertificateException: Certificates does not conform to algorithm constraints
    public void testGetNewVersionInfo() {
        JtsVersionInfoModule versionInfo = server.getLastVersionInfo().blockingFirst();
        System.out.println(versionInfo);
    }



    private class MockSchedulers extends JtsSchedulers {
        @Override
        public <T> Observable<T> switchSchedulers(Observable<T> a) {
            return a;
        }
    }
}
