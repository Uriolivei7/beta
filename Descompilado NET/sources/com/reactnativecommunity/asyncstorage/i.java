package com.reactnativecommunity.asyncstorage;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.model.ReactModuleInfo;
import d1.b0;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import v1.InterfaceC0756a;
import w1.InterfaceC0763a;

/* JADX INFO: loaded from: classes.dex */
public class i extends b0 {

    class a implements InterfaceC0763a {
        a() {
        }

        @Override // w1.InterfaceC0763a
        public Map a() {
            HashMap map = new HashMap();
            Class cls = new Class[]{AsyncStorageModule.class}[0];
            InterfaceC0756a interfaceC0756a = (InterfaceC0756a) cls.getAnnotation(InterfaceC0756a.class);
            map.put(interfaceC0756a.name(), new ReactModuleInfo(interfaceC0756a.name(), cls.getName(), interfaceC0756a.canOverrideExistingModule(), interfaceC0756a.needsEagerInit(), interfaceC0756a.hasConstants(), interfaceC0756a.isCxxModule(), true));
            return map;
        }
    }

    @Override // d1.AbstractC0493a, d1.O
    public List f(ReactApplicationContext reactApplicationContext) {
        return Collections.emptyList();
    }

    @Override // d1.AbstractC0493a
    public NativeModule g(String str, ReactApplicationContext reactApplicationContext) {
        str.hashCode();
        if (str.equals("RNCAsyncStorage")) {
            return new AsyncStorageModule(reactApplicationContext);
        }
        return null;
    }

    @Override // d1.AbstractC0493a
    public InterfaceC0763a i() {
        try {
            return (InterfaceC0763a) Class.forName("com.reactnativecommunity.asyncstorage.AsyncStoragePackage$$ReactModuleInfoProvider").newInstance();
        } catch (ClassNotFoundException unused) {
            return new a();
        } catch (IllegalAccessException e4) {
            e = e4;
            throw new RuntimeException("No ReactModuleInfoProvider for com.reactnativecommunity.asyncstorage.AsyncStoragePackage$$ReactModuleInfoProvider", e);
        } catch (InstantiationException e5) {
            e = e5;
            throw new RuntimeException("No ReactModuleInfoProvider for com.reactnativecommunity.asyncstorage.AsyncStoragePackage$$ReactModuleInfoProvider", e);
        }
    }
}
