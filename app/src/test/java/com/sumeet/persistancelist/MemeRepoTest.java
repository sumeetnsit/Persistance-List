package com.sumeet.persistancelist;

import com.sumeet.persistancelist.data.MemesRepoImpl;
import com.sumeet.persistancelist.data.local.MemesLocalRepo;
import com.sumeet.persistancelist.data.remote.MemesRemoteRepo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(application = MemeApplication.class)
public class MemeRepoTest {

    @Mock
    public MemesRemoteRepo memesRemoteRepo;

    @Mock
    public MemesLocalRepo memesLocalRepo;

    private MemesRepoImpl memesRepo;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        memesRepo = new MemesRepoImpl(memesRemoteRepo, memesLocalRepo);
    }

    @Test
    public void networkResponse_success() {
        //System under test
        Mockito.when(memesRemoteRepo.getAllMemes()).thenReturn(TestUtils.DUMMY_MEME_RESPONSE_DTO_SUCCESS);
        Mockito.when(memesLocalRepo.getAllMeme()).thenReturn(TestUtils.getMockedMemeListSuccess());

        memesRepo
                .getMemes()
                .test()
                .awaitCount(1)
                .assertValue(TestUtils.getMockedMemeListSuccess().blockingFirst())
                .dispose();

    }

    @Test
    public void networkResponse_fail_db_get() {
        //System under test
        Mockito.when(memesRemoteRepo.getAllMemes()).thenReturn(TestUtils.getBadResponseFromNetwork());
        Mockito.when(memesLocalRepo.getAllMeme()).thenReturn(TestUtils.getMockedMemeListSuccess());

        memesRepo
                .getMemes()
                .test()
                .awaitCount(1)
                .assertValue(TestUtils.getMockedMemeListSuccess().blockingFirst())
                .dispose();

    }

    @Test
    public void networkResponse_fail_empty_db() {
        //System under test
        Mockito.when(memesRemoteRepo.getAllMemes()).thenReturn(TestUtils.getBadResponseFromNetwork());
        Mockito.when(memesLocalRepo.getAllMeme()).thenReturn(TestUtils.getEmptyList());

        memesRepo
                .getMemes()
                .test()
                .awaitCount(1)
                .assertValue(TestUtils.getEmptyList().blockingFirst())
                .dispose();

    }
}
