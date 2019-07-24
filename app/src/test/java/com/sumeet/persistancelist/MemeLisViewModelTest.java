package com.sumeet.persistancelist;

import android.accounts.NetworkErrorException;

import com.sumeet.persistancelist.data.Meme;
import com.sumeet.persistancelist.data.remote.MemesResponseDto;
import com.sumeet.persistancelist.ui.memelist.MemeListViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
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

    private static final MemesResponseDto DUMMY_MEME_RESPONSE_DTO_SUCCESS = new MemesResponseDto(
            true,
            Mockito.mock(MemesResponseDto.Data.class));

    private static final MemesResponseDto DUMMY_MEME_RESPONSE_DTO_FAIL = new
            MemesResponseDto(
            false,
            Mockito.mock(MemesResponseDto.Data.class));


    private Observable<List<Meme>> getMockedMemeListsuccess() {
        ArrayList<Meme> list = new ArrayList<>();
        list.add(new Meme("0", "mock", "moccccck"));
        list.add(new Meme("1", "mock1", "moccccck1"));
        list.add(new Meme("2", "mock2", "moccccck2"));
        return Observable.just(list);
    }

    private Observable<List<Meme>> getMockedMemeListFail() {
        return Observable.error(NetworkErrorException::new);
    }

    @Test
    public void onNetworkSuccess() {
        //when
        memeListViewModel.setDataAndSubscribe(uiInteraction, getMockedMemeListsuccess());

        //then
        Mockito.verify(uiInteraction).onFetchStart();
        Mockito.verify(uiInteraction).onFetchSuccess(getMockedMemeListsuccess().blockingFirst());
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
