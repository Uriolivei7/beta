package com.facebook.react.fabric.mounting.mountitems;

import com.facebook.react.views.image.ReactImageManager;
import com.facebook.react.views.modal.ReactModalHostManager;
import com.facebook.react.views.progressbar.ReactProgressBarViewManager;
import com.facebook.react.views.scroll.ReactScrollViewManager;
import com.facebook.react.views.text.ReactRawTextManager;
import com.facebook.react.views.text.ReactTextViewManager;
import com.facebook.react.views.view.ReactViewManager;
import java.util.Map;
import r2.n;
import s2.AbstractC0696D;

/* JADX INFO: loaded from: classes.dex */
public final class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final f f6856a = new f();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final Map f6857b = AbstractC0696D.h(n.a("View", ReactViewManager.REACT_CLASS), n.a("Image", ReactImageManager.REACT_CLASS), n.a("ScrollView", ReactScrollViewManager.REACT_CLASS), n.a("Slider", "RCTSlider"), n.a("ModalHostView", ReactModalHostManager.REACT_CLASS), n.a("Paragraph", ReactTextViewManager.REACT_CLASS), n.a("Text", ReactTextViewManager.REACT_CLASS), n.a("RawText", ReactRawTextManager.REACT_CLASS), n.a("ActivityIndicatorView", ReactProgressBarViewManager.REACT_CLASS), n.a("ShimmeringView", "RKShimmeringView"), n.a("TemplateView", "RCTTemplateView"), n.a("AxialGradientView", "RCTAxialGradientView"), n.a("Video", "RCTVideo"), n.a("Map", "RCTMap"), n.a("WebView", "RCTWebView"), n.a("Keyframes", "RCTKeyframes"), n.a("ImpressionTrackingView", "RCTImpressionTrackingView"));

    private f() {
    }

    public static final String a(String str) {
        D2.h.f(str, "componentName");
        String str2 = (String) f6857b.get(str);
        return str2 == null ? str : str2;
    }
}
