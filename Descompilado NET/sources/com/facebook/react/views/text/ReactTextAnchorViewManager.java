package com.facebook.react.views.text;

import android.text.TextUtils;
import android.view.View;
import com.facebook.react.animated.NativeAnimatedModule;
import com.facebook.react.uimanager.BaseViewManager;
import com.facebook.react.uimanager.C0418a;
import com.facebook.react.uimanager.W;
import com.facebook.react.uimanager.X;
import com.facebook.react.views.text.c;

/* JADX INFO: loaded from: classes.dex */
public abstract class ReactTextAnchorViewManager<T extends View, C extends c> extends BaseViewManager<T, C> {
    private static final int[] SPACING_TYPES = {8, 0, 2, 1, 3, 4, 5};
    private static final String TAG = "ReactTextAnchorViewManager";

    @L1.a(name = "accessible")
    public void setAccessible(m mVar, boolean z3) {
        mVar.setFocusable(z3);
    }

    @L1.a(name = "adjustsFontSizeToFit")
    public void setAdjustFontSizeToFit(m mVar, boolean z3) {
        mVar.setAdjustFontSizeToFit(z3);
    }

    @L1.a(name = "android_hyphenationFrequency")
    public void setAndroidHyphenationFrequency(m mVar, String str) {
        if (str == null || str.equals("none")) {
            mVar.setHyphenationFrequency(0);
            return;
        }
        if (str.equals("full")) {
            mVar.setHyphenationFrequency(2);
            return;
        }
        if (str.equals("normal")) {
            mVar.setHyphenationFrequency(1);
            return;
        }
        Y.a.I("ReactNative", "Invalid android_hyphenationFrequency: " + str);
        mVar.setHyphenationFrequency(0);
    }

    @L1.b(customType = "Color", names = {"borderColor", "borderLeftColor", "borderRightColor", "borderTopColor", "borderBottomColor"})
    public void setBorderColor(m mVar, int i3, Integer num) {
        C0418a.p(mVar, R1.n.f2075c, num);
    }

    @L1.b(defaultFloat = Float.NaN, names = {"borderRadius", "borderTopLeftRadius", "borderTopRightRadius", "borderBottomRightRadius", "borderBottomLeftRadius"})
    public void setBorderRadius(m mVar, int i3, float f3) {
        C0418a.q(mVar, R1.d.values()[i3], Float.isNaN(f3) ? null : new W(f3, X.f7408b));
    }

    @L1.a(name = "borderStyle")
    public void setBorderStyle(m mVar, String str) {
        C0418a.r(mVar, str == null ? null : R1.f.b(str));
    }

    @L1.b(defaultFloat = Float.NaN, names = {"borderWidth", "borderLeftWidth", "borderRightWidth", "borderTopWidth", "borderBottomWidth", "borderStartWidth", "borderEndWidth"})
    public void setBorderWidth(m mVar, int i3, float f3) {
        C0418a.s(mVar, R1.n.values()[i3], Float.valueOf(f3));
    }

    @L1.a(name = "dataDetectorType")
    public void setDataDetectorType(m mVar, String str) {
        if (str != null) {
            switch (str) {
                case "phoneNumber":
                    mVar.setLinkifyMask(4);
                    break;
                case "all":
                    mVar.setLinkifyMask(15);
                    break;
                case "link":
                    mVar.setLinkifyMask(1);
                    break;
                case "email":
                    mVar.setLinkifyMask(2);
                    break;
            }
            return;
        }
        mVar.setLinkifyMask(0);
    }

    @L1.a(defaultBoolean = NativeAnimatedModule.ANIMATED_MODULE_DEBUG, name = "disabled")
    public void setDisabled(m mVar, boolean z3) {
        mVar.setEnabled(!z3);
    }

    @L1.a(name = "ellipsizeMode")
    public void setEllipsizeMode(m mVar, String str) {
        if (str == null || str.equals("tail")) {
            mVar.setEllipsizeLocation(TextUtils.TruncateAt.END);
            return;
        }
        if (str.equals("head")) {
            mVar.setEllipsizeLocation(TextUtils.TruncateAt.START);
            return;
        }
        if (str.equals("middle")) {
            mVar.setEllipsizeLocation(TextUtils.TruncateAt.MIDDLE);
            return;
        }
        if (str.equals("clip")) {
            mVar.setEllipsizeLocation(null);
            return;
        }
        Y.a.I("ReactNative", "Invalid ellipsizeMode: " + str);
        mVar.setEllipsizeLocation(TextUtils.TruncateAt.END);
    }

    @L1.a(name = "fontSize")
    public void setFontSize(m mVar, float f3) {
        mVar.setFontSize(f3);
    }

    @L1.a(defaultBoolean = true, name = "includeFontPadding")
    public void setIncludeFontPadding(m mVar, boolean z3) {
        mVar.setIncludeFontPadding(z3);
    }

    @L1.a(defaultFloat = 0.0f, name = "letterSpacing")
    public void setLetterSpacing(m mVar, float f3) {
        mVar.setLetterSpacing(f3);
    }

    @L1.a(name = "onInlineViewLayout")
    public void setNotifyOnInlineViewLayout(m mVar, boolean z3) {
        mVar.setNotifyOnInlineViewLayout(z3);
    }

    @L1.a(defaultInt = Integer.MAX_VALUE, name = "numberOfLines")
    public void setNumberOfLines(m mVar, int i3) {
        mVar.setNumberOfLines(i3);
    }

    @L1.a(name = "selectable")
    public void setSelectable(m mVar, boolean z3) {
        mVar.setTextIsSelectable(z3);
    }

    @L1.a(customType = "Color", name = "selectionColor")
    public void setSelectionColor(m mVar, Integer num) {
        if (num == null) {
            mVar.setHighlightColor(a.c(mVar.getContext()));
        } else {
            mVar.setHighlightColor(num.intValue());
        }
    }

    @L1.a(name = "textAlignVertical")
    public void setTextAlignVertical(m mVar, String str) {
        if (str == null || "auto".equals(str)) {
            mVar.setGravityVertical(0);
            return;
        }
        if ("top".equals(str)) {
            mVar.setGravityVertical(48);
            return;
        }
        if ("bottom".equals(str)) {
            mVar.setGravityVertical(80);
            return;
        }
        if ("center".equals(str)) {
            mVar.setGravityVertical(16);
            return;
        }
        Y.a.I("ReactNative", "Invalid textAlignVertical: " + str);
        mVar.setGravityVertical(0);
    }
}
