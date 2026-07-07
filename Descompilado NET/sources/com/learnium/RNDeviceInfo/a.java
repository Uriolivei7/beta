package com.learnium.RNDeviceInfo;

/* JADX INFO: loaded from: classes.dex */
public enum a {
    HANDSET("Handset"),
    TABLET("Tablet"),
    TV("Tv"),
    UNKNOWN("unknown");


    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final String f8392b;

    a(String str) {
        this.f8392b = str;
    }

    public String b() {
        return this.f8392b;
    }
}
