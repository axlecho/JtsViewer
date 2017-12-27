package com.axlecho.jtsviewer;

import android.content.Context;

import com.axlecho.jtsviewer.module.JtsTabDetailModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.module.JtsUserModule;
import com.axlecho.jtsviewer.network.JtsSchedulers;
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

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.subscribers.TestSubscriber;
import okhttp3.ResponseBody;
import retrofit2.Response;

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

    @Test
    public void testLogin() {
        server.login("6b3db232", "http://www.jitashe.org/", "d39", "123456789", 2592000)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        //System.out.println(response.body().string());
                    }
                });
    }

    @Test
    public void testPostCommentWithLogin() {
        server.login("6b3db232", "http://www.jitashe.org/", "d39", "123456789", 2592000)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String responseBodyResponse) throws Exception {
                        server.getDetail(24285).subscribe(new Consumer<JtsTabDetailModule>() {
                            @Override
                            public void accept(JtsTabDetailModule jtsTabDetailModule) throws Exception {
                                final JtsTabDetailModule module = jtsTabDetailModule;
                                server.postComment(module.fid, 24285, "66666666666666666666", module.formhash)
                                        .subscribe(new Consumer<String>() {
                                            @Override
                                            public void accept(String s) throws Exception {
                                                System.out.println(s);
                                            }
                                        });
                            }
                        });
                    }
                });


    }

    private Consumer<Throwable> errorHandler = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            // System.out.println(throwable.getMessage());
            // throwable.printStackTrace();
            throw new AssertionError(throwable);
        }
    };

    private class MockSchedulers extends JtsSchedulers {
        @Override
        public <T> Observable<T> switchSchedulers(Observable<T> a) {
            return a;
        }
    }
}
