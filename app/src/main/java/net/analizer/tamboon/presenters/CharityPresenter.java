package net.analizer.tamboon.presenters;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import net.analizer.domainlayer.api.ApiInterface;
import net.analizer.domainlayer.models.Charity;
import net.analizer.domainlayer.rx.NetworkObserver;
import net.analizer.tamboon.views.CharityListView;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings("WeakerAccess")
public class CharityPresenter implements BasicPresenter<CharityListView> {
    // Prevent a cyclic reference
    private WeakReference<CharityListView> mCharityRef;

    private ApiInterface mApiInterface;

    @Inject
    public CharityPresenter(ApiInterface mApiInterface) {
        this.mApiInterface = mApiInterface;
    }

    @Override
    public void setView(@Nullable CharityListView view) {

        if (view != null) {
            mCharityRef = new WeakReference<>(view);

        } else if (mCharityRef != null) {
            mCharityRef.clear();
            mCharityRef = null;
        }
    }

    @Nullable
    @Override
    public CharityListView getView() {
        CharityListView charityListView = null;

        if (mCharityRef != null) {
            charityListView = mCharityRef.get();
        }

        return charityListView;
    }

    public void loadCharityList() {
        CharityListView charityListView = mCharityRef.get();
        if (charityListView == null) {
            return;
        }

        charityListView.showLoading(false);

        mApiInterface.getCharityList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new NetworkObserver<List<Charity>>() {
                    @Override
                    public void onResponse(List<Charity> response) {
                        charityListView.dismissLoading();
                        charityListView.displayCharityList(response);
                    }

                    @Override
                    public void onNetworkError(Throwable t) {
                        charityListView.dismissLoading();
                        charityListView.clearList();

                        // In this case,
                        // Client cannot be sure of how well the server is implemented.
                        // These checks reassure that we do not display NULL message.
                        String msg = t.getMessage();
                        if (TextUtils.isEmpty(msg)) {
                            msg = t.toString();
                        }

                        if (TextUtils.isEmpty(msg)) {
                            msg = "Unknown Error";
                        }

                        charityListView.displayError(msg);
                    }
                });
    }
}
