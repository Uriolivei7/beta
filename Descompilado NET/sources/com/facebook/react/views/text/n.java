package com.facebook.react.views.text;

import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import androidx.core.view.Z;
import com.facebook.react.uimanager.C0433h0;
import d1.AbstractC0505m;
import d1.AbstractC0508p;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r.w;

/* JADX INFO: loaded from: classes.dex */
public final class n extends C0433h0 {

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    public static final b f8002y = new b(null);

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private a f8003x;

    public static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final List f8004a;

        /* JADX INFO: renamed from: com.facebook.react.views.text.n$a$a, reason: collision with other inner class name */
        public static final class C0118a {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            private String f8005a;

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            private int f8006b;

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            private int f8007c;

            /* JADX INFO: renamed from: d, reason: collision with root package name */
            private int f8008d;

            public final String a() {
                return this.f8005a;
            }

            public final int b() {
                return this.f8007c;
            }

            public final int c() {
                return this.f8008d;
            }

            public final int d() {
                return this.f8006b;
            }

            public final void e(String str) {
                this.f8005a = str;
            }

            public final void f(int i3) {
                this.f8007c = i3;
            }

            public final void g(int i3) {
                this.f8008d = i3;
            }

            public final void h(int i3) {
                this.f8006b = i3;
            }
        }

        public a(ClickableSpan[] clickableSpanArr, Spannable spannable) {
            D2.h.f(clickableSpanArr, "spans");
            D2.h.f(spannable, "text");
            ArrayList arrayList = new ArrayList();
            int length = clickableSpanArr.length;
            for (int i3 = 0; i3 < length; i3++) {
                ClickableSpan clickableSpan = clickableSpanArr[i3];
                int spanStart = spannable.getSpanStart(clickableSpan);
                int spanEnd = spannable.getSpanEnd(clickableSpan);
                if (spanStart != spanEnd && spanStart >= 0 && spanEnd >= 0 && spanStart <= spannable.length() && spanEnd <= spannable.length()) {
                    C0118a c0118a = new C0118a();
                    c0118a.e(spannable.subSequence(spanStart, spanEnd).toString());
                    c0118a.h(spanStart);
                    c0118a.f(spanEnd);
                    c0118a.g((clickableSpanArr.length - 1) - i3);
                    arrayList.add(c0118a);
                }
            }
            this.f8004a = arrayList;
        }

        public final C0118a a(int i3) {
            for (C0118a c0118a : this.f8004a) {
                if (c0118a.c() == i3) {
                    return c0118a;
                }
            }
            return null;
        }

        public final C0118a b(int i3, int i4) {
            for (C0118a c0118a : this.f8004a) {
                if (c0118a.d() == i3 && c0118a.b() == i4) {
                    return c0118a;
                }
            }
            return null;
        }

