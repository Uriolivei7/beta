package com.facebook.react.views.image;

import kotlin.enums.EnumEntries;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final c f7671b = new c("AUTO", 0);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final c f7672c = new c("RESIZE", 1);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final c f7673d = new c("SCALE", 2);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final c f7674e = new c("NONE", 3);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final /* synthetic */ c[] f7675f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f7676g;

    static {
        c[] cVarArrA = a();
        f7675f = cVarArrA;
        f7676g = AbstractC0764a.a(cVarArrA);
    }

    private c(String str, int i3) {
    }

    private static final /* synthetic */ c[] a() {
        return new c[]{f7671b, f7672c, f7673d, f7674e};
    }

    public static c valueOf(String str) {
        return (c) Enum.valueOf(c.class, str);
    }

    public static c[] values() {
        return (c[]) f7675f.clone();
    }
}
