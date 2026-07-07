package com.facebook.react.uimanager;

import android.widget.ImageView;
import e1.C0527d;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
abstract class J0 {
    static Map a() {
        return C0527d.a().b("topChange", C0527d.d("phasedRegistrationNames", C0527d.e("bubbled", "onChange", "captured", "onChangeCapture"))).b("topSelect", C0527d.d("phasedRegistrationNames", C0527d.e("bubbled", "onSelect", "captured", "onSelectCapture"))).b(P1.s.b(P1.s.f1702d), C0527d.d("phasedRegistrationNames", C0527d.e("bubbled", "onTouchStart", "captured", "onTouchStartCapture"))).b(P1.s.b(P1.s.f1704f), C0527d.d("phasedRegistrationNames", C0527d.e("bubbled", "onTouchMove", "captured", "onTouchMoveCapture"))).b(P1.s.b(P1.s.f1703e), C0527d.d("phasedRegistrationNames", C0527d.e("bubbled", "onTouchEnd", "captured", "onTouchEndCapture"))).b(P1.s.b(P1.s.f1705g), C0527d.d("phasedRegistrationNames", C0527d.e("bubbled", "onTouchCancel", "captured", "onTouchCancelCapture"))).a();
    }

    public static Map b() {
        HashMap mapB = C0527d.b();
        mapB.put("UIView", C0527d.d("ContentMode", C0527d.f("ScaleAspectFit", Integer.valueOf(ImageView.ScaleType.FIT_CENTER.ordinal()), "ScaleAspectFill", Integer.valueOf(ImageView.ScaleType.CENTER_CROP.ordinal()), "ScaleAspectCenter", Integer.valueOf(ImageView.ScaleType.CENTER_INSIDE.ordinal()))));
        mapB.put("StyleConstants", C0527d.d("PointerEventsValues", C0527d.g("none", Integer.valueOf(EnumC0431g0.f7479c.ordinal()), "boxNone", Integer.valueOf(EnumC0431g0.f7480d.ordinal()), "boxOnly", Integer.valueOf(EnumC0431g0.f7481e.ordinal()), "unspecified", Integer.valueOf(EnumC0431g0.f7482f.ordinal()))));
        mapB.put("AccessibilityEventTypes", C0527d.f("typeWindowStateChanged", 32, "typeViewFocused", 8, "typeViewClicked", 1));
        return mapB;
    }

    static Map c() {
        return C0527d.a().b("topContentSizeChange", C0527d.d("registrationName", "onContentSizeChange")).b("topLayout", C0527d.d("registrationName", "onLayout")).b("topLoadingError", C0527d.d("registrationName", "onLoadingError")).b("topLoadingFinish", C0527d.d("registrationName", "onLoadingFinish")).b("topLoadingStart", C0527d.d("registrationName", "onLoadingStart")).b("topSelectionChange", C0527d.d("registrationName", "onSelectionChange")).b("topMessage", C0527d.d("registrationName", "onMessage")).b("topScrollBeginDrag", C0527d.d("registrationName", "onScrollBeginDrag")).b("topScrollEndDrag", C0527d.d("registrationName", "onScrollEndDrag")).b("topScroll", C0527d.d("registrationName", "onScroll")).b("topMomentumScrollBegin", C0527d.d("registrationName", "onMomentumScrollBegin")).b("topMomentumScrollEnd", C0527d.d("registrationName", "onMomentumScrollEnd")).a();
    }
}
