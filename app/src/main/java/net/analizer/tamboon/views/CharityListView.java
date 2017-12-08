package net.analizer.tamboon.views;

import net.analizer.domainlayer.models.Charity;

import java.util.List;

public interface CharityListView extends LoadingView {

    /**
     * Display a list of charities.
     *
     * @param charityList list of charities
     */
    void displayCharityList(List<Charity> charityList);

    /**
     * Clear the charity list.
     */
    void clearList();
}
