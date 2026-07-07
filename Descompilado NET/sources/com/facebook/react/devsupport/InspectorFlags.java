package com.facebook.react.devsupport;

/* JADX INFO: loaded from: classes.dex */
public final class InspectorFlags {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final InspectorFlags f6639a = new InspectorFlags();

    static {
        I.a();
    }

    private InspectorFlags() {
    }

    public static final native boolean getFuseboxEnabled();

    public static final native boolean getIsProfilingBuild();
}
