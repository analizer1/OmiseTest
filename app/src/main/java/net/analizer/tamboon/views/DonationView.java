package net.analizer.tamboon.views;

import net.analizer.domainlayer.models.CreditCartInfo;

public interface DonationView extends LoadingView {

    /**
     * Enable donate button.
     */
    void enableDonateBtn();

    /**
     * Disable donate button.
     */
    void disableDonateBtn();

    /**
     * Notify user that they have entered invalid card details.
     */
    void displayInvalidCardInfo();

    /**
     * Notify user that the donation amount is invalid (greater than zero).
     */
    void displayInvalidDonationAmount();

    /**
     * Notify user that the donation has been made successfully.
     */
    void displayDonationComplete();

    /**
     * Display Credit Card Editor.
     */
    void displayCardEditor();

    /**
     * Display Credit Card Detail.
     *
     * @param creditCartInfo the Credit Card Detail
     */
    void displayCreditCard(CreditCartInfo creditCartInfo);
}
