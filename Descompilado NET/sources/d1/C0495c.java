package d1;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMarker;
import com.facebook.react.bridge.ReactMarkerConstants;
import com.facebook.react.devsupport.LogBoxModule;
import com.facebook.react.module.model.ReactModuleInfo;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.modules.core.ExceptionsManagerModule;
import com.facebook.react.modules.core.HeadlessJsTaskSupportModule;
import com.facebook.react.modules.core.TimingModule;
import com.facebook.react.modules.debug.DevMenuModule;
import com.facebook.react.modules.debug.DevSettingsModule;
import com.facebook.react.modules.debug.SourceCodeModule;
import com.facebook.react.modules.deviceinfo.DeviceInfoModule;
import com.facebook.react.modules.systeminfo.AndroidInfoModule;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.V0;
import com.facebook.react.uimanager.ViewManager;
import d2.C0518a;
import e1.C0524a;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import v1.InterfaceC0756a;
import w1.InterfaceC0763a;

/* JADX INFO: renamed from: d1.c, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
class C0495c extends AbstractC0493a implements Q {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final J f9198a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final B1.a f9199b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final boolean f9200c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final int f9201d;

    /* JADX INFO: renamed from: d1.c$a */
    class a implements V0 {
        a() {
        }

        @Override // com.facebook.react.uimanager.V0
        public ViewManager a(String str) {
            return C0495c.this.f9198a.z(str);
        }

        @Override // com.facebook.react.uimanager.V0
        public Collection b() {
            return C0495c.this.f9198a.H();
        }
    }

    public C0495c(J j3, B1.a aVar, boolean z3, int i3) {
        this.f9198a = j3;
        this.f9199b = aVar;
        this.f9200c = z3;
        this.f9201d = i3;
    }

    private UIManagerModule m(ReactApplicationContext reactApplicationContext) {
        ReactMarker.logMarker(ReactMarkerConstants.CREATE_UI_MANAGER_MODULE_START);
        C0518a.c(0L, "createUIManagerModule");
        try {
            return this.f9200c ? new UIManagerModule(reactApplicationContext, new a(), this.f9201d) : new UIManagerModule(reactApplicationContext, (List<ViewManager>) this.f9198a.G(reactApplicationContext), this.f9201d);
        } finally {
            C0518a.i(0L);
            ReactMarker.logMarker(ReactMarkerConstants.CREATE_UI_MANAGER_MODULE_END);
        }
    }

    private InterfaceC0763a n() {
        Class[] clsArr = {AndroidInfoModule.class, DeviceEventManagerModule.class, DeviceInfoModule.class, DevMenuModule.class, DevSettingsModule.class, ExceptionsManagerModule.class, LogBoxModule.class, HeadlessJsTaskSupportModule.class, SourceCodeModule.class, TimingModule.class, UIManagerModule.class};
        final HashMap map = new HashMap();
        for (int i3 = 0; i3 < 11; i3++) {
            Class cls = clsArr[i3];
            InterfaceC0756a interfaceC0756a = (InterfaceC0756a) cls.getAnnotation(InterfaceC0756a.class);
            map.put(interfaceC0756a.name(), new ReactModuleInfo(interfaceC0756a.name(), cls.getName(), interfaceC0756a.canOverrideExistingModule(), interfaceC0756a.needsEagerInit(), interfaceC0756a.isCxxModule(), ReactModuleInfo.b(cls)));
        }
        return new InterfaceC0763a() { // from class: d1.b
            @Override // w1.InterfaceC0763a
            public final Map a() {
                return C0495c.o(map);
            }
        };
    }

    @Override // d1.Q
    public void b() {
        ReactMarker.logMarker(ReactMarkerConstants.PROCESS_CORE_REACT_PACKAGE_START);
    }

    @Override // d1.Q
    public void c() {
        ReactMarker.logMarker(ReactMarkerConstants.PROCESS_CORE_REACT_PACKAGE_END);
    }

    @Override // d1.AbstractC0493a
    public NativeModule g(String str, ReactApplicationContext reactApplicationContext) {
        str.hashCode();
        switch (str) {
            case "LogBox":
                return new LogBoxModule(reactApplicationContext, this.f9198a.D());
            case "Timing":
                return new TimingModule(reactApplicationContext, this.f9198a.D());
            case "DevSettings":
                return new DevSettingsModule(reactApplicationContext, this.f9198a.D());
            case "DeviceInfo":
                return new DeviceInfoModule(reactApplicationContext);
            case "DevMenu":
                return new DevMenuModule(reactApplicationContext, this.f9198a.D());
            case "DeviceEventManager":
                return new DeviceEventManagerModule(reactApplicationContext, this.f9199b);
            case "PlatformConstants":
                return new AndroidInfoModule(reactApplicationContext);
            case "ExceptionsManager":
                return new ExceptionsManagerModule(this.f9198a.D());
            case "SourceCode":
                return new SourceCodeModule(reactApplicationContext);
            case "HeadlessJsTaskSupport":
                return new HeadlessJsTaskSupportModule(reactApplicationContext);
            case "UIManager":
                return m(reactApplicationContext);
            default:
                throw new IllegalArgumentException("In CoreModulesPackage, could not find Native module for " + str);
        }
    }

    @Override // d1.AbstractC0493a
    public InterfaceC0763a i() {
        if (!C0524a.a()) {
            return n();
        }
        try {
            return (InterfaceC0763a) C0524a.b("com.facebook.react.CoreModulesPackage$$ReactModuleInfoProvider").newInstance();
        } catch (ClassNotFoundException unused) {
            return n();
        } catch (IllegalAccessException e4) {
            throw new RuntimeException("No ReactModuleInfoProvider for CoreModulesPackage$$ReactModuleInfoProvider", e4);
        } catch (InstantiationException e5) {
            throw new RuntimeException("No ReactModuleInfoProvider for CoreModulesPackage$$ReactModuleInfoProvider", e5);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Map o(Map map) {
        return map;
    }
}
