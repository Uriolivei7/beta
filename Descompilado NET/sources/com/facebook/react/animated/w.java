package com.facebook.react.animated;

import com.facebook.react.bridge.ReadableMap;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public class w extends b {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public double f6495f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public double f6496g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private c f6497h;

    /* JADX WARN: Multi-variable type inference failed */
    public w() {
        this(null, 1, 0 == true ? 1 : 0);
    }

    @Override // com.facebook.react.animated.b
    public String e() {
        return "ValueAnimatedNode[" + this.f6381d + "]: value: " + this.f6495f + " offset: " + this.f6496g;
    }

    public final void i() {
        this.f6496g += this.f6495f;
        this.f6495f = 0.0d;
    }

    public final void j() {
        this.f6495f += this.f6496g;
        this.f6496g = 0.0d;
    }

    public Object k() {
        return null;
    }

    public final double l() {
        if (Double.isNaN(this.f6496g + this.f6495f)) {
            h();
        }
        return this.f6496g + this.f6495f;
    }

    public final void m() {
        c cVar = this.f6497h;
        if (cVar != null) {
            cVar.a(l());
        }
    }

    public final void n(c cVar) {
        this.f6497h = cVar;
    }

    public w(ReadableMap readableMap) {
        this.f6495f = readableMap != null ? readableMap.getDouble("value") : Double.NaN;
        this.f6496g = readableMap != null ? readableMap.getDouble("offset") : 0.0d;
    }

    public /* synthetic */ w(ReadableMap readableMap, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? null : readableMap);
    }
}
