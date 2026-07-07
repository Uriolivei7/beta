package m2;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.UIManager;
import com.facebook.react.fabric.FabricUIManager;
import com.facebook.react.uimanager.H0;

/* JADX INFO: renamed from: m2.g, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0613g {
    public static final void a(ReactContext reactContext, P1.d dVar) {
        D2.h.f(reactContext, "<this>");
        D2.h.f(dVar, "event");
        UIManager uIManagerG = H0.g(reactContext, 2);
        D2.h.d(uIManagerG, "null cannot be cast to non-null type com.facebook.react.fabric.FabricUIManager");
        ((FabricUIManager) uIManagerG).getEventDispatcher().b(dVar);
    }
}
