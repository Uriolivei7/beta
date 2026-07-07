package com.facebook.react.module.model;

import D2.h;
import com.facebook.react.turbomodule.core.interfaces.TurboModule;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class ReactModuleInfo {

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final a f6873g = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final String f6874a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final String f6875b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final boolean f6876c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final boolean f6877d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final boolean f6878e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final boolean f6879f;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final boolean a(Class cls) {
            h.f(cls, "clazz");
            return TurboModule.class.isAssignableFrom(cls);
        }

        private a() {
        }
    }

    public ReactModuleInfo(String str, String str2, boolean z3, boolean z4, boolean z5, boolean z6) {
        h.f(str, "name");
        h.f(str2, "className");
        this.f6874a = str;
        this.f6875b = str2;
        this.f6876c = z3;
        this.f6877d = z4;
        this.f6878e = z5;
        this.f6879f = z6;
    }

    public static final boolean b(Class cls) {
        return f6873g.a(cls);
    }

    public final boolean a() {
        return this.f6876c;
    }

    public final String c() {
        return this.f6875b;
    }

    public final boolean d() {
        return this.f6878e;
    }

    public final boolean e() {
        return this.f6879f;
    }

    public final String f() {
        return this.f6874a;
    }

    public final boolean g() {
        return this.f6877d;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public ReactModuleInfo(String str, String str2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7) {
        this(str, str2, z3, z4, z6, z7);
        h.f(str, "name");
        h.f(str2, "className");
    }
}
