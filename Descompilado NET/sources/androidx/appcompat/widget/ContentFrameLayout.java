package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;

/* JADX INFO: loaded from: classes.dex */
public class ContentFrameLayout extends FrameLayout {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private TypedValue f3766b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private TypedValue f3767c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private TypedValue f3768d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private TypedValue f3769e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private TypedValue f3770f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private TypedValue f3771g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final Rect f3772h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private a f3773i;

    public interface a {
        void a();

        void onDetachedFromWindow();
    }

    public ContentFrameLayout(Context context) {
        this(context, null);
    }

    public void a(int i3, int i4, int i5, int i6) {
        this.f3772h.set(i3, i4, i5, i6);
        if (isLaidOut()) {
            requestLayout();
        }
    }

    public TypedValue getFixedHeightMajor() {
        if (this.f3770f == null) {
            this.f3770f = new TypedValue();
        }
        return this.f3770f;
    }

    public TypedValue getFixedHeightMinor() {
        if (this.f3771g == null) {
            this.f3771g = new TypedValue();
        }
        return this.f3771g;
    }

    public TypedValue getFixedWidthMajor() {
        if (this.f3768d == null) {
            this.f3768d = new TypedValue();
        }
        return this.f3768d;
    }

    public TypedValue getFixedWidthMinor() {
        if (this.f3769e == null) {
            this.f3769e = new TypedValue();
        }
        return this.f3769e;
    }

    public TypedValue getMinWidthMajor() {
        if (this.f3766b == null) {
            this.f3766b = new TypedValue();
        }
        return this.f3766b;
    }

    public TypedValue getMinWidthMinor() {
        if (this.f3767c == null) {
            this.f3767c = new TypedValue();
        }
        return this.f3767c;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        a aVar = this.f3773i;
        if (aVar != null) {
            aVar.a();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        a aVar = this.f3773i;
        if (aVar != null) {
            aVar.onDetachedFromWindow();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x004a  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0060  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0086  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00cc  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00d6  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00db  */
    @Override // android.widget.FrameLayout, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void onMeasure(int r14, int r15) {
        /*
            Method dump skipped, instruction units count: 226
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.ContentFrameLayout.onMeasure(int, int):void");
    }

    public void setAttachListener(a aVar) {
        this.f3773i = aVar;
    }

    public ContentFrameLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ContentFrameLayout(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.f3772h = new Rect();
    }
}
