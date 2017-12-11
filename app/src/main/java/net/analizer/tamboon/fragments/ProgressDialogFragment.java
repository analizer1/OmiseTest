package net.analizer.tamboon.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import net.analizer.tamboon.R;

public class ProgressDialogFragment extends DialogFragment {
    private static final String ARG_CANCELABLE = "isCancelable";

    private OnDismissListener mOnDismissListener;
    private OnCancelListener mOnCancelListener;

    public static ProgressDialogFragment newInstance(boolean isCancelable) {
        ProgressDialogFragment loadingDialogFragment = new ProgressDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_CANCELABLE, isCancelable);
        loadingDialogFragment.setArguments(args);
        return loadingDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        boolean isCancelable = false;
        if (arguments != null) {
            isCancelable = arguments.getBoolean(ARG_CANCELABLE, false);
        }

        ProgressDialog dialog = new ProgressDialog(getActivity(), getTheme());
        dialog.setIndeterminate(true);
        dialog.setMessage(getString(R.string.msg_please_wait));
        dialog.setCancelable(isCancelable);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (mOnCancelListener != null) {
            mOnCancelListener.onCancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOnDismissListener = null;
        mOnCancelListener = null;
    }

    public void setOnDismissListener(OnDismissListener listener) {
        mOnDismissListener = listener;
    }

    public void setOnCancelListener(OnCancelListener listener) {
        mOnCancelListener = listener;
    }


    public interface OnDismissListener {
        void onDismiss();
    }

    public interface OnCancelListener {
        void onCancel();
    }
}