        public final int c() {
            return this.f8004a.size();
        }
    }

    public static final class b {
        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final void a(View view, boolean z3, int i3) {
            D2.h.f(view, "view");
            Z.X(view, new n(view, z3, i3));
        }

        public final void b(View view, boolean z3, int i3) {
            D2.h.f(view, "view");
            if (Z.C(view)) {
                return;
            }
            if (view.getTag(AbstractC0505m.f9234g) == null && view.getTag(AbstractC0505m.f9235h) == null && view.getTag(AbstractC0505m.f9228a) == null && view.getTag(AbstractC0505m.f9247t) == null && view.getTag(AbstractC0505m.f9230c) == null && view.getTag(AbstractC0505m.f9233f) == null && view.getTag(AbstractC0505m.f9253z) == null) {
                return;
            }
            Z.X(view, new n(view, z3, i3));
        }

        private b() {
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public n(View view, boolean z3, int i3) {
        super(view, z3, i3);
        D2.h.f(view, "view");
        this.f8003x = (a) V().getTag(AbstractC0505m.f9233f);
    }

    private final Rect k0(a.C0118a c0118a) {
        if (!(V() instanceof TextView)) {
            return new Rect(0, 0, V().getWidth(), V().getHeight());
        }
        View viewV = V();
        D2.h.d(viewV, "null cannot be cast to non-null type android.widget.TextView");
        TextView textView = (TextView) viewV;
        Layout layout = textView.getLayout();
        if (layout == null) {
            return new Rect(0, 0, textView.getWidth(), textView.getHeight());
        }
        int iD = c0118a.d();
        int iB = c0118a.b();
        int lineForOffset = layout.getLineForOffset(iD);
        if (iD > layout.getLineEnd(lineForOffset)) {
            return null;
        }
        Rect rect = new Rect();
        double primaryHorizontal = layout.getPrimaryHorizontal(iD);
        new Paint().setTextSize(((AbsoluteSizeSpan) l0(c0118a.d(), c0118a.b(), AbsoluteSizeSpan.class)) != null ? r9.getSize() : textView.getTextSize());
        int iCeil = (int) Math.ceil(r3.measureText(c0118a.a()));
        boolean z3 = lineForOffset != layout.getLineForOffset(iB);
        layout.getLineBounds(lineForOffset, rect);
        int scrollY = textView.getScrollY() + textView.getTotalPaddingTop();
        rect.top += scrollY;
        rect.bottom += scrollY;
        rect.left = (int) (((double) rect.left) + ((primaryHorizontal + ((double) textView.getTotalPaddingLeft())) - ((double) textView.getScrollX())));
        if (z3) {
            return new Rect(rect.left, rect.top, rect.right, rect.bottom);
        }
        int i3 = rect.left;
        return new Rect(i3, rect.top, iCeil + i3, rect.bottom);
    }

    @Override // com.facebook.react.uimanager.C0433h0, x.AbstractC0766a
    protected void A(List list) {
        D2.h.f(list, "virtualViewIds");
        a aVar = this.f8003x;
        if (aVar == null) {
            return;
        }
        int iC = aVar.c();
        for (int i3 = 0; i3 < iC; i3++) {
            list.add(Integer.valueOf(i3));
        }
    }

    @Override // com.facebook.react.uimanager.C0433h0, x.AbstractC0766a
    protected boolean H(int i3, int i4, Bundle bundle) {
        a.C0118a c0118aA;
        ClickableSpan clickableSpan;
        a aVar = this.f8003x;
        if (aVar == null || aVar == null || (c0118aA = aVar.a(i3)) == null || (clickableSpan = (ClickableSpan) l0(c0118aA.d(), c0118aA.b(), ClickableSpan.class)) == null || !(clickableSpan instanceof Z1.f) || i4 != 16) {
            return false;
        }
        View viewV = V();
        D2.h.e(viewV, "getHostView(...)");
        ((Z1.f) clickableSpan).onClick(viewV);
        return true;
    }

    @Override // com.facebook.react.uimanager.C0433h0, x.AbstractC0766a
    protected void L(int i3, r.v vVar) {
        D2.h.f(vVar, "node");
        a aVar = this.f8003x;
        if (aVar == null) {
            vVar.t0("");
            vVar.l0(new Rect(0, 0, 1, 1));
            return;
        }
        a.C0118a c0118aA = aVar.a(i3);
        if (c0118aA == null) {
            vVar.t0("");
            vVar.l0(new Rect(0, 0, 1, 1));
            return;
        }
        Rect rectK0 = k0(c0118aA);
        if (rectK0 == null) {
            vVar.t0("");
            vVar.l0(new Rect(0, 0, 1, 1));
            return;
        }
        vVar.t0(c0118aA.a());
        vVar.a(16);
        vVar.l0(rectK0);
        vVar.F0(V().getResources().getString(AbstractC0508p.f9296w));
        vVar.p0(C0433h0.d.e(C0433h0.d.BUTTON));
    }

    @Override // x.AbstractC0766a
    protected void M(int i3, boolean z3) {
        a.C0118a c0118aA;
        ClickableSpan clickableSpan;
        a aVar = this.f8003x;
        if (aVar == null || aVar == null || (c0118aA = aVar.a(i3)) == null || (clickableSpan = (ClickableSpan) l0(c0118aA.d(), c0118aA.b(), ClickableSpan.class)) == null || !(clickableSpan instanceof Z1.f) || !(V() instanceof m)) {
            return;
        }
        Z1.f fVar = (Z1.f) clickableSpan;
        fVar.b(z3);
        View viewV = V();
        D2.h.d(viewV, "null cannot be cast to non-null type android.widget.TextView");
        fVar.a(((TextView) viewV).getHighlightColor());
        V().invalidate();
    }

    @Override // com.facebook.react.uimanager.C0433h0, x.AbstractC0766a, androidx.core.view.C0237a
    public w b(View view) {
        D2.h.f(view, "host");
        if (this.f8003x != null) {
            return j0(view);
        }
        return null;
    }

    protected final Object l0(int i3, int i4, Class cls) {
        if (!(V() instanceof TextView)) {
            return null;
        }
        View viewV = V();
        D2.h.d(viewV, "null cannot be cast to non-null type android.widget.TextView");
        if (!(((TextView) viewV).getText() instanceof Spanned)) {
            return null;
        }
        View viewV2 = V();
        D2.h.d(viewV2, "null cannot be cast to non-null type android.widget.TextView");
        CharSequence text = ((TextView) viewV2).getText();
        D2.h.d(text, "null cannot be cast to non-null type android.text.Spanned");
        Object[] spans = ((Spanned) text).getSpans(i3, i4, cls);
        D2.h.c(spans);
        if (spans.length == 0) {
            return null;
        }
        return spans[0];
    }

    @Override // com.facebook.react.uimanager.C0433h0, x.AbstractC0766a
    protected int z(float f3, float f4) {
        Layout layout;
        a aVar = this.f8003x;
        if (aVar == null || aVar.c() == 0 || !(V() instanceof TextView)) {
            return Integer.MIN_VALUE;
        }
        View viewV = V();
        D2.h.d(viewV, "null cannot be cast to non-null type android.widget.TextView");
        TextView textView = (TextView) viewV;
        if (!(textView.getText() instanceof Spanned) || (layout = textView.getLayout()) == null) {
            return Integer.MIN_VALUE;
        }
        int offsetForHorizontal = layout.getOffsetForHorizontal(layout.getLineForVertical((int) ((f4 - textView.getTotalPaddingTop()) + textView.getScrollY())), (f3 - textView.getTotalPaddingLeft()) + textView.getScrollX());
        ClickableSpan clickableSpan = (ClickableSpan) l0(offsetForHorizontal, offsetForHorizontal, ClickableSpan.class);
        if (clickableSpan == null) {
            return Integer.MIN_VALUE;
        }
        CharSequence text = textView.getText();
        D2.h.d(text, "null cannot be cast to non-null type android.text.Spanned");
        Spanned spanned = (Spanned) text;
        a.C0118a c0118aB = aVar.b(spanned.getSpanStart(clickableSpan), spanned.getSpanEnd(clickableSpan));
        if (c0118aB != null) {
            return c0118aB.c();
        }
        return Integer.MIN_VALUE;
    }
}
