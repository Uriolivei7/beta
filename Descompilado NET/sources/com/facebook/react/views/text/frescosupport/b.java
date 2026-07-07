package com.facebook.react.views.text.frescosupport;

import U0.c;
import Z1.p;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.TextView;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.C0429f0;
import com.facebook.react.views.image.d;
import q.g;
import q0.AbstractC0646b;
import u0.C0734a;
import u0.C0735b;
import x0.C0770b;

/* JADX INFO: loaded from: classes.dex */
class b extends p {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Drawable f7958b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final AbstractC0646b f7959c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final C0770b f7960d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final Object f7961e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private int f7962f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private int f7963g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private Uri f7964h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private int f7965i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private ReadableMap f7966j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private String f7967k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private TextView f7968l;

    public b(Resources resources, int i3, int i4, int i5, Uri uri, ReadableMap readableMap, AbstractC0646b abstractC0646b, Object obj, String str) {
        this.f7960d = new C0770b(C0735b.u(resources).a());
        this.f7959c = abstractC0646b;
        this.f7961e = obj;
        this.f7963g = i5;
        this.f7964h = uri == null ? Uri.EMPTY : uri;
        this.f7966j = readableMap;
        this.f7965i = (int) C0429f0.h(i4);
        this.f7962f = (int) C0429f0.h(i3);
        this.f7967k = str;
    }

    @Override // Z1.p
    public Drawable a() {
        return this.f7958b;
    }

    @Override // Z1.p
    public int b() {
        return this.f7962f;
    }

    @Override // Z1.p
    public void c() {
        this.f7960d.j();
    }

    @Override // Z1.p
    public void d() {
        this.f7960d.k();
    }

    @Override // android.text.style.ReplacementSpan
    public void draw(Canvas canvas, CharSequence charSequence, int i3, int i4, float f3, int i5, int i6, int i7, Paint paint) {
        if (this.f7958b == null) {
            E1.b bVarA = E1.b.A(c.x(this.f7964h), this.f7966j);
            ((C0734a) this.f7960d.f()).v(d.c(this.f7967k));
            this.f7959c.A();
            this.f7959c.b(this.f7960d.e());
            Object obj = this.f7961e;
            if (obj != null) {
                this.f7959c.C(obj);
            }
            this.f7959c.E(bVarA);
            this.f7960d.o(this.f7959c.a());
            this.f7959c.A();
            Drawable drawable = (Drawable) g.g(this.f7960d.g());
            this.f7958b = drawable;
            drawable.setBounds(0, 0, this.f7965i, this.f7962f);
            int i8 = this.f7963g;
            if (i8 != 0) {
                this.f7958b.setColorFilter(i8, PorterDuff.Mode.SRC_IN);
            }
            this.f7958b.setCallback(this.f7968l);
        }
        canvas.save();
        canvas.translate(f3, ((i6 + ((int) paint.descent())) - (((int) (paint.descent() - paint.ascent())) / 2)) - ((this.f7958b.getBounds().bottom - this.f7958b.getBounds().top) / 2));
        this.f7958b.draw(canvas);
        canvas.restore();
    }

    @Override // Z1.p
    public void e() {
        this.f7960d.j();
    }

    @Override // Z1.p
    public void f() {
        this.f7960d.k();
    }

    @Override // android.text.style.ReplacementSpan
    public int getSize(Paint paint, CharSequence charSequence, int i3, int i4, Paint.FontMetricsInt fontMetricsInt) {
        if (fontMetricsInt != null) {
            int i5 = -this.f7962f;
            fontMetricsInt.ascent = i5;
            fontMetricsInt.descent = 0;
            fontMetricsInt.top = i5;
            fontMetricsInt.bottom = 0;
        }
        return this.f7965i;
    }

    @Override // Z1.p
    public void h(TextView textView) {
        this.f7968l = textView;
    }
}
