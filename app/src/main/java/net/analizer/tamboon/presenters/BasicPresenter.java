package net.analizer.tamboon.presenters;

import android.support.annotation.Nullable;

public interface BasicPresenter<T> {
    void setView(@Nullable T view);

    @Nullable
    T getView();
}
