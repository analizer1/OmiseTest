package net.analizer.tamboon.views;

import android.support.annotation.NonNull;

public interface LoadingView {

    /**
     * Show loading animation.
     *
     * @param isCancelable True if the loading view is cancelable.
     */
    void showLoading(boolean isCancelable);

    /**
     * Dismiss loading animation.
     */
    void dismissLoading();

    /**
     * Display an error message.
     *
     * @param errMsg the error message to be displayed.
     */
    void displayError(@NonNull String errMsg);
}
