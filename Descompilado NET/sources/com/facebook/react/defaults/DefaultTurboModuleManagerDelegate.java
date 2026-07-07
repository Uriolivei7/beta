package com.facebook.react.defaults;

import C2.l;
import com.facebook.jni.HybridData;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.defaults.DefaultTurboModuleManagerDelegate;
import d1.O;
import d1.V;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultTurboModuleManagerDelegate extends V {
    private static final b Companion = new b(null);

    public static final class a extends V.a {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final List f6560c = new ArrayList();

        /* JADX INFO: Access modifiers changed from: private */
        public static final List g(l lVar, ReactApplicationContext reactApplicationContext) {
            D2.h.f(reactApplicationContext, "context");
            return AbstractC0717n.b(lVar.d(reactApplicationContext));
        }

        public final a f(final l lVar) {
            D2.h.f(lVar, "provider");
            this.f6560c.add(new l() { // from class: com.facebook.react.defaults.i
                @Override // C2.l
                public final Object d(Object obj) {
                    return DefaultTurboModuleManagerDelegate.a.g(lVar, (ReactApplicationContext) obj);
                }
            });
            return this;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // d1.V.a
        /* JADX INFO: renamed from: h, reason: merged with bridge method [inline-methods] */
        public DefaultTurboModuleManagerDelegate b(ReactApplicationContext reactApplicationContext, List list) {
            D2.h.f(reactApplicationContext, "context");
            D2.h.f(list, "packages");
            List list2 = this.f6560c;
            ArrayList arrayList = new ArrayList();
            Iterator it = list2.iterator();
            while (it.hasNext()) {
                AbstractC0717n.t(arrayList, (Iterable) ((l) it.next()).d(reactApplicationContext));
            }
            return new DefaultTurboModuleManagerDelegate(reactApplicationContext, list, arrayList, null);
        }
    }

    private static final class b {
        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final HybridData initHybrid(List<Object> list) {
            return DefaultTurboModuleManagerDelegate.initHybrid(list);
        }

        private b() {
        }
    }

    static {
        h.f6573a.a();
    }

    public /* synthetic */ DefaultTurboModuleManagerDelegate(ReactApplicationContext reactApplicationContext, List list, List list2, DefaultConstructorMarker defaultConstructorMarker) {
        this(reactApplicationContext, list, list2);
    }

    public static final native HybridData initHybrid(List<Object> list);

    @Override // com.facebook.react.internal.turbomodule.core.TurboModuleManagerDelegate
    protected HybridData initHybrid() {
        throw new UnsupportedOperationException("DefaultTurboModuleManagerDelegate.initHybrid() must never be called!");
    }

    private DefaultTurboModuleManagerDelegate(ReactApplicationContext reactApplicationContext, List<? extends O> list, List<Object> list2) {
        super(reactApplicationContext, list, Companion.initHybrid(list2));
    }
}
