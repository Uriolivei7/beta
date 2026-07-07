package m2;

import com.facebook.react.bridge.ModuleSpec;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.model.ReactModuleInfo;
import com.facebook.react.uimanager.ViewManager;
import com.swmansion.gesturehandler.react.RNGestureHandlerButtonViewManager;
import com.swmansion.gesturehandler.react.RNGestureHandlerModule;
import com.swmansion.gesturehandler.react.RNGestureHandlerRootViewManager;
import d1.AbstractC0493a;
import d1.c0;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import javax.inject.Provider;
import kotlin.Lazy;
import r2.AbstractC0681d;
import r2.n;
import s2.AbstractC0696D;
import s2.AbstractC0717n;
import v1.InterfaceC0756a;
import w1.InterfaceC0763a;

/* JADX INFO: renamed from: m2.e, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0611e extends AbstractC0493a implements c0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Lazy f9839a = AbstractC0681d.a(new C2.a() { // from class: m2.a
        @Override // C2.a
        public final Object a() {
            return C0611e.r();
        }
    });

    /* JADX INFO: Access modifiers changed from: private */
    public static final Map o() {
        Annotation annotation = RNGestureHandlerModule.class.getAnnotation(InterfaceC0756a.class);
        D2.h.c(annotation);
        InterfaceC0756a interfaceC0756a = (InterfaceC0756a) annotation;
        String strName = interfaceC0756a.name();
        String name = RNGestureHandlerModule.class.getName();
        D2.h.e(name, "getName(...)");
        return AbstractC0696D.i(n.a("RNGestureHandlerModule", new ReactModuleInfo(strName, name, interfaceC0756a.canOverrideExistingModule(), interfaceC0756a.needsEagerInit(), interfaceC0756a.isCxxModule(), true)));
    }

    private final Map q() {
        return (Map) this.f9839a.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Map r() {
        return AbstractC0696D.h(n.a(RNGestureHandlerRootViewManager.REACT_CLASS, ModuleSpec.viewManagerSpec(new Provider() { // from class: m2.c
            @Override // javax.inject.Provider
            public final Object get() {
                return C0611e.s();
            }
        })), n.a(RNGestureHandlerButtonViewManager.REACT_CLASS, ModuleSpec.viewManagerSpec(new Provider() { // from class: m2.d
            @Override // javax.inject.Provider
            public final Object get() {
                return C0611e.t();
            }
        })));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final NativeModule s() {
        return new RNGestureHandlerRootViewManager();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final NativeModule t() {
        return new RNGestureHandlerButtonViewManager();
    }

    @Override // d1.c0
    public ViewManager a(ReactApplicationContext reactApplicationContext, String str) {
        Provider provider;
        D2.h.f(reactApplicationContext, "reactContext");
        D2.h.f(str, "viewManagerName");
        ModuleSpec moduleSpec = (ModuleSpec) q().get(str);
        NativeModule nativeModule = (moduleSpec == null || (provider = moduleSpec.getProvider()) == null) ? null : (NativeModule) provider.get();
        if (nativeModule instanceof ViewManager) {
            return (ViewManager) nativeModule;
        }
        return null;
    }

    @Override // d1.AbstractC0493a, d1.O
    public List f(ReactApplicationContext reactApplicationContext) {
        D2.h.f(reactApplicationContext, "reactContext");
        return AbstractC0717n.j(new RNGestureHandlerRootViewManager(), new RNGestureHandlerButtonViewManager());
    }

    @Override // d1.AbstractC0493a
    public NativeModule g(String str, ReactApplicationContext reactApplicationContext) {
        D2.h.f(str, "name");
        D2.h.f(reactApplicationContext, "reactContext");
        if (D2.h.b(str, "RNGestureHandlerModule")) {
            return new RNGestureHandlerModule(reactApplicationContext);
        }
        return null;
    }

    @Override // d1.AbstractC0493a
    public InterfaceC0763a i() throws InvocationTargetException {
        try {
            Object objNewInstance = Class.forName("com.swmansion.gesturehandler.RNGestureHandlerPackage$$ReactModuleInfoProvider").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            D2.h.d(objNewInstance, "null cannot be cast to non-null type com.facebook.react.module.model.ReactModuleInfoProvider");
            return (InterfaceC0763a) objNewInstance;
        } catch (ClassNotFoundException unused) {
            return new InterfaceC0763a() { // from class: m2.b
                @Override // w1.InterfaceC0763a
                public final Map a() {
                    return C0611e.o();
                }
            };
        } catch (IllegalAccessException e4) {
            throw new RuntimeException("No ReactModuleInfoProvider for RNGestureHandlerPackage$$ReactModuleInfoProvider", e4);
        } catch (InstantiationException e5) {
            throw new RuntimeException("No ReactModuleInfoProvider for RNGestureHandlerPackage$$ReactModuleInfoProvider", e5);
        }
    }

    @Override // d1.AbstractC0493a
    protected List j(ReactApplicationContext reactApplicationContext) {
        D2.h.f(reactApplicationContext, "reactContext");
        return AbstractC0717n.g0(q().values());
    }

    @Override // d1.c0
    /* JADX INFO: renamed from: p, reason: merged with bridge method [inline-methods] */
    public List d(ReactApplicationContext reactApplicationContext) {
        D2.h.f(reactApplicationContext, "reactContext");
        return AbstractC0717n.e0(q().keySet());
    }
}
