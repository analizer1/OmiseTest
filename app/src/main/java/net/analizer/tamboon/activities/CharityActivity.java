package net.analizer.tamboon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.View;

import net.analizer.domainlayer.models.Charity;
import net.analizer.tamboon.R;
import net.analizer.tamboon.adapters.CharityListAdapter;
import net.analizer.tamboon.databinding.ActivityCharityBinding;
import net.analizer.tamboon.fragments.ProgressDialogFragment;
import net.analizer.tamboon.presenters.CharityPresenter;
import net.analizer.tamboon.views.CharityListView;

import java.util.List;

import javax.inject.Inject;

public class CharityActivity extends BaseActivity implements CharityListView {

    @Inject
    CharityPresenter charityPresenter;

    private ActivityCharityBinding mViewBinding;
    private CharityListAdapter mCharityListAdapter;
    private CharityListAdapter.OnCharityClickListener mOnCharityClickListener;

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
        showAlert(getString(R.string.error_title), errMsg);
    }

    @Override
    public void displayCharityList(List<Charity> charityList) {
        mViewBinding.charityRecyclerView.setVisibility(View.VISIBLE);
        mViewBinding.emptyCharityListTextView.setVisibility(View.GONE);

        mCharityListAdapter = new CharityListAdapter(charityList);
        mCharityListAdapter.setOnClickListener(mOnCharityClickListener);
        mViewBinding.charityRecyclerView.setAdapter(mCharityListAdapter);
    }

    @Override
    public void clearList() {
        mViewBinding.emptyCharityListTextView.setVisibility(View.VISIBLE);
        mViewBinding.charityRecyclerView.setVisibility(View.GONE);

        if (mCharityListAdapter != null) {
            mCharityListAdapter.clear();
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
        mOnCharityClickListener = charity -> {
            Intent intent = DonateActivity.getIntent(CharityActivity.this, charity);
            startActivity(intent);
        };

        charityPresenter.loadCharityList();
    }
}
