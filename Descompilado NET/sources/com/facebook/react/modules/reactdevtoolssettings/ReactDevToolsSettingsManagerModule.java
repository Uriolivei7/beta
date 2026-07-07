package com.facebook.react.modules.reactdevtoolssettings;

import D2.h;
import android.content.SharedPreferences;
import com.facebook.fbreact.specs.NativeReactDevToolsSettingsManagerSpec;
import com.facebook.react.bridge.ReactApplicationContext;
import kotlin.jvm.internal.DefaultConstructorMarker;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = "ReactDevToolsSettingsManager")
public final class ReactDevToolsSettingsManagerModule extends NativeReactDevToolsSettingsManagerSpec {
    public static final a Companion = new a(null);
    private static final String KEY_HOOK_SETTINGS = "HookSettings";
    public static final String NAME = "ReactDevToolsSettingsManager";
    private static final String SHARED_PREFERENCES_PREFIX = "ReactNative__DevToolsSettings";
    private final SharedPreferences sharedPreferences;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ReactDevToolsSettingsManagerModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        h.f(reactApplicationContext, "reactContext");
        SharedPreferences sharedPreferences = reactApplicationContext.getSharedPreferences(SHARED_PREFERENCES_PREFIX, 0);
        h.e(sharedPreferences, "getSharedPreferences(...)");
        this.sharedPreferences = sharedPreferences;
    }

    @Override // com.facebook.fbreact.specs.NativeReactDevToolsSettingsManagerSpec
    public String getGlobalHookSettings() {
        return this.sharedPreferences.getString(KEY_HOOK_SETTINGS, null);
    }

    @Override // com.facebook.fbreact.specs.NativeReactDevToolsSettingsManagerSpec
    public void setGlobalHookSettings(String str) {
        h.f(str, "settings");
        this.sharedPreferences.edit().putString(KEY_HOOK_SETTINGS, str).apply();
    }
}
