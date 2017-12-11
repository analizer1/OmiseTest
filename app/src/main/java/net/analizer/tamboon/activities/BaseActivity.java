package net.analizer.tamboon.activities;

import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import net.analizer.tamboon.R;
import net.analizer.tamboon.dagger2.HasComponent;
import net.analizer.tamboon.dagger2.components.AppComponent;

public abstract class BaseActivity extends AppCompatActivity {

    private ViewDataBinding mViewBinding;
    protected boolean isDestroyed = false;

    protected abstract int getLayoutResId();

    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDestroyed = false;

        if (getLayoutResId() > 0) {
            mViewBinding = DataBindingUtil.setContentView(this, getLayoutResId());
        }
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }

    /**
     * Get the Main Application component for dependency injection.
     *
     * @return {@link AppComponent}
     */
    @SuppressWarnings("unchecked")
    protected AppComponent getApplicationComponent() {
        return ((HasComponent<AppComponent>) getApplication()).getComponent();
    }

    /**
     * Gets a ViewBinding for children.
     */
    @SuppressWarnings("unchecked")
    protected <C> C getViewBinding(Class<C> componentType) {
        return componentType.cast(mViewBinding);
    }

    /**
     * Check whether or not the UIs can be updated or not.
     *
     * @return True if Views are not destroyed and still available for updates. False otherwise.
     */
    protected boolean canUpdateView() {
        return !isDestroyed && mViewBinding != null;
    }

    protected void showAlert(@NonNull String title, @NonNull String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatDialog);
        builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton(R.string.label_ok, (dialogInterface, i) -> {
                    // do something after the OK button is clicked here...
                })
                .show();
    }
}
