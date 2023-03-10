package com.cointizen.paysdk.dialog.delaccount;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.cointizen.paysdk.utils.MCHInflaterUtils;
import com.cointizen.paysdk.utils.MCLog;


public class DeleteSuccessDialog extends DialogFragment {

    private static final String TAG = "AboutAboutAddAccountDialog";
    private Context activity;

    public DeleteSuccessDialog() { }

    @SuppressLint("ValidFragment")
    public DeleteSuccessDialog(Context activity) {
        this.activity = activity;
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View containerView = inflater.inflate(MCHInflaterUtils.getIdByName(activity, "layout", "mch_dialog_deletesuccess"), container, false);
        WindowManager.LayoutParams params = ((Activity)activity).getWindow().getAttributes();
        params.alpha = 0.5f;
        ((Activity)activity).getWindow().setAttributes(params);

        Button btnCancel = (Button) containerView.findViewById(MCHInflaterUtils.getControl(activity, "btn_del_close"));
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });

        setCancelable(false);
        this.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismissAllowingStateLoss();
                    return true;
                } else {
                    return false;
                }
            }
        });
        return containerView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ????????????????????????
        setStyle(STYLE_NO_FRAME, MCHInflaterUtils.getIdByName(activity, "style", "mch_MCTipDialog"));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        WindowManager.LayoutParams params = ((Activity)activity).getWindow().getAttributes();
        params.alpha = 1f;
        ((Activity)activity).getWindow().setAttributes(params);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @SuppressLint("NewApi")
    @Override
    public void onStart() {
        // 1, ????????????????????????
        Window window = getDialog().getWindow();
        WindowManager wm = window.getWindowManager();
        Point windowSize = new Point();
        wm.getDefaultDisplay().getSize(windowSize);
        float size_x = 0;
        float size_y = 0;
        int width = windowSize.x;
        int height = windowSize.y;
        if (width >= height) {// ??????
            size_x = 0.7f;
            size_y = 0.8f;
//            window.getAttributes().width = (int) (windowSize.y * 0.8);
            window.getAttributes().width = windowSize.x;
//            window.getAttributes().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.getAttributes().height = windowSize.y;
        } else {// ??????
            size_x = 0.9f;
            size_y = 0.855f;
//            window.getAttributes().width = (int) (windowSize.x * 0.85);
            window.getAttributes().width = windowSize.x;
//            window.getAttributes().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.getAttributes().height = windowSize.y;
        }
        window.setGravity(Gravity.CENTER);
        super.onStart();
    }

    public static class Builder {
        /**
         * ?????????????????????
         **/
        private Bundle mBundle;
        private DeleteSuccessDialog dialog;

        private View.OnClickListener mmDelSuccessClick;

        public Builder() {
            mBundle = new Bundle();
        }

        private DeleteSuccessDialog create(Activity activity) {
            dialog = new DeleteSuccessDialog(activity);
            // 1,??????????????????
            dialog.setArguments(mBundle);

            return dialog;
        }

        public DeleteSuccessDialog show(Activity activity, FragmentManager fm) {
            if (fm == null) {
                MCLog.e(TAG, "show error : fragment manager is null.");
                return null;
            }

            DeleteSuccessDialog dialog = create(activity);
            MCLog.d(TAG, "show AboutAddAccountDialog.");

            FragmentTransaction ft = fm.beginTransaction();
            ft.add(dialog, TAG);
            ft.show(dialog);
            ft.commitAllowingStateLoss();
            return dialog;
        }


    }

}
