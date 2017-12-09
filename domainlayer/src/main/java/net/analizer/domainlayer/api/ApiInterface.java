package net.analizer.domainlayer.api;

import net.analizer.domainlayer.models.Charity;
import net.analizer.domainlayer.models.CreditCartInfo;
import net.analizer.domainlayer.models.Donation;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;

/**
 * Map to our available Api
 */
public interface ApiInterface {

    /**
     * Get a list of available charities from server.
     *
     * @return a list of {@link Charity}
     */
    Observable<List<Charity>> getCharityList();

    /**
     * Make a donation to a selected charity.
     *
     * @param donation the donation
     * @return
     */
    Observable<String> donate(@Body Donation donation);

    /**
     * Get access token.
     *
     * @param creditCartInfo contains credit card details.
     * @return an access token.
     */
    Observable<String> getToken(CreditCartInfo creditCartInfo);
}
