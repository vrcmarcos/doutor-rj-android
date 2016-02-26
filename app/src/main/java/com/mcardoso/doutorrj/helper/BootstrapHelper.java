package com.mcardoso.doutorrj.helper;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;

import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;
import com.beardedhen.androidbootstrap.api.attributes.BootstrapHeading;
import com.beardedhen.androidbootstrap.utils.DimenUtils;
import com.mcardoso.doutorrj.R;

import static com.beardedhen.androidbootstrap.utils.ColorUtils.ACTIVE_OPACITY_FACTOR_EDGE;
import static com.beardedhen.androidbootstrap.utils.ColorUtils.ACTIVE_OPACITY_FACTOR_FILL;
import static com.beardedhen.androidbootstrap.utils.ColorUtils.DISABLED_ALPHA_EDGE;
import static com.beardedhen.androidbootstrap.utils.ColorUtils.DISABLED_ALPHA_FILL;
import static com.beardedhen.androidbootstrap.utils.ColorUtils.decreaseRgbChannels;
import static com.beardedhen.androidbootstrap.utils.ColorUtils.increaseOpacity;
import static com.beardedhen.androidbootstrap.utils.ColorUtils.resolveColor;

/**
 * Created by mcardoso on 1/5/16.
 */
public class BootstrapHelper {

    public enum Brand implements BootstrapBrand {

        GO_TO(R.color.mapToGoButtonBackground,  R.color.mapToGoButtonText),
        UBER(R.color.mapUberButtonBackground,  R.color.mapUberButtonText),
        CENTRALIZE(R.color.mapCentralizeButtonBackground,  R.color.mapCentralizeButtonText);

        private final int color;
        private final int textColor;

        Brand(int color, int textColor) {
            this.color = color;
            this.textColor = textColor;
        }

        @ColorInt
        public int defaultFill(Context context) {
            return resolveColor(color, context);
        }

        @ColorInt public int defaultEdge(Context context) {
            return decreaseRgbChannels(context, color, ACTIVE_OPACITY_FACTOR_EDGE);
        }

        @ColorInt public int activeFill(Context context) {
            return decreaseRgbChannels(context, color, ACTIVE_OPACITY_FACTOR_FILL);
        }

        @ColorInt public int activeEdge(Context context) {
            return decreaseRgbChannels(context, color, ACTIVE_OPACITY_FACTOR_FILL + ACTIVE_OPACITY_FACTOR_EDGE);
        }

        @ColorInt public int disabledFill(Context context) {
            return increaseOpacity(context, color, DISABLED_ALPHA_FILL);
        }

        @ColorInt public int disabledEdge(Context context) {
            return increaseOpacity(context, color, DISABLED_ALPHA_FILL - DISABLED_ALPHA_EDGE);
        }

        @ColorInt public int defaultTextColor(Context context) {
            return resolveColor(textColor, context);
        }

        @ColorInt public int activeTextColor(Context context) {
            return resolveColor(textColor, context);
        }

        @ColorInt public int disabledTextColor(Context context) {
            return resolveColor(textColor, context);
        }

    }

    public enum Heading implements BootstrapHeading {

        H7(
                R.dimen.bootstrap_h7_text_size,
                R.dimen.bootstrap_h7_vert_padding,
                R.dimen.bootstrap_h7_hori_padding
        );


        private final @DimenRes int textSize;
        private final @DimenRes int vertPadding;
        private final @DimenRes int horiPadding;

        Heading(int textSize, int vertPadding, int horiPadding) {
            this.textSize = textSize;
            this.vertPadding = vertPadding;
            this.horiPadding = horiPadding;
        }


        @Override public float getTextSize(Context context) {
            return DimenUtils.pixelsFromSpResource(context, textSize);
        }

        @Override public float verticalPadding(Context context) {
            return DimenUtils.pixelsFromDpResource(context, vertPadding);
        }

        @Override public float horizontalPadding(Context context) {
            return DimenUtils.pixelsFromDpResource(context, horiPadding);
        }
    }
}
