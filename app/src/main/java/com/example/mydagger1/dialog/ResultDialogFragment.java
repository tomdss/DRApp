package com.example.mydagger1.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;

import com.example.mydagger1.R;

public class ResultDialogFragment extends BaseDialogFragment<ResultDialogFragment.OnResultDialogFragmentClickListener> {

    private TextView tvDialogResult;
    private TextView tvDialogNextButton;
    private Boolean mIsWin;

    // Create an instance of the Dialog with the input
    public static ResultDialogFragment newInstance(Boolean isWin) {
        ResultDialogFragment frag = new ResultDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean("isWin", isWin);
        frag.setArguments(args);
        return frag;
    }

    // Create a Dialog using default AlertDialog builder , if not inflate custom view in onCreateView
    /*@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mIsWin = getArguments().getBoolean("isWin");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_result, null);
        if (mIsWin) {
            Dialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Dialog))
                    .setView(view)
                    .setTitle(getArguments().getString("Win"))
                    .setMessage(getArguments().getString("message"))
                    .setCancelable(false)
                    .setPositiveButton("NEXT",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // Positive button clicked
                                    getActivityInstance().onWinClicked(ResultDialogFragment.this);
                                }
                            }
                    ).create();

            return dialog;
        } else {
            Dialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle(getArguments().getString("Lose"))
                    .setMessage(getArguments().getString("message"))
                    .setCancelable(false)
                    .setPositiveButton("AGAIN",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // Positive button clicked
                                    getActivityInstance().onLoseClicked(ResultDialogFragment.this);
                                }
                            }
                    ).create();

            return dialog;
        }

    }*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog() != null) {
            getDialog().setCanceledOnTouchOutside(false);
        }
        return inflater.inflate(R.layout.dialog_result, container);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,
                R.style.FullScreenDialog);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIsWin = getArguments().getBoolean("isWin");
        tvDialogResult = view.findViewById(R.id.tvDialogTitle);
        tvDialogNextButton = view.findViewById(R.id.tvDialogNextButton);
        if (mIsWin) {
            tvDialogResult.setText("WIN");
            tvDialogNextButton.setText("NEXT");
            tvDialogNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivityInstance().onWinClicked(ResultDialogFragment.this);
                }
            });
        } else {
            tvDialogResult.setText("LOSE");
            tvDialogNextButton.setText("PLAY AGAIN");
            tvDialogNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivityInstance().onLoseClicked(ResultDialogFragment.this);
                }
            });
        }
    }

    // interface to handle the dialog click back to the Activity
    public interface OnResultDialogFragmentClickListener {

        public void onWinClicked(ResultDialogFragment dialog);

        public void onLoseClicked(ResultDialogFragment dialog);
    }
}