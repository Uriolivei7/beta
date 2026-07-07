package com.facebook.react.modules.i18nmanager;

import D2.h;
import com.facebook.fbreact.specs.NativeI18nManagerSpec;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.modules.i18nmanager.a;
import java.util.Locale;
import java.util.Map;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.n;
import s2.AbstractC0696D;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = "I18nManager")
public final class I18nManagerModule extends NativeI18nManagerSpec {
    public static final a Companion = new a(null);
    public static final String NAME = "I18nManager";

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public I18nManagerModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    @Override // com.facebook.fbreact.specs.NativeI18nManagerSpec
    public void allowRTL(boolean z3) {
        com.facebook.react.modules.i18nmanager.a aVarA = com.facebook.react.modules.i18nmanager.a.f6977a.a();
        ReactApplicationContext reactApplicationContext = getReactApplicationContext();
        h.e(reactApplicationContext, "getReactApplicationContext(...)");
        aVarA.b(reactApplicationContext, z3);
    }

    @Override // com.facebook.fbreact.specs.NativeI18nManagerSpec
    public void forceRTL(boolean z3) {
        com.facebook.react.modules.i18nmanager.a aVarA = com.facebook.react.modules.i18nmanager.a.f6977a.a();
        ReactApplicationContext reactApplicationContext = getReactApplicationContext();
        h.e(reactApplicationContext, "getReactApplicationContext(...)");
        aVarA.e(reactApplicationContext, z3);
    }

    @Override // com.facebook.fbreact.specs.NativeI18nManagerSpec
    public Map<String, Object> getTypedExportedConstants() {
        ReactApplicationContext reactApplicationContext = getReactApplicationContext();
        Locale locale = reactApplicationContext.getResources().getConfiguration().getLocales().get(0);
        a.C0108a c0108a = com.facebook.react.modules.i18nmanager.a.f6977a;
        com.facebook.react.modules.i18nmanager.a aVarA = c0108a.a();
        h.c(reactApplicationContext);
        return AbstractC0696D.h(n.a("isRTL", Boolean.valueOf(aVarA.i(reactApplicationContext))), n.a("doLeftAndRightSwapInRTL", Boolean.valueOf(c0108a.a().d(reactApplicationContext))), n.a("localeIdentifier", locale.toString()));
    }

    @Override // com.facebook.fbreact.specs.NativeI18nManagerSpec
    public void swapLeftAndRightInRTL(boolean z3) {
        com.facebook.react.modules.i18nmanager.a aVarA = com.facebook.react.modules.i18nmanager.a.f6977a.a();
        ReactApplicationContext reactApplicationContext = getReactApplicationContext();
        h.e(reactApplicationContext, "getReactApplicationContext(...)");
        aVarA.m(reactApplicationContext, z3);
    }
}
