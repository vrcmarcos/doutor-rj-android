package com.mcardoso.doutorrj.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;

import com.mcardoso.doutorrj.R;

/**
 * Created by mcardoso on 2/27/16.
 */
public class PopUpHelper {

    private static AlertDialog DIALOG;

    private PopUpHelper() {
    }

    public static void show(final Context ctx, PopUpBrand brand, Object... messageArgs) {
        show(ctx, brand, new PopUpClickListener() {
            @Override
            public void onPositiveButtonClicked() {
                dismiss();
            }
        }, messageArgs);
    }

    public static void show(final Context ctx, PopUpBrand brand, final PopUpClickListener listener, Object... messageArgs) {

        if( DIALOG != null && DIALOG.isShowing() ) {
            dismiss();
        }

        Resources res = ctx.getResources();
        DIALOG = new AlertDialog.Builder(ctx)
                .setTitle(res.getString(brand.getTitleId()))
                .setCancelable(false)
                .setMessage(res.getString(brand.getTextId(), messageArgs))
                .setPositiveButton(
                        res.getString(brand.getPositiveButtonId()),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                listener.onPositiveButtonClicked();
                            }
                        })
                .create();
        DIALOG.show();
    }

    public static void dismiss() {
        if ( DIALOG != null ) {
            DIALOG.dismiss();
        }
    }

    public enum PopUpBrand {
        ABOUT(
                R.string.popup_about_title,
                R.string.popup_about_text,
                R.string.popup_about_positive_button),
        GPS_OFF(
                R.string.popup_gps_off_title,
                R.string.popup_gps_off_text,
                R.string.popup_gps_off_positive_button);

        private Integer titleId;
        private Integer textId;
        private Integer positiveButtonId;

        PopUpBrand(Integer titleId, Integer textId, Integer positiveButtonId) {
            this.titleId = titleId;
            this.textId = textId;
            this.positiveButtonId = positiveButtonId;
        }

        public Integer getTitleId() {
            return titleId;
        }

        public Integer getTextId() {
            return textId;
        }

        public Integer getPositiveButtonId() {
            return positiveButtonId;
        }
    }

    public interface PopUpClickListener {
        void onPositiveButtonClicked();
    }
}
