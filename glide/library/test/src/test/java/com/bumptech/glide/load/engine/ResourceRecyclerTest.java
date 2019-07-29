package com.bumptech.glide.load.engine;

import android.os.Looper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

import static com.bumptech.glide.tests.Util.mockResource;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 18)
public class ResourceRecyclerTest {

    private ResourceRecycler recycler;

    @Before
    public void setUp() {
        recycler = new ResourceRecycler();
    }

    @Test
    public void testRecyclesResourceSynchronouslyIfNotAlreadyRecyclingResource() {
        Resource<?> resource = mockResource();
        Shadows.shadowOf(Looper.getMainLooper()).pause();
        recycler.recycle(resource);
        verify(resource).recycle();
    }

    @Test
    public void testDoesNotRecycleChildResourceSynchronously() {
        Resource<?> parent = mockResource();
        final Resource<?> child = mockResource();
        doAnswer(
                new Answer<Void>() {
                    @Override
                    public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                        recycler.recycle(child);
                        return null;
                    }
                })
                .when(parent)
                .recycle();

        Shadows.shadowOf(Looper.getMainLooper()).pause();

        recycler.recycle(parent);

        verify(parent).recycle();
        verify(child, never()).recycle();

        Shadows.shadowOf(Looper.getMainLooper()).runOneTask();

        verify(child).recycle();
    }
}
