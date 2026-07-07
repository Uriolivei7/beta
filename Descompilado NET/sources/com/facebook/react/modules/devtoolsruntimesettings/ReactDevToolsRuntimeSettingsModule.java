package com.facebook.react.modules.devtoolsruntimesettings;

import D2.h;
import com.facebook.fbreact.specs.NativeReactDevToolsRuntimeSettingsModuleSpec;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import kotlin.jvm.internal.DefaultConstructorMarker;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = "ReactDevToolsRuntimeSettingsModule")
public final class ReactDevToolsRuntimeSettingsModule extends NativeReactDevToolsRuntimeSettingsModuleSpec {
    public static final String NAME = "ReactDevToolsRuntimeSettingsModule";
    public static final a Companion = new a(null);
    private static final com.facebook.react.modules.devtoolsruntimesettings.a settings = new com.facebook.react.modules.devtoolsruntimesettings.a();

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public ReactDevToolsRuntimeSettingsModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    @Override // com.facebook.fbreact.specs.NativeReactDevToolsRuntimeSettingsModuleSpec
    public WritableMap getReloadAndProfileConfig() {
        WritableMap writableMapCreateMap = Arguments.createMap();
        com.facebook.react.modules.devtoolsruntimesettings.a aVar = settings;
        writableMapCreateMap.putBoolean("shouldReloadAndProfile", aVar.b());
        writableMapCreateMap.putBoolean("recordChangeDescriptions", aVar.a());
        h.c(writableMapCreateMap);
        return writableMapCreateMap;
    }

    @Override // com.facebook.fbreact.specs.NativeReactDevToolsRuntimeSettingsModuleSpec
    public void setReloadAndProfileConfig(ReadableMap readableMap) {
        h.f(readableMap, "map");
        if (readableMap.hasKey("shouldReloadAndProfile")) {
            settings.d(readableMap.getBoolean("shouldReloadAndProfile"));
        }
        if (readableMap.hasKey("recordChangeDescriptions")) {
            settings.c(readableMap.getBoolean("recordChangeDescriptions"));
        }
    }
}
