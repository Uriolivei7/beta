package r;

import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.view.View;

/* JADX INFO: renamed from: r.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0658a extends ClickableSpan {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f10430a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final v f10431b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int f10432c;

    public C0658a(int i3, v vVar, int i4) {
        this.f10430a = i3;
        this.f10431b = vVar;
        this.f10432c = i4;
    }

    @Override // android.text.style.ClickableSpan
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("ACCESSIBILITY_CLICKABLE_SPAN_ID", this.f10430a);
        this.f10431b.f0(this.f10432c, bundle);
    }
}
