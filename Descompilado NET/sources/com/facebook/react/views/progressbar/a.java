package com.facebook.react.views.progressbar;

import D2.h;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.views.progressbar.ReactProgressBarViewManager;
import d1.AbstractC0505m;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class a extends FrameLayout {

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final C0114a f7737g = new C0114a(null);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Integer f7738b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private boolean f7739c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f7740d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private double f7741e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private ProgressBar f7742f;

    /* JADX INFO: renamed from: com.facebook.react.views.progressbar.a$a, reason: collision with other inner class name */
    private static final class C0114a {
        public /* synthetic */ C0114a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private C0114a() {
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a(Context context) {
        super(context);
        h.f(context, "context");
        this.f7739c = true;
        this.f7740d = true;
    }

    private final void setColor(ProgressBar progressBar) {
        Drawable indeterminateDrawable = progressBar.isIndeterminate() ? progressBar.getIndeterminateDrawable() : progressBar.getProgressDrawable();
        if (indeterminateDrawable == null) {
            return;
        }
        Integer num = this.f7738b;
        if (num != null) {
            indeterminateDrawable.setColorFilter(num.intValue(), PorterDuff.Mode.SRC_IN);
        } else {
            indeterminateDrawable.clearColorFilter();
        }
    }

    public final void a() {
        ProgressBar progressBar = this.f7742f;
        if (progressBar == null) {
            throw new JSApplicationIllegalArgumentException("setStyle() not called");
        }
        progressBar.setIndeterminate(this.f7739c);
        setColor(progressBar);
        progressBar.setProgress((int) (this.f7741e * ((double) 1000)));
        progressBar.setVisibility(this.f7740d ? 0 : 4);
    }

    public final boolean getAnimating$ReactAndroid_release() {
        return this.f7740d;
    }

    public final Integer getColor$ReactAndroid_release() {
        return this.f7738b;
    }

    public final boolean getIndeterminate$ReactAndroid_release() {
        return this.f7739c;
    }

    public final double getProgress$ReactAndroid_release() {
        return this.f7741e;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        h.f(accessibilityNodeInfo, "info");
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        String str = (String) getTag(AbstractC0505m.f9247t);
        if (str != null) {
            accessibilityNodeInfo.setViewIdResourceName(str);
        }
    }

    public final void setAnimating$ReactAndroid_release(boolean z3) {
        this.f7740d = z3;
    }

    public final void setColor$ReactAndroid_release(Integer num) {
        this.f7738b = num;
    }

    public final void setIndeterminate$ReactAndroid_release(boolean z3) {
        this.f7739c = z3;
    }

    public final void setProgress$ReactAndroid_release(double d4) {
        this.f7741e = d4;
    }

    public final void setStyle$ReactAndroid_release(String str) {
        ReactProgressBarViewManager.a aVar = ReactProgressBarViewManager.Companion;
        ProgressBar progressBarA = aVar.a(getContext(), aVar.b(str));
        progressBarA.setMax(1000);
        this.f7742f = progressBarA;
        removeAllViews();
        addView(this.f7742f, new ViewGroup.LayoutParams(-1, -1));
    }
}
