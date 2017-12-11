package net.analizer.tamboon;

import net.analizer.domainlayer.api.ApiInterface;
import net.analizer.domainlayer.models.Charity;
import net.analizer.tamboon.presenters.CharityPresenter;
import net.analizer.tamboon.views.CharityListView;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CharityPresenterTest {

    @ClassRule
    public static final RxImmediateSchedulerRule schedulers = new RxImmediateSchedulerRule();

    private CharityPresenter charityPresenter;

    @Mock
    private CharityListView charityListView;

    @Mock
    private ApiInterface apiInterface;

    @Mock
    private Charity charity;

    @Before
    public void setUp() {
        charityPresenter = new CharityPresenter(apiInterface);
        charityPresenter.setView(charityListView);
    }

    @Test
    public void testSuccessfulDownloadCharityList() throws Exception {
        List<Charity> charityList = new ArrayList<>();
        charityList.add(charity);

        Observable<List<Charity>> charityObservable = Observable.just(charityList);
        when(apiInterface.getCharityList()).thenReturn(charityObservable);

        charityPresenter.loadCharityList();
        verify(charityListView).showLoading(false);
        verify(charityListView).displayCharityList(charityList);
        verify(charityListView).dismissLoading();
    }

    @Test
    public void testUnSuccessfulDownloadCharityList() throws Exception {
        Observable<List<Charity>> charityObservable = Observable.error(new Throwable("Test Error"));
        when(apiInterface.getCharityList()).thenReturn(charityObservable);

        charityPresenter.loadCharityList();
        verify(charityListView).showLoading(false);
        verify(charityListView).dismissLoading();
        verify(charityListView).clearList();
        verify(charityListView).displayError("Test Error");
    }
}