package com.facebook.react.views.swiperefresh;

import U1.g;
import U1.h;
import android.view.View;
import androidx.swiperefreshlayout.widget.c;
import com.facebook.react.bridge.ColorPropConverter;
import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.uimanager.B0;
import com.facebook.react.uimanager.H0;
import com.facebook.react.uimanager.Q0;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.events.EventDispatcher;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.n;
import s2.AbstractC0696D;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = SwipeRefreshLayoutManager.REACT_CLASS)
public class SwipeRefreshLayoutManager extends ViewGroupManager<com.facebook.react.views.swiperefresh.a> implements h {
    public static final a Companion = new a(null);
    public static final String REACT_CLASS = "AndroidSwipeRefreshLayout";
    private final Q0 delegate = new g(this);

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void addEventEmitters$lambda$0(B0 b02, com.facebook.react.views.swiperefresh.a aVar) {
        EventDispatcher eventDispatcherC = H0.c(b02, aVar.getId());
        if (eventDispatcherC != null) {
            eventDispatcherC.b(new b(H0.f(aVar), aVar.getId()));
        }
    }

    @Override // com.facebook.react.uimanager.ViewManager
    protected Q0 getDelegate() {
        return this.delegate;
    }

    @Override // com.facebook.react.uimanager.BaseViewManager, com.facebook.react.uimanager.ViewManager
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        Map<String, Object> exportedCustomDirectEventTypeConstants = super.getExportedCustomDirectEventTypeConstants();
        if (exportedCustomDirectEventTypeConstants == null) {
            exportedCustomDirectEventTypeConstants = new HashMap<>();
        }
        exportedCustomDirectEventTypeConstants.putAll(AbstractC0696D.i(n.a("topRefresh", AbstractC0696D.i(n.a("registrationName", "onRefresh")))));
        return exportedCustomDirectEventTypeConstants;
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Map<String, Object> getExportedViewConstants() {
        return AbstractC0696D.i(n.a("SIZE", AbstractC0696D.i(n.a("DEFAULT", 1), n.a("LARGE", 0))));
    }

    @Override // com.facebook.react.uimanager.ViewManager, com.facebook.react.bridge.NativeModule
    public String getName() {
        return REACT_CLASS;
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager, com.facebook.react.uimanager.N
    public /* bridge */ /* synthetic */ void removeAllViews(View view) {
        super.removeAllViews(view);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.ViewManager
    public void addEventEmitters(final B0 b02, final com.facebook.react.views.swiperefresh.a aVar) {
        D2.h.f(b02, "reactContext");
        D2.h.f(aVar, "view");
        aVar.setOnRefreshListener(new c.j() { // from class: com.facebook.react.views.swiperefresh.c
            @Override // androidx.swiperefreshlayout.widget.c.j
            public final void a() {
                SwipeRefreshLayoutManager.addEventEmitters$lambda$0(b02, aVar);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.ViewManager
    public com.facebook.react.views.swiperefresh.a createViewInstance(B0 b02) {
        D2.h.f(b02, "reactContext");
        return new com.facebook.react.views.swiperefresh.a(b02);
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void receiveCommand(com.facebook.react.views.swiperefresh.a aVar, String str, ReadableArray readableArray) {
        D2.h.f(aVar, "root");
        D2.h.f(str, "commandId");
        if (!D2.h.b(str, "setNativeRefreshing") || readableArray == null) {
            return;
        }
        setRefreshing(aVar, readableArray.getBoolean(0));
    }

    @Override // U1.h
    @L1.a(customType = "ColorArray", name = "colors")
    public void setColors(com.facebook.react.views.swiperefresh.a aVar, ReadableArray readableArray) {
        D2.h.f(aVar, "view");
        if (readableArray == null) {
            aVar.setColorSchemeColors(new int[0]);
            return;
        }
        int size = readableArray.size();
        int[] iArr = new int[size];
        int size2 = readableArray.size();
        for (int i3 = 0; i3 < size2; i3++) {
            if (readableArray.getType(i3) == ReadableType.Map) {
                Integer color = ColorPropConverter.getColor(readableArray.getMap(i3), aVar.getContext());
                D2.h.e(color, "getColor(...)");
                iArr[i3] = color.intValue();
            } else {
                iArr[i3] = readableArray.getInt(i3);
            }
        }
        aVar.setColorSchemeColors(Arrays.copyOf(iArr, size));
    }

    @Override // U1.h
    @L1.a(defaultBoolean = true, name = "enabled")
    public void setEnabled(com.facebook.react.views.swiperefresh.a aVar, boolean z3) {
        D2.h.f(aVar, "view");
        aVar.setEnabled(z3);
    }

    @Override // U1.h
    public void setNativeRefreshing(com.facebook.react.views.swiperefresh.a aVar, boolean z3) {
        D2.h.f(aVar, "view");
        setRefreshing(aVar, z3);
    }

    @Override // U1.h
    @L1.a(customType = "Color", name = "progressBackgroundColor")
    public void setProgressBackgroundColor(com.facebook.react.views.swiperefresh.a aVar, Integer num) {
        D2.h.f(aVar, "view");
        aVar.setProgressBackgroundColorSchemeColor(num != null ? num.intValue() : 0);
    }

    @Override // U1.h
    @L1.a(defaultFloat = 0.0f, name = "progressViewOffset")
    public void setProgressViewOffset(com.facebook.react.views.swiperefresh.a aVar, float f3) {
        D2.h.f(aVar, "view");
        aVar.setProgressViewOffset(f3);
    }

    @Override // U1.h
    @L1.a(name = "refreshing")
    public void setRefreshing(com.facebook.react.views.swiperefresh.a aVar, boolean z3) {
        D2.h.f(aVar, "view");
        aVar.setRefreshing(z3);
    }

    public final void setSize(com.facebook.react.views.swiperefresh.a aVar, int i3) {
        D2.h.f(aVar, "view");
        aVar.setSize(i3);
    }

    @Override // U1.h
    public void setSize(com.facebook.react.views.swiperefresh.a aVar, String str) {
        D2.h.f(aVar, "view");
        if (str != null && !str.equals("default")) {
            if (str.equals("large")) {
                aVar.setSize(0);
                return;
            }
            throw new IllegalArgumentException("Size must be 'default' or 'large', received: " + str);
        }
        aVar.setSize(1);
    }

    @L1.a(name = "size")
    public final void setSize(com.facebook.react.views.swiperefresh.a aVar, Dynamic dynamic) {
        D2.h.f(aVar, "view");
        D2.h.f(dynamic, "size");
        if (dynamic.isNull()) {
            aVar.setSize(1);
        } else if (dynamic.getType() == ReadableType.Number) {
            aVar.setSize(dynamic.asInt());
        } else {
            if (dynamic.getType() != ReadableType.String) {
                throw new IllegalArgumentException("Size must be 'default' or 'large'");
            }
            setSize(aVar, dynamic.asString());
        }
    }
}
