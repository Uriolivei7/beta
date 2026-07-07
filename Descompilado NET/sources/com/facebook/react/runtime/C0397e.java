package com.facebook.react.runtime;

import a1.C0210a;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.devsupport.LogBoxModule;
import com.facebook.react.module.model.ReactModuleInfo;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.modules.core.ExceptionsManagerModule;
import com.facebook.react.modules.debug.DevMenuModule;
import com.facebook.react.modules.debug.DevSettingsModule;
import com.facebook.react.modules.debug.SourceCodeModule;
import com.facebook.react.modules.deviceinfo.DeviceInfoModule;
import com.facebook.react.modules.systeminfo.AndroidInfoModule;
import d1.AbstractC0493a;
import e1.C0524a;
import java.util.HashMap;
import java.util.Map;
import v1.InterfaceC0756a;
import w1.InterfaceC0763a;

/* JADX INFO: renamed from: com.facebook.react.runtime.e, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
class C0397e extends AbstractC0493a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final k1.e f7174a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final B1.a f7175b;

    public C0397e(k1.e eVar, B1.a aVar) {
        this.f7174a = eVar;
        this.f7175b = aVar;
    }

    private InterfaceC0763a l() {
        Class[] clsArr = {AndroidInfoModule.class, DeviceInfoModule.class, SourceCodeModule.class, DevMenuModule.class, DevSettingsModule.class, DeviceEventManagerModule.class, LogBoxModule.class, ExceptionsManagerModule.class};
        final HashMap map = new HashMap();
        for (int i3 = 0; i3 < 8; i3++) {
            Class cls = clsArr[i3];
            InterfaceC0756a interfaceC0756a = (InterfaceC0756a) cls.getAnnotation(InterfaceC0756a.class);
            if (interfaceC0756a != null) {
                map.put(interfaceC0756a.name(), new ReactModuleInfo(interfaceC0756a.name(), cls.getName(), interfaceC0756a.canOverrideExistingModule(), interfaceC0756a.needsEagerInit(), interfaceC0756a.isCxxModule(), ReactModuleInfo.b(cls)));
            }
        }
        return new InterfaceC0763a() { // from class: com.facebook.react.runtime.d
            @Override // w1.InterfaceC0763a
            public final Map a() {
                return C0397e.m(map);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Map m(Map map) {
        return map;
    }

    @Override // d1.AbstractC0493a
    public NativeModule g(String str, ReactApplicationContext reactApplicationContext) {
        str.hashCode();
        switch (str) {
            case "LogBox":
                return new LogBoxModule(reactApplicationContext, this.f7174a);
            case "DevSettings":
                return new DevSettingsModule(reactApplicationContext, this.f7174a);
            case "DeviceInfo":
                return new DeviceInfoModule(reactApplicationContext);
            case "DevMenu":
                return new DevMenuModule(reactApplicationContext, this.f7174a);
            case "DeviceEventManager":
                return new DeviceEventManagerModule(reactApplicationContext, this.f7175b);
            case "PlatformConstants":
                return new AndroidInfoModule(reactApplicationContext);
            case "ExceptionsManager":
                return new ExceptionsManagerModule(this.f7174a);
            case "SourceCode":
                return new SourceCodeModule(reactApplicationContext);
            default:
                return null;
        }
    }

    @Override // d1.AbstractC0493a
    public InterfaceC0763a i() {
        if (!C0524a.a()) {
            return l();
        }
        try {
            return (InterfaceC0763a) ((Class) C0210a.c(C0524a.b(C0397e.class.getName() + "$$ReactModuleInfoProvider"))).newInstance();
        } catch (ClassNotFoundException unused) {
            return l();
        } catch (IllegalAccessException e4) {
            throw new RuntimeException("No ReactModuleInfoProvider for " + C0397e.class.getName() + "$$ReactModuleInfoProvider", e4);
        } catch (InstantiationException e5) {
            throw new RuntimeException("No ReactModuleInfoProvider for " + C0397e.class.getName() + "$$ReactModuleInfoProvider", e5);
        }
    }
}
