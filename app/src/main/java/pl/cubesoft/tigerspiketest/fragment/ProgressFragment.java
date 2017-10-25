package pl.cubesoft.tigerspiketest.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;

import pl.cubesoft.tigerspiketest.R;


public class ProgressFragment extends DialogFragment {


    private static final String ARG_MESSAGE = "message";

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle args = getArguments();
        final String message = args.getString(ARG_MESSAGE);
        return createProgressDialog(getContext(), message, false);
    }

    private Dialog createProgressDialog(Context context, String message, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppThemeDialog);
        builder
                .setTitle(message)
                .setCancelable(cancelable)
                .setView(R.layout.dialog_progress);

        return builder.create();
    }

    public static void show1(Context context, int message, FragmentManager fm, String tag) {
        final ProgressFragment fragment = ProgressFragment.newInstance(context, message);
        //fm.beginTransaction().show(fragment).commit();
        fragment.show(fm, tag);
    }

    public static void show(Context context, int message, FragmentManager fm, String tag) {
        final ProgressFragment fragment = ProgressFragment.newInstance(context, message);
        FragmentTransaction trans = fm.beginTransaction();
        trans.add(fragment, tag);
        trans.commitAllowingStateLoss();
    }

    private static ProgressFragment newInstance(Context context, int message) {
        final ProgressFragment fragment = new ProgressFragment();
        final Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, context.getString(message));
        fragment.setArguments(args);
        return fragment;
    }


}
