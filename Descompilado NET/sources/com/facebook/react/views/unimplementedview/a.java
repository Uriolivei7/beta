package com.facebook.react.views.unimplementedview;

import D2.h;
import android.content.Context;
import android.widget.LinearLayout;
import androidx.appcompat.widget.D;

/* JADX INFO: loaded from: classes.dex */
public final class a extends LinearLayout {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final D f8158b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a(Context context) {
        super(context);
        h.f(context, "context");
        D d4 = new D(context);
        this.f8158b = d4;
        d4.setLayoutParams(new LinearLayout.LayoutParams(-2, -1));
        d4.setGravity(17);
        d4.setTextColor(-1);
        setBackgroundColor(1442775040);
        setGravity(1);
        setOrientation(1);
        addView(d4);
    }

    public final void setName$ReactAndroid_release(String str) {
        h.f(str, "name");
        this.f8158b.setText("'" + str + "' is not Fabric compatible yet.");
    }
}
