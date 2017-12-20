package com.axlecho.jtsviewer;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by axlecho on 17-12-20.
 */

public class RxJavaUnitTest {
    @Before
    public void setUp() {

    }

    @Test
    public void MocksTest() {
        TestObserver<Mock> testObserver = new TestObserver<>();
        getMocks().subscribe(testObserver);
        assertThat(testObserver.getEvents().size(), is(3));
    }


    private Observable<Mock> getMocks() {
        return Observable.just(new Mock(), new Mock(), new Mock());
    }

    class Mock {
    }
}
