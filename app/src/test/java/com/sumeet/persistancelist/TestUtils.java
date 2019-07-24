package com.sumeet.persistancelist;

import com.sumeet.persistancelist.data.Meme;
import com.sumeet.persistancelist.data.remote.MemesResponseDto;
import com.sumeet.persistancelist.data.remote.NetworkError;

import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class TestUtils {

    static final Observable<MemesResponseDto> DUMMY_MEME_RESPONSE_DTO_SUCCESS = Observable.just(
            new MemesResponseDto(
                    true,
                    new MemesResponseDto.Data(getMockedMemeListSuccess().blockingFirst())));

    public static final Observable<MemesResponseDto> DUMMY_MEME_RESPONSE_DTO_FAIL = Observable.just(
            new MemesResponseDto(
                    false,
                    Mockito.mock(MemesResponseDto.Data.class)));

    static Observable<List<Meme>> getMockedMemeListSuccess() {
        ArrayList<Meme> list = new ArrayList<>();
        list.add(new Meme("0", "mock", "moccccck"));
        list.add(new Meme("1", "mock1", "moccccck1"));
        list.add(new Meme("2", "mock2", "moccccck2"));
        return Observable.just(list);
    }

    public static  Observable<MemesResponseDto> getBadResponseFromNetwork() {
        return Observable.error(new NetworkError());
    }

    public static Observable<List<Meme>> getEmptyList() {
        return Observable.just(new ArrayList<>());
    }

}
