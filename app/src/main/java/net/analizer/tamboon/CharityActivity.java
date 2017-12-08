package net.analizer.tamboon;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.View;

import net.analizer.domainlayer.models.Charity;
import net.analizer.tamboon.adapters.CharityListAdapter;
import net.analizer.tamboon.databinding.ActivityCharityBinding;
import net.analizer.tamboon.presenters.CharityPresenter;
import net.analizer.tamboon.views.CharityListView;

import java.util.List;

import javax.inject.Inject;

public class CharityActivity extends BaseActivity implements CharityListView {

    @Inject
    CharityPresenter charityPresenter;

    private ActivityCharityBinding mViewBinding;
    private CharityListAdapter mCharityListAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_charity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = getViewBinding(ActivityCharityBinding.class);

        initializeInjector();
        initializeUIs();
    }

    @Override
    protected void onDestroy() {
        charityPresenter.setView(null);

        super.onDestroy();
    }

    @Override
    public void showLoading(boolean isCancelable) {
        if (canUpdateView()) {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            ProgressDialogFragment progressDialogFragment = ProgressDialogFragment.newInstance(isCancelable);
            supportFragmentManager
                    .beginTransaction()
                    .add(progressDialogFragment, ProgressDialogFragment.class.getName())
                    .commitNowAllowingStateLoss();
        }
    }

    @Override
    public synchronized void dismissLoading() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        ProgressDialogFragment loadingDialogFragment = (ProgressDialogFragment)
                supportFragmentManager.findFragmentByTag(ProgressDialogFragment.class.getName());
        if (loadingDialogFragment != null) {
            loadingDialogFragment.dismissAllowingStateLoss();
            supportFragmentManager.executePendingTransactions();
        }
    }

    @Override
    public void displayError(@NonNull String errMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatDialog);
        builder.setTitle(R.string.error_title)
                .setMessage(errMsg)
                .setPositiveButton(R.string.label_ok, (dialogInterface, i) -> {
                    // do something after the OK button is clicked here...
                })
                .show();
    }

    @Override
    public void displayCharityList(List<Charity> charityList) {
        mViewBinding.charityRecyclerView.setVisibility(View.VISIBLE);
        mViewBinding.emptyCharityListTextView.setVisibility(View.GONE);

        mCharityListAdapter = new CharityListAdapter(charityList);
        mViewBinding.charityRecyclerView.setAdapter(mCharityListAdapter);
    }

    @Override
    public void clearList() {
        mViewBinding.emptyCharityListTextView.setVisibility(View.VISIBLE);
        mViewBinding.charityRecyclerView.setVisibility(View.GONE);

        if (mCharityListAdapter != null) {
            mCharityListAdapter.notifyDataSetChanged();
        }
    }

    private void initializeInjector() {
        getApplicationComponent().inject(this);

        // initialize Activity Component here (IF ANY)
    }

    private void initializeUIs() {
        charityPresenter.setView(this);

        // do other UIs initialization...
        charityPresenter.loadCharityList();
    }
}
