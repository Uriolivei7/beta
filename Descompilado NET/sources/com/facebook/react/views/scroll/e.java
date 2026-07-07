package com.facebook.react.views.scroll;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
public final class e extends com.facebook.react.views.view.g {

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private final boolean f7767t;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public e(Context context) {
        super(context);
        D2.h.f(context, "context");
        this.f7767t = com.facebook.react.modules.i18nmanager.a.f6977a.a().i(context);
    }

    @Override // com.facebook.react.views.view.g, com.facebook.react.uimanager.InterfaceC0435i0
    public boolean getRemoveClippedSubviews() {
        return super.getRemoveClippedSubviews();
    }

    @Override // com.facebook.react.views.view.g, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z3, int i3, int i4, int i5, int i6) {
        if (this.f7767t) {
            setLeft(0);
            setTop(i4);
            setRight(i5 - i3);
            setBottom(i6);
        }
    }

    @Override // com.facebook.react.views.view.g
    public void setRemoveClippedSubviews(boolean z3) {
        if (this.f7767t) {
            super.setRemoveClippedSubviews(false);
        } else {
            super.setRemoveClippedSubviews(z3);
        }
    }
}
