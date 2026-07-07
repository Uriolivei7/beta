package com.facebook.react.modules.toast;

import D2.h;
import android.widget.Toast;
import com.facebook.fbreact.specs.NativeToastAndroidSpec;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.modules.toast.ToastModule;
import java.util.Map;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.n;
import s2.AbstractC0696D;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = "ToastAndroid")
public final class ToastModule extends NativeToastAndroidSpec {
    public static final a Companion = new a(null);
    private static final String DURATION_LONG_KEY = "LONG";
    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String GRAVITY_BOTTOM_KEY = "BOTTOM";
    private static final String GRAVITY_CENTER = "CENTER";
    private static final String GRAVITY_TOP_KEY = "TOP";
    public static final String NAME = "ToastAndroid";

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ToastModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        h.f(reactApplicationContext, "reactContext");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void show$lambda$0(ToastModule toastModule, String str, int i3) {
        Toast.makeText(toastModule.getReactApplicationContext(), str, i3).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void showWithGravity$lambda$1(ToastModule toastModule, String str, int i3, int i4) {
        Toast toastMakeText = Toast.makeText(toastModule.getReactApplicationContext(), str, i3);
        toastMakeText.setGravity(i4, 0, 0);
        toastMakeText.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void showWithGravityAndOffset$lambda$2(ToastModule toastModule, String str, int i3, int i4, int i5, int i6) {
        Toast toastMakeText = Toast.makeText(toastModule.getReactApplicationContext(), str, i3);
        toastMakeText.setGravity(i4, i5, i6);
        toastMakeText.show();
    }

    @Override // com.facebook.fbreact.specs.NativeToastAndroidSpec
    public Map<String, Object> getTypedExportedConstants() {
        return AbstractC0696D.i(n.a(DURATION_SHORT_KEY, 0), n.a(DURATION_LONG_KEY, 1), n.a(GRAVITY_TOP_KEY, 49), n.a(GRAVITY_BOTTOM_KEY, 81), n.a(GRAVITY_CENTER, 17));
    }

    @Override // com.facebook.fbreact.specs.NativeToastAndroidSpec
    public void show(final String str, double d4) {
        final int i3 = (int) d4;
        UiThreadUtil.runOnUiThread(new Runnable() { // from class: F1.c
            @Override // java.lang.Runnable
            public final void run() {
                ToastModule.show$lambda$0(this.f244b, str, i3);
            }
        });
    }

    @Override // com.facebook.fbreact.specs.NativeToastAndroidSpec
    public void showWithGravity(final String str, double d4, double d5) {
        final int i3 = (int) d4;
        final int i4 = (int) d5;
        UiThreadUtil.runOnUiThread(new Runnable() { // from class: F1.b
            @Override // java.lang.Runnable
            public final void run() {
                ToastModule.showWithGravity$lambda$1(this.f240b, str, i3, i4);
            }
        });
    }

    @Override // com.facebook.fbreact.specs.NativeToastAndroidSpec
    public void showWithGravityAndOffset(final String str, double d4, double d5, double d6, double d7) {
        final int i3 = (int) d4;
        final int i4 = (int) d5;
        final int i5 = (int) d6;
        final int i6 = (int) d7;
        UiThreadUtil.runOnUiThread(new Runnable() { // from class: F1.a
            @Override // java.lang.Runnable
            public final void run() {
                ToastModule.showWithGravityAndOffset$lambda$2(this.f234b, str, i3, i4, i5, i6);
            }
        });
    }
}
