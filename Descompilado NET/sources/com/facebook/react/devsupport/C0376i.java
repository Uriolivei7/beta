package com.facebook.react.devsupport;

import android.content.Context;
import g1.C0542a;
import java.util.Map;
import k1.InterfaceC0584b;
import k1.InterfaceC0585c;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: renamed from: com.facebook.react.devsupport.i, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0376i implements H {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final a f6723a = new a(null);

    /* JADX INFO: renamed from: com.facebook.react.devsupport.i$a */
    private static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    @Override // com.facebook.react.devsupport.H
    public k1.e a(Context context, c0 c0Var, String str, boolean z3, k1.i iVar, InterfaceC0584b interfaceC0584b, int i3, Map map, e1.k kVar, InterfaceC0585c interfaceC0585c, k1.h hVar, boolean z4) {
        D2.h.f(context, "applicationContext");
        D2.h.f(c0Var, "reactInstanceManagerHelper");
        return !z4 ? C0542a.f9425d ? new b0(context) : new k0() : new C0368a(context, c0Var, str, z3, iVar, interfaceC0584b, i3, map, kVar, interfaceC0585c, hVar);
    }

    @Override // com.facebook.react.devsupport.H
    public k1.e b(Context context, c0 c0Var, String str, boolean z3, k1.i iVar, InterfaceC0584b interfaceC0584b, int i3, Map map, e1.k kVar, InterfaceC0585c interfaceC0585c, k1.h hVar) {
        D2.h.f(context, "applicationContext");
        D2.h.f(c0Var, "reactInstanceManagerHelper");
        if (!z3) {
            return new k0();
        }
        try {
            String str2 = "com.facebook.react.devsupport.BridgeDevSupportManager";
            D2.h.e(str2, "toString(...)");
            Object objNewInstance = Class.forName(str2).getConstructor(Context.class, c0.class, String.class, Boolean.TYPE, k1.i.class, InterfaceC0584b.class, Integer.TYPE, Map.class, e1.k.class, InterfaceC0585c.class, k1.h.class).newInstance(context, c0Var, str, Boolean.TRUE, iVar, interfaceC0584b, Integer.valueOf(i3), map, kVar, interfaceC0585c, hVar);
            D2.h.d(objNewInstance, "null cannot be cast to non-null type com.facebook.react.devsupport.interfaces.DevSupportManager");
            return (k1.e) objNewInstance;
        } catch (Exception unused) {
            return new b0(context);
        }
    }
}
