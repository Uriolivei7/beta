package com.facebook.react.defaults;

import C2.l;
import K2.o;
import android.content.Context;
import com.facebook.react.bridge.JSBundleLoader;
import com.facebook.react.defaults.DefaultTurboModuleManagerDelegate;
import com.facebook.react.fabric.ComponentFactory;
import com.facebook.react.runtime.BindingsInstaller;
import com.facebook.react.runtime.JSRuntimeFactory;
import com.facebook.react.runtime.ReactHostImpl;
import com.facebook.react.runtime.hermes.HermesInstance;
import d1.InterfaceC0491A;
import d1.N;
import java.util.Iterator;
import java.util.List;
import r2.r;

/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final d f6569a = new d();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static InterfaceC0491A f6570b;

    private d() {
    }

    public static final InterfaceC0491A b(Context context, N n3, JSRuntimeFactory jSRuntimeFactory) {
        D2.h.f(context, "context");
        D2.h.f(n3, "reactNativeHost");
        if (n3 instanceof g) {
            return ((g) n3).A(context, jSRuntimeFactory);
        }
        throw new IllegalArgumentException("You can call getDefaultReactHost only with instances of DefaultReactNativeHost");
    }

    public static final InterfaceC0491A c(Context context, List list, String str, String str2, String str3, JSRuntimeFactory jSRuntimeFactory, boolean z3, List list2) {
        D2.h.f(context, "context");
        D2.h.f(list, "packageList");
        D2.h.f(str, "jsMainModulePath");
        D2.h.f(str2, "jsBundleAssetPath");
        D2.h.f(list2, "cxxReactPackageProviders");
        return d(context, list, str, str2, str3, jSRuntimeFactory, z3, list2, new l() { // from class: com.facebook.react.defaults.c
            @Override // C2.l
            public final Object d(Object obj) {
                return d.g((Exception) obj);
            }
        }, null);
    }

    public static final InterfaceC0491A d(Context context, List list, String str, String str2, String str3, JSRuntimeFactory jSRuntimeFactory, boolean z3, List list2, l lVar, BindingsInstaller bindingsInstaller) {
        JSBundleLoader jSBundleLoaderCreateAssetLoader;
        D2.h.f(context, "context");
        D2.h.f(list, "packageList");
        D2.h.f(str, "jsMainModulePath");
        D2.h.f(str2, "jsBundleAssetPath");
        D2.h.f(list2, "cxxReactPackageProviders");
        D2.h.f(lVar, "exceptionHandler");
        if (f6570b == null) {
            if (str3 != null) {
                jSBundleLoaderCreateAssetLoader = o.z(str3, "assets://", false, 2, null) ? JSBundleLoader.createAssetLoader(context, str3, true) : JSBundleLoader.createFileLoader(str3);
            } else {
                jSBundleLoaderCreateAssetLoader = JSBundleLoader.createAssetLoader(context, "assets://" + str2, true);
            }
            JSBundleLoader jSBundleLoader = jSBundleLoaderCreateAssetLoader;
            DefaultTurboModuleManagerDelegate.a aVar = new DefaultTurboModuleManagerDelegate.a();
            Iterator it = list2.iterator();
            while (it.hasNext()) {
                aVar.f((l) it.next());
            }
            D2.h.c(jSBundleLoader);
            DefaultReactHostDelegate defaultReactHostDelegate = new DefaultReactHostDelegate(str, jSBundleLoader, list, jSRuntimeFactory == null ? new HermesInstance() : jSRuntimeFactory, bindingsInstaller, lVar, aVar);
            ComponentFactory componentFactory = new ComponentFactory();
            DefaultComponentsRegistry.register(componentFactory);
            f6570b = new ReactHostImpl(context, defaultReactHostDelegate, componentFactory, true, z3);
        }
        InterfaceC0491A interfaceC0491A = f6570b;
        D2.h.d(interfaceC0491A, "null cannot be cast to non-null type com.facebook.react.ReactHost");
        return interfaceC0491A;
    }

    public static /* synthetic */ InterfaceC0491A e(Context context, N n3, JSRuntimeFactory jSRuntimeFactory, int i3, Object obj) {
        if ((i3 & 4) != 0) {
            jSRuntimeFactory = null;
        }
        return b(context, n3, jSRuntimeFactory);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final r g(Exception exc) throws Exception {
        D2.h.f(exc, "it");
        throw exc;
    }
}
