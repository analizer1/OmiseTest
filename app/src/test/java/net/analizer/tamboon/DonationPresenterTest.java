package net.analizer.tamboon;

import net.analizer.domainlayer.api.ApiInterface;
import net.analizer.domainlayer.models.CreditCartInfo;
import net.analizer.domainlayer.models.Donation;
import net.analizer.tamboon.views.DonationPresenter;
import net.analizer.tamboon.views.DonationView;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DonationPresenterTest {

    @ClassRule
    public static final RxImmediateSchedulerRule schedulers = new RxImmediateSchedulerRule();

    private DonationPresenter donationPresenter;

    private CreditCartInfo validCreditCartInfo;

    private CreditCartInfo invalidCreditCartInfo;

    @Mock
    private DonationView donationView;

    @Mock
    private ApiInterface apiInterface;

    @Mock
    private Donation donation;

    @Before
    public void setUp() {
        donationPresenter = new DonationPresenter(apiInterface);
        donationPresenter.setView(donationView);

        validCreditCartInfo = new CreditCartInfo();
        validCreditCartInfo.setCreditCardHolderName("PANATCHAI VATHANASRI");
        validCreditCartInfo.setCreditCardNo("38056789000000000");
        validCreditCartInfo.setCreditCardExpiry("01/22");
        validCreditCartInfo.setCreditCardCCV("552");

        invalidCreditCartInfo = new CreditCartInfo();
        invalidCreditCartInfo.setCreditCardHolderName("PANATCHAI VATHANASRI");
        invalidCreditCartInfo.setCreditCardNo("");
        invalidCreditCartInfo.setCreditCardExpiry("01/22");
        invalidCreditCartInfo.setCreditCardCCV("552");
    }

    @Test
    public void textCreditCardValidationForValidCard() throws Exception {
        donationPresenter.onDonationDetailsEntered(validCreditCartInfo, 1);
        verify(donationView).enableDonateBtn();
    }

    @Test
    public void textCreditCardValidationForInvValidCard() throws Exception {
        donationPresenter.onDonationDetailsEntered(invalidCreditCartInfo, 1);

        verify(donationView).disableDonateBtn();
        verify(donationView).displayInvalidCardInfo();
    }

    @Test
    public void textCreditCardValidationForInvalidDonationAmount() throws Exception {
        donationPresenter.onDonationDetailsEntered(validCreditCartInfo, 0);

        verify(donationView).disableDonateBtn();
        verify(donationView).displayInvalidDonationAmount();
    }

    @Test
    public void testSuccessfulDonate() throws Exception {
        Observable<String> donateObservable = Observable.just("Success");
        Observable<String> tokenObservable = Observable.just("Some Token");

        when(apiInterface.getToken(validCreditCartInfo)).thenReturn(tokenObservable);
        when(apiInterface.donate(any(Donation.class))).thenReturn(donateObservable);

        donationPresenter.onSubmitDonation(validCreditCartInfo, 1);

        verify(donationView).showLoading(false);
        verify(donationView).dismissLoading();
        verify(donationView).displayDonationComplete();
    }

    @Test
    public void testUnSuccessfulDonate() throws Exception {
        Observable<String> donateObservable = Observable.error(new Throwable("Donation Api Error"));
        Observable<String> tokenObservable = Observable.just("Some Token");

        when(apiInterface.getToken(validCreditCartInfo)).thenReturn(tokenObservable);
        when(apiInterface.donate(any(Donation.class))).thenReturn(donateObservable);

        donationPresenter.onSubmitDonation(validCreditCartInfo, 1);

        verify(donationView).showLoading(false);
        verify(donationView).dismissLoading();
        verify(donationView).displayError("Donation Api Error");
    }

    @Test
    public void testUnSuccessfulGetToken() throws Exception {
        Observable<String> tokenObservable = Observable.error(new Throwable("AccessToken Api Error"));

        when(apiInterface.getToken(validCreditCartInfo)).thenReturn(tokenObservable);

        donationPresenter.onSubmitDonation(validCreditCartInfo, 1);

        verify(donationView).showLoading(false);
        verify(donationView).dismissLoading();
        verify(donationView).displayError("AccessToken Api Error");
    }
}