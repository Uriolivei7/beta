package com.facebook.react.views.text;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import f1.C0534a;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f7946b = new a(null);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static d f7947c;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final C0534a f7948a;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final d a() {
            d dVar = d.f7947c;
            if (dVar != null) {
                return dVar;
            }
            d dVar2 = new d(C0534a.f9381c.c(), null);
            d.f7947c = dVar2;
            return dVar2;
        }

        private a() {
        }
    }

    public /* synthetic */ d(C0534a c0534a, DefaultConstructorMarker defaultConstructorMarker) {
        this(c0534a);
    }

    public static final d c() {
        return f7946b.a();
    }

    public final Typeface d(String str, int i3, AssetManager assetManager) {
        D2.h.f(str, "fontFamilyName");
        D2.h.f(assetManager, "assetManager");
        return this.f7948a.d(str, i3, assetManager);
    }

    private d(C0534a c0534a) {
        this.f7948a = c0534a;
    }
}
