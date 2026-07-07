package com.facebook.react.common;

import kotlin.enums.EnumEntries;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: loaded from: classes.dex */
public final class LifecycleState {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final LifecycleState f6516b = new LifecycleState("BEFORE_CREATE", 0);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final LifecycleState f6517c = new LifecycleState("BEFORE_RESUME", 1);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final LifecycleState f6518d = new LifecycleState("RESUMED", 2);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final /* synthetic */ LifecycleState[] f6519e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f6520f;

    static {
        LifecycleState[] lifecycleStateArrA = a();
        f6519e = lifecycleStateArrA;
        f6520f = AbstractC0764a.a(lifecycleStateArrA);
    }

    private LifecycleState(String str, int i3) {
    }

    private static final /* synthetic */ LifecycleState[] a() {
        return new LifecycleState[]{f6516b, f6517c, f6518d};
    }

    public static LifecycleState valueOf(String str) {
        return (LifecycleState) Enum.valueOf(LifecycleState.class, str);
    }

    public static LifecycleState[] values() {
        return (LifecycleState[]) f6519e.clone();
    }
}
