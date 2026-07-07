package com.facebook.react.uimanager;

import android.view.View;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.X0;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class R0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final R0 f7369a = new R0();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final Map f7370b = new HashMap();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final Map f7371c = new HashMap();

    private static final class a implements e {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final Map f7372a;

        public a(Class cls) {
            D2.h.f(cls, "shadowNodeClass");
            Map mapH = X0.h(cls);
            D2.h.e(mapH, "getNativePropSettersForShadowNodeClass(...)");
            this.f7372a = mapH;
        }

        @Override // com.facebook.react.uimanager.R0.d
        public void b(Map map) {
            D2.h.f(map, "props");
            for (X0.m mVar : this.f7372a.values()) {
                map.put(mVar.a(), mVar.b());
            }
        }

        @Override // com.facebook.react.uimanager.R0.e
        public void c(InterfaceC0451q0 interfaceC0451q0, String str, Object obj) {
            D2.h.f(interfaceC0451q0, "node");
            D2.h.f(str, "name");
            X0.m mVar = (X0.m) this.f7372a.get(str);
            if (mVar != null) {
                mVar.d(interfaceC0451q0, obj);
            }
        }
    }

    private static final class b implements f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final Map f7373a;

        public b(Class<? extends ViewManager<View, ?>> cls) {
            D2.h.f(cls, "viewManagerClass");
            Map mapI = X0.i(cls);
            D2.h.e(mapI, "getNativePropSettersForViewManagerClass(...)");
            this.f7373a = mapI;
        }

        @Override // com.facebook.react.uimanager.R0.f
        public void a(ViewManager viewManager, View view, String str, Object obj) {
            D2.h.f(viewManager, "manager");
            D2.h.f(view, "view");
            D2.h.f(str, "name");
            X0.m mVar = (X0.m) this.f7373a.get(str);
            if (mVar != null) {
                mVar.e(viewManager, view, obj);
            }
        }

        @Override // com.facebook.react.uimanager.R0.d
        public void b(Map map) {
            D2.h.f(map, "props");
            for (X0.m mVar : this.f7373a.values()) {
                map.put(mVar.a(), mVar.b());
            }
        }
    }

    public static final class c implements Q0 {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final ViewManager f7374a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final f f7375b;

        public c(ViewManager<View, ?> viewManager) {
            D2.h.f(viewManager, "manager");
            this.f7374a = viewManager;
            this.f7375b = R0.f7369a.d(viewManager.getClass());
        }

        @Override // com.facebook.react.uimanager.Q0
        public void a(View view, String str, ReadableArray readableArray) {
            D2.h.f(view, "view");
            D2.h.f(str, "commandName");
        }

        @Override // com.facebook.react.uimanager.Q0
        public void b(View view, String str, Object obj) {
            D2.h.f(view, "view");
            D2.h.f(str, "propName");
            this.f7375b.a(this.f7374a, view, str, obj);
        }
    }

    public interface d {
        void b(Map map);
    }

    public interface e extends d {
        void c(InterfaceC0451q0 interfaceC0451q0, String str, Object obj);
    }

    public interface f extends d {
        void a(ViewManager viewManager, View view, String str, Object obj);
    }

    private R0() {
    }

    public static final void b() {
        X0.b();
        f7370b.clear();
        f7371c.clear();
    }

    private final Object c(Class cls) {
        String name = cls.getName();
        try {
            return Class.forName(name + "$$PropsSetter").newInstance();
        } catch (ClassNotFoundException unused) {
            Y.a.I("ViewManagerPropertyUpdater", "Could not find generated setter for " + cls);
            return null;
        } catch (IllegalAccessException e4) {
            throw new RuntimeException("Unable to instantiate methods getter for " + name, e4);
        } catch (InstantiationException e5) {
            throw new RuntimeException("Unable to instantiate methods getter for " + name, e5);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final f d(Class cls) {
        Map map = f7370b;
        f bVar = (f) map.get(cls);
        if (bVar == null) {
            bVar = (f) c(cls);
            if (bVar == null) {
                bVar = new b(cls);
            }
            map.put(cls, bVar);
        }
        return bVar;
    }

    private final e e(Class cls) {
        Map map = f7371c;
        e aVar = (e) map.get(cls);
        if (aVar == null) {
            aVar = (e) c(cls);
            if (aVar == null) {
                D2.h.d(cls, "null cannot be cast to non-null type java.lang.Class<kotlin.Nothing>");
                aVar = new a(cls);
            }
            map.put(cls, aVar);
        }
        return aVar;
    }

    public static final Map f(Class cls, Class cls2) {
        D2.h.f(cls, "viewManagerTopClass");
        D2.h.f(cls2, "shadowNodeTopClass");
        HashMap map = new HashMap();
        R0 r02 = f7369a;
        r02.d(cls).b(map);
        r02.e(cls2).b(map);
        return map;
    }

    public static final void g(InterfaceC0451q0 interfaceC0451q0, C0454s0 c0454s0) {
        D2.h.f(interfaceC0451q0, "node");
        D2.h.f(c0454s0, "props");
        e eVarE = f7369a.e(interfaceC0451q0.getClass());
        Iterator<Map.Entry<String, Object>> entryIterator = c0454s0.f7630a.getEntryIterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, Object> next = entryIterator.next();
            eVarE.c(interfaceC0451q0, next.getKey(), next.getValue());
        }
    }
}
