package com.facebook.react.modules.appearance;

import D2.h;
import android.content.Context;
import androidx.appcompat.app.f;
import com.facebook.fbreact.specs.NativeAppearanceSpec;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.appearance.AppearanceModule;
import kotlin.jvm.internal.DefaultConstructorMarker;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = "Appearance")
public final class AppearanceModule extends NativeAppearanceSpec {
    private static final String APPEARANCE_CHANGED_EVENT_NAME = "appearanceChanged";
    public static final a Companion = new a(null);
    public static final String NAME = "Appearance";
    private String lastEmittedColorScheme;
    private final b overrideColorScheme;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public interface b {
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public AppearanceModule(ReactApplicationContext reactApplicationContext) {
        this(reactApplicationContext, null, 2, 0 == true ? 1 : 0);
        h.f(reactApplicationContext, "reactContext");
    }

    private final String colorSchemeForCurrentConfiguration(Context context) {
        int i3 = context.getResources().getConfiguration().uiMode & 48;
        return (i3 == 16 || i3 != 32) ? "light" : "dark";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void setColorScheme$lambda$0(String str) {
        int iHashCode = str.hashCode();
        if (iHashCode == -1626174665) {
            if (str.equals("unspecified")) {
                f.M(-1);
            }
        } else if (iHashCode == 3075958) {
            if (str.equals("dark")) {
                f.M(2);
            }
        } else if (iHashCode == 102970646 && str.equals("light")) {
            f.M(1);
        }
    }

    @Override // com.facebook.fbreact.specs.NativeAppearanceSpec
    public void addListener(String str) {
        h.f(str, "eventName");
    }

    public final void emitAppearanceChanged(String str) {
        h.f(str, "colorScheme");
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putString("colorScheme", str);
        ReactApplicationContext reactApplicationContextIfActiveOrWarn = getReactApplicationContextIfActiveOrWarn();
        if (reactApplicationContextIfActiveOrWarn != null) {
            reactApplicationContextIfActiveOrWarn.emitDeviceEvent(APPEARANCE_CHANGED_EVENT_NAME, writableMapCreateMap);
        }
    }

    @Override // com.facebook.fbreact.specs.NativeAppearanceSpec
    public String getColorScheme() {
        Context currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            currentActivity = getReactApplicationContext();
        }
        h.c(currentActivity);
        return colorSchemeForCurrentConfiguration(currentActivity);
    }

    public final void onConfigurationChanged(Context context) {
        h.f(context, "currentContext");
        String strColorSchemeForCurrentConfiguration = colorSchemeForCurrentConfiguration(context);
        if (h.b(this.lastEmittedColorScheme, strColorSchemeForCurrentConfiguration)) {
            return;
        }
        this.lastEmittedColorScheme = strColorSchemeForCurrentConfiguration;
        emitAppearanceChanged(strColorSchemeForCurrentConfiguration);
    }

    @Override // com.facebook.fbreact.specs.NativeAppearanceSpec
    public void removeListeners(double d4) {
    }

    @Override // com.facebook.fbreact.specs.NativeAppearanceSpec
    public void setColorScheme(final String str) {
        h.f(str, "style");
        UiThreadUtil.runOnUiThread(new Runnable() { // from class: y1.a
            @Override // java.lang.Runnable
            public final void run() {
                AppearanceModule.setColorScheme$lambda$0(str);
            }
        });
    }

    public /* synthetic */ AppearanceModule(ReactApplicationContext reactApplicationContext, b bVar, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(reactApplicationContext, (i3 & 2) != 0 ? null : bVar);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AppearanceModule(ReactApplicationContext reactApplicationContext, b bVar) {
        super(reactApplicationContext);
        h.f(reactApplicationContext, "reactContext");
    }
}
