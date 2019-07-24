package com.sumeet.persistancelist;

import android.accounts.NetworkErrorException;

import com.sumeet.persistancelist.data.Meme;
import com.sumeet.persistancelist.ui.memelist.MemeListViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import io.reactivex.Observable;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
public class MemeLisViewModelTest {

    private MemeListViewModel memeListViewModel;
    private MemeListViewModel.MemeListViewModelInteraction uiInteraction;

    @Before()
    public void initMocks() {
        memeListViewModel = new MemeListViewModel();
        uiInteraction = Mockito.mock(MemeListViewModel.MemeListViewModelInteraction.class);
    }

    private Observable<List<Meme>> getMockedMemeListFail() {
        return Observable.error(NetworkErrorException::new);
    }

    @Test
    public void onNetworkSuccess() {
        //when
        memeListViewModel.setDataAndSubscribe(uiInteraction, TestUtils.getMockedMemeListSuccess());

        //then
        Mockito.verify(uiInteraction).onFetchStart();
        Mockito.verify(uiInteraction).onFetchSuccess(TestUtils.getMockedMemeListSuccess().blockingFirst());
        Mockito.verify(uiInteraction, Mockito.never()).onFetchFailed(Mockito.any());
    }

    @Test
    public void onNetworkError() {
        //when
        memeListViewModel.setDataAndSubscribe(uiInteraction, getMockedMemeListFail());

        //then
        Mockito.verify(uiInteraction).onFetchStart();
        Mockito.verify(uiInteraction).onFetchFailed(Mockito.any());
        Mockito.verify(uiInteraction, Mockito.never()).onFetchSuccess(Mockito.any());
    }
}
