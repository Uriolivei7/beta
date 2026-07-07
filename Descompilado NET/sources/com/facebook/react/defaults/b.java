package com.facebook.react.defaults;

import d1.AbstractActivityC0510s;
import d1.C0514w;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public class b extends C0514w {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final boolean f6568f;

    public /* synthetic */ b(AbstractActivityC0510s abstractActivityC0510s, String str, boolean z3, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(abstractActivityC0510s, str, (i3 & 4) != 0 ? false : z3);
    }

    @Override // d1.C0514w
    protected boolean k() {
        return this.f6568f;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public b(AbstractActivityC0510s abstractActivityC0510s, String str, boolean z3) {
        super(abstractActivityC0510s, str);
        D2.h.f(abstractActivityC0510s, "activity");
        D2.h.f(str, "mainComponentName");
        this.f6568f = z3;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public b(AbstractActivityC0510s abstractActivityC0510s, String str, boolean z3, boolean z4) {
        this(abstractActivityC0510s, str, z3);
        D2.h.f(abstractActivityC0510s, "activity");
        D2.h.f(str, "mainComponentName");
    }
}
