package com.facebook.react.uimanager;

import kotlin.enums.EnumEntries;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: renamed from: com.facebook.react.uimanager.a0, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class EnumC0419a0 {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final EnumC0419a0 f7441b = new EnumC0419a0("PARENT", 0);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final EnumC0419a0 f7442c = new EnumC0419a0("LEAF", 1);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final EnumC0419a0 f7443d = new EnumC0419a0("NONE", 2);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final /* synthetic */ EnumC0419a0[] f7444e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f7445f;

    static {
        EnumC0419a0[] enumC0419a0ArrA = a();
        f7444e = enumC0419a0ArrA;
        f7445f = AbstractC0764a.a(enumC0419a0ArrA);
    }

    private EnumC0419a0(String str, int i3) {
    }

    private static final /* synthetic */ EnumC0419a0[] a() {
        return new EnumC0419a0[]{f7441b, f7442c, f7443d};
    }

    public static EnumC0419a0 valueOf(String str) {
        return (EnumC0419a0) Enum.valueOf(EnumC0419a0.class, str);
    }

    public static EnumC0419a0[] values() {
        return (EnumC0419a0[]) f7444e.clone();
    }
}
