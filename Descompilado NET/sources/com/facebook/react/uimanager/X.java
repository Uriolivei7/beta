package com.facebook.react.uimanager;

import kotlin.enums.EnumEntries;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: loaded from: classes.dex */
public final class X {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final X f7408b = new X("POINT", 0);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final X f7409c = new X("PERCENT", 1);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final /* synthetic */ X[] f7410d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f7411e;

    static {
        X[] xArrA = a();
        f7410d = xArrA;
        f7411e = AbstractC0764a.a(xArrA);
    }

    private X(String str, int i3) {
    }

    private static final /* synthetic */ X[] a() {
        return new X[]{f7408b, f7409c};
    }

    public static X valueOf(String str) {
        return (X) Enum.valueOf(X.class, str);
    }

    public static X[] values() {
        return (X[]) f7410d.clone();
    }
}
