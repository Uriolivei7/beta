package com.facebook.react.fabric;

import com.facebook.jni.HybridClassBase;
import com.facebook.react.bridge.NativeMap;
import kotlin.jvm.internal.DefaultConstructorMarker;
import n1.InterfaceC0618a;
import p1.InterfaceC0637b;

/* JADX INFO: loaded from: classes.dex */
public class SurfaceHandlerBinding extends HybridClassBase implements InterfaceC0637b {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final a f6813b = new a(null);

    private static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    static {
        c.a();
    }

    public SurfaceHandlerBinding(String str) {
        D2.h.f(str, "moduleName");
        initHybrid(0, str);
    }

    private final native String _getModuleName();

    private final native int _getSurfaceId();

    private final native boolean _isRunning();

    private final native void initHybrid(int i3, String str);

    private final native void setDisplayMode(int i3);

    private final native void setLayoutConstraintsNative(float f3, float f4, float f5, float f6, float f7, float f8, boolean z3, boolean z4, float f9);

    @Override // p1.InterfaceC0637b
    public String a() {
        return _getModuleName();
    }

    @Override // p1.InterfaceC0637b
    public void c(int i3, int i4, int i5, int i6, boolean z3, boolean z4, float f3) {
        InterfaceC0618a.C0137a c0137a = InterfaceC0618a.f9849a;
        setLayoutConstraintsNative(c0137a.b(i3) / f3, c0137a.a(i3) / f3, c0137a.b(i4) / f3, c0137a.a(i4) / f3, i5 / f3, i6 / f3, z3, z4, f3);
    }

    @Override // p1.InterfaceC0637b
    public int getSurfaceId() {
        return _getSurfaceId();
    }

    @Override // p1.InterfaceC0637b
    public void h(boolean z3) {
        setDisplayMode(!z3 ? 1 : 0);
    }

    @Override // p1.InterfaceC0637b
    public boolean isRunning() {
        return _isRunning();
    }

    @Override // p1.InterfaceC0637b
    public native void setProps(NativeMap nativeMap);
}
