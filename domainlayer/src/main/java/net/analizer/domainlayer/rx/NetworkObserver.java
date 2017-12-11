package net.analizer.domainlayer.rx;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class NetworkObserver<T> implements Observer<T> {
    private T mModel;

    public abstract void onResponse(T response);

    public abstract void onNetworkError(Throwable t);

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(T value) {
        mModel = value;
    }

    @Override
    public void onError(Throwable e) {
        onNetworkError(e);
    }

    @Override
    public void onComplete() {
        onResponse(mModel);
    }
}
