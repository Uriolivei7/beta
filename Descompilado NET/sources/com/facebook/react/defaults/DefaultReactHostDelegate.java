package com.facebook.react.defaults;

import C2.l;
import com.facebook.react.bridge.JSBundleLoader;
import com.facebook.react.runtime.BindingsInstaller;
import com.facebook.react.runtime.InterfaceC0398f;
import com.facebook.react.runtime.JSRuntimeFactory;
import com.facebook.react.runtime.hermes.HermesInstance;
import d1.O;
import d1.V;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.r;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultReactHostDelegate implements InterfaceC0398f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final String f6553a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final JSBundleLoader f6554b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final List f6555c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final JSRuntimeFactory f6556d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final BindingsInstaller f6557e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final l f6558f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final V.a f6559g;

    public DefaultReactHostDelegate(String str, JSBundleLoader jSBundleLoader, List<? extends O> list, JSRuntimeFactory jSRuntimeFactory, BindingsInstaller bindingsInstaller, l lVar, V.a aVar) {
        D2.h.f(str, "jsMainModulePath");
        D2.h.f(jSBundleLoader, "jsBundleLoader");
        D2.h.f(list, "reactPackages");
        D2.h.f(jSRuntimeFactory, "jsRuntimeFactory");
        D2.h.f(lVar, "exceptionHandler");
        D2.h.f(aVar, "turboModuleManagerDelegateBuilder");
        this.f6553a = str;
        this.f6554b = jSBundleLoader;
        this.f6555c = list;
        this.f6556d = jSRuntimeFactory;
        this.f6557e = bindingsInstaller;
        this.f6558f = lVar;
        this.f6559g = aVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final r h(Exception exc) throws Exception {
        D2.h.f(exc, "it");
        throw exc;
    }

    @Override // com.facebook.react.runtime.InterfaceC0398f
    public void a(Exception exc) {
        D2.h.f(exc, "error");
        this.f6558f.d(exc);
    }

    @Override // com.facebook.react.runtime.InterfaceC0398f
    public JSBundleLoader b() {
        return this.f6554b;
    }

    @Override // com.facebook.react.runtime.InterfaceC0398f
    public V.a c() {
        return this.f6559g;
    }

    @Override // com.facebook.react.runtime.InterfaceC0398f
    public JSRuntimeFactory d() {
        return this.f6556d;
    }

    @Override // com.facebook.react.runtime.InterfaceC0398f
    public String e() {
        return this.f6553a;
    }

    @Override // com.facebook.react.runtime.InterfaceC0398f
    public List f() {
        return this.f6555c;
    }

    @Override // com.facebook.react.runtime.InterfaceC0398f
    public BindingsInstaller getBindingsInstaller() {
        return this.f6557e;
    }

    public /* synthetic */ DefaultReactHostDelegate(String str, JSBundleLoader jSBundleLoader, List list, JSRuntimeFactory jSRuntimeFactory, BindingsInstaller bindingsInstaller, l lVar, V.a aVar, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, jSBundleLoader, (i3 & 4) != 0 ? AbstractC0717n.g() : list, (i3 & 8) != 0 ? new HermesInstance() : jSRuntimeFactory, (i3 & 16) != 0 ? null : bindingsInstaller, (i3 & 32) != 0 ? new l() { // from class: com.facebook.react.defaults.e
            @Override // C2.l
            public final Object d(Object obj) {
                return DefaultReactHostDelegate.h((Exception) obj);
            }
        } : lVar, aVar);
    }
}
