package com.reactnativecommunity.webview;

import android.view.View;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.B0;
import com.facebook.react.uimanager.Q0;
import com.facebook.react.uimanager.ViewGroupManager;
import e1.C0527d;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = "RNCWebView")
public class RNCWebViewManager extends ViewGroupManager<r> implements U1.p {
    private final Q0 mDelegate = new U1.o(this);
    private final m mRNCWebViewManagerImpl = new m(true);

    @Override // com.facebook.react.uimanager.ViewManager
    public Map<String, Integer> getCommandsMap() {
        return this.mRNCWebViewManagerImpl.g();
    }

    @Override // com.facebook.react.uimanager.ViewManager
    protected Q0 getDelegate() {
        return this.mDelegate;
    }

    @Override // com.facebook.react.uimanager.BaseViewManager, com.facebook.react.uimanager.ViewManager
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        Map<String, Object> exportedCustomDirectEventTypeConstants = super.getExportedCustomDirectEventTypeConstants();
        if (exportedCustomDirectEventTypeConstants == null) {
            exportedCustomDirectEventTypeConstants = C0527d.b();
        }
        exportedCustomDirectEventTypeConstants.put("topLoadingStart", C0527d.d("registrationName", "onLoadingStart"));
        exportedCustomDirectEventTypeConstants.put("topLoadingFinish", C0527d.d("registrationName", "onLoadingFinish"));
        exportedCustomDirectEventTypeConstants.put("topLoadingError", C0527d.d("registrationName", "onLoadingError"));
        exportedCustomDirectEventTypeConstants.put("topMessage", C0527d.d("registrationName", "onMessage"));
        exportedCustomDirectEventTypeConstants.put("topLoadingProgress", C0527d.d("registrationName", "onLoadingProgress"));
        exportedCustomDirectEventTypeConstants.put("topShouldStartLoadWithRequest", C0527d.d("registrationName", "onShouldStartLoadWithRequest"));
        exportedCustomDirectEventTypeConstants.put(com.facebook.react.views.scroll.l.b(com.facebook.react.views.scroll.l.f7890e), C0527d.d("registrationName", "onScroll"));
        exportedCustomDirectEventTypeConstants.put("topHttpError", C0527d.d("registrationName", "onHttpError"));
        exportedCustomDirectEventTypeConstants.put("topRenderProcessGone", C0527d.d("registrationName", "onRenderProcessGone"));
        exportedCustomDirectEventTypeConstants.put("topCustomMenuSelection", C0527d.d("registrationName", "onCustomMenuSelection"));
        exportedCustomDirectEventTypeConstants.put("topOpenWindow", C0527d.d("registrationName", "onOpenWindow"));
        return exportedCustomDirectEventTypeConstants;
    }

    @Override // com.facebook.react.uimanager.ViewManager, com.facebook.react.bridge.NativeModule
    public String getName() {
        return "RNCWebView";
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager, com.facebook.react.uimanager.N
    public /* bridge */ /* synthetic */ void removeAllViews(View view) {
        super.removeAllViews(view);
    }

    @Override // U1.p
    public void setAllowingReadAccessToURL(r rVar, String str) {
    }

    @Override // U1.p
    public void setAllowsAirPlayForMediaPlayback(r rVar, boolean z3) {
    }

    @Override // U1.p
    public void setAllowsBackForwardNavigationGestures(r rVar, boolean z3) {
    }

    @Override // U1.p
    public void setAllowsInlineMediaPlayback(r rVar, boolean z3) {
    }

    @Override // U1.p
    public void setAllowsLinkPreview(r rVar, boolean z3) {
    }

    @Override // U1.p
    public void setAllowsPictureInPictureMediaPlayback(r rVar, boolean z3) {
    }

    @Override // U1.p
    public void setAutoManageStatusBarEnabled(r rVar, boolean z3) {
    }

    @Override // U1.p
    public void setAutomaticallyAdjustContentInsets(r rVar, boolean z3) {
    }

    @Override // U1.p
    public void setBounces(r rVar, boolean z3) {
    }

    @Override // U1.p
    public void setContentInset(r rVar, ReadableMap readableMap) {
    }

    @Override // U1.p
    public void setContentInsetAdjustmentBehavior(r rVar, String str) {
    }

    @Override // U1.p
    public void setContentMode(r rVar, String str) {
    }

    @Override // U1.p
    public void setDataDetectorTypes(r rVar, ReadableArray readableArray) {
    }

    @Override // U1.p
    public void setDecelerationRate(r rVar, double d4) {
    }

    @Override // U1.p
    public void setDirectionalLockEnabled(r rVar, boolean z3) {
    }

    @Override // U1.p
    public void setEnableApplePay(r rVar, boolean z3) {
    }

    @Override // U1.p
    public void setFraudulentWebsiteWarningEnabled(r rVar, boolean z3) {
    }

    @Override // U1.p
    public void setHasOnFileDownload(r rVar, boolean z3) {
    }

    @Override // U1.p
    public void setHideKeyboardAccessoryView(r rVar, boolean z3) {
    }

    @Override // U1.p
    public void setKeyboardDisplayRequiresUserAction(r rVar, boolean z3) {
    }

    @Override // U1.p
    public void setLimitsNavigationsToAppBoundDomains(r rVar, boolean z3) {
    }

    @Override // U1.p
    public void setMediaCapturePermissionGrantType(r rVar, String str) {
    }

    @Override // U1.p
    public void setPagingEnabled(r rVar, boolean z3) {
    }

    @Override // U1.p
    public void setPullToRefreshEnabled(r rVar, boolean z3) {
    }

    @Override // U1.p
    public void setRefreshControlLightMode(r rVar, boolean z3) {
    }

    @Override // U1.p
    public void setScrollEnabled(r rVar, boolean z3) {
    }

    @Override // U1.p
    public void setSharedCookiesEnabled(r rVar, boolean z3) {
    }

    @Override // U1.p
    @L1.a(name = "suppressMenuItems ")
    public void setSuppressMenuItems(r rVar, ReadableArray readableArray) {
    }

    @Override // U1.p
    public void setTextInteractionEnabled(r rVar, boolean z3) {
    }

    @Override // U1.p
    public void setUseSharedProcessPool(r rVar, boolean z3) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.ViewManager
    public void addEventEmitters(B0 b02, r rVar) {
        rVar.getWebView().setWebViewClient(new i());
    }

    @Override // U1.p
    public void clearCache(r rVar, boolean z3) {
        rVar.getWebView().clearCache(z3);
    }

    @Override // U1.p
    public void clearFormData(r rVar) {
        rVar.getWebView().clearFormData();
    }

    @Override // U1.p
    public void clearHistory(r rVar) {
        rVar.getWebView().clearHistory();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.ViewManager
    public r createViewInstance(B0 b02) {
        return this.mRNCWebViewManagerImpl.d(b02);
    }

    @Override // U1.p
    public void goBack(r rVar) {
        rVar.getWebView().goBack();
    }

    @Override // U1.p
    public void goForward(r rVar) {
        rVar.getWebView().goForward();
    }

    @Override // U1.p
    public void injectJavaScript(r rVar, String str) {
        rVar.getWebView().h(str);
    }

    @Override // U1.p
    public void loadUrl(r rVar, String str) {
        rVar.getWebView().loadUrl(str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.BaseViewManager, com.facebook.react.uimanager.ViewManager
    public void onAfterUpdateTransaction(r rVar) {
        super.onAfterUpdateTransaction(rVar);
        this.mRNCWebViewManagerImpl.l(rVar);
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void onDropViewInstance(r rVar) {
        this.mRNCWebViewManagerImpl.m(rVar);
        super.onDropViewInstance(rVar);
    }

    @Override // U1.p
    public void postMessage(r rVar, String str) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("data", str);
            rVar.getWebView().h("(function () {var event;var data = " + jSONObject.toString() + ";try {event = new MessageEvent('message', data);} catch (e) {event = document.createEvent('MessageEvent');event.initMessageEvent('message', true, true, data.data, data.origin, data.lastEventId, data.source);}document.dispatchEvent(event);})();");
        } catch (JSONException e4) {
            throw new RuntimeException(e4);
        }
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void receiveCommand(r rVar, String str, ReadableArray readableArray) {
        super.receiveCommand(rVar, str, readableArray);
    }

    @Override // U1.p
    public void reload(r rVar) {
        rVar.getWebView().reload();
    }

    @Override // U1.p
    public void requestFocus(r rVar) {
        rVar.requestFocus();
    }

    @Override // U1.p
    @L1.a(name = "allowFileAccess")
    public void setAllowFileAccess(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.n(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "allowFileAccessFromFileURLs")
    public void setAllowFileAccessFromFileURLs(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.o(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "allowUniversalAccessFromFileURLs")
    public void setAllowUniversalAccessFromFileURLs(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.p(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "allowsFullscreenVideo")
    public void setAllowsFullscreenVideo(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.q(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "allowsProtectedMedia")
    public void setAllowsProtectedMedia(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.r(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "androidLayerType")
    public void setAndroidLayerType(r rVar, String str) {
        this.mRNCWebViewManagerImpl.s(rVar, str);
    }

    @Override // U1.p
    @L1.a(name = "applicationNameForUserAgent")
    public void setApplicationNameForUserAgent(r rVar, String str) {
        this.mRNCWebViewManagerImpl.t(rVar, str);
    }

    @Override // U1.p
    @L1.a(name = "basicAuthCredential")
    public void setBasicAuthCredential(r rVar, ReadableMap readableMap) {
        this.mRNCWebViewManagerImpl.u(rVar, readableMap);
    }

    @Override // U1.p
    @L1.a(name = "cacheEnabled")
    public void setCacheEnabled(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.v(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "cacheMode")
    public void setCacheMode(r rVar, String str) {
        this.mRNCWebViewManagerImpl.w(rVar, str);
    }

    @Override // U1.p
    @L1.a(name = "domStorageEnabled")
    public void setDomStorageEnabled(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.x(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "downloadingMessage")
    public void setDownloadingMessage(r rVar, String str) {
        this.mRNCWebViewManagerImpl.y(str);
    }

    @Override // U1.p
    @L1.a(name = "forceDarkOn")
    public void setForceDarkOn(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.z(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "geolocationEnabled")
    public void setGeolocationEnabled(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.A(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "hasOnOpenWindowEvent")
    public void setHasOnOpenWindowEvent(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.B(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "hasOnScroll")
    public void setHasOnScroll(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.C(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "incognito")
    public void setIncognito(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.D(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "injectedJavaScript")
    public void setInjectedJavaScript(r rVar, String str) {
        this.mRNCWebViewManagerImpl.E(rVar, str);
    }

    @Override // U1.p
    @L1.a(name = "injectedJavaScriptBeforeContentLoaded")
    public void setInjectedJavaScriptBeforeContentLoaded(r rVar, String str) {
        this.mRNCWebViewManagerImpl.F(rVar, str);
    }

    @Override // U1.p
    @L1.a(name = "injectedJavaScriptBeforeContentLoadedForMainFrameOnly")
    public void setInjectedJavaScriptBeforeContentLoadedForMainFrameOnly(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.G(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "injectedJavaScriptForMainFrameOnly")
    public void setInjectedJavaScriptForMainFrameOnly(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.H(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "injectedJavaScriptObject")
    public void setInjectedJavaScriptObject(r rVar, String str) {
        this.mRNCWebViewManagerImpl.I(rVar, str);
    }

    @Override // U1.p
    @L1.a(name = "javaScriptCanOpenWindowsAutomatically")
    public void setJavaScriptCanOpenWindowsAutomatically(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.J(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "javaScriptEnabled")
    public void setJavaScriptEnabled(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.K(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "lackPermissionToDownloadMessage")
    public void setLackPermissionToDownloadMessage(r rVar, String str) {
        this.mRNCWebViewManagerImpl.L(str);
    }

    @Override // U1.p
    @L1.a(name = "mediaPlaybackRequiresUserAction")
    public void setMediaPlaybackRequiresUserAction(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.M(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "menuItems")
    public void setMenuItems(r rVar, ReadableArray readableArray) {
        this.mRNCWebViewManagerImpl.N(rVar, readableArray);
    }

    @Override // U1.p
    @L1.a(name = "messagingEnabled")
    public void setMessagingEnabled(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.O(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "messagingModuleName")
    public void setMessagingModuleName(r rVar, String str) {
        this.mRNCWebViewManagerImpl.P(rVar, str);
    }

    @Override // U1.p
    @L1.a(name = "minimumFontSize")
    public void setMinimumFontSize(r rVar, int i3) {
        this.mRNCWebViewManagerImpl.Q(rVar, i3);
    }

    @Override // U1.p
    @L1.a(name = "mixedContentMode")
    public void setMixedContentMode(r rVar, String str) {
        this.mRNCWebViewManagerImpl.R(rVar, str);
    }

    @Override // U1.p
    @L1.a(name = "nestedScrollEnabled")
    public void setNestedScrollEnabled(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.S(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "newSource")
    public void setNewSource(r rVar, ReadableMap readableMap) {
        this.mRNCWebViewManagerImpl.b0(rVar, readableMap);
    }

    @Override // U1.p
    @L1.a(name = "overScrollMode")
    public void setOverScrollMode(r rVar, String str) {
        this.mRNCWebViewManagerImpl.T(rVar, str);
    }

    @Override // U1.p
    @L1.a(name = "saveFormDataDisabled")
    public void setSaveFormDataDisabled(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.U(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "scalesPageToFit")
    public void setScalesPageToFit(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.V(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "setBuiltInZoomControls")
    public void setSetBuiltInZoomControls(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.W(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "setDisplayZoomControls")
    public void setSetDisplayZoomControls(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.X(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "setSupportMultipleWindows")
    public void setSetSupportMultipleWindows(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.Y(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "showsHorizontalScrollIndicator")
    public void setShowsHorizontalScrollIndicator(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.Z(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "showsVerticalScrollIndicator")
    public void setShowsVerticalScrollIndicator(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.a0(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "textZoom")
    public void setTextZoom(r rVar, int i3) {
        this.mRNCWebViewManagerImpl.c0(rVar, i3);
    }

    @Override // U1.p
    @L1.a(name = "thirdPartyCookiesEnabled")
    public void setThirdPartyCookiesEnabled(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.d0(rVar, z3);
    }

    @Override // U1.p
    @L1.a(name = "userAgent")
    public void setUserAgent(r rVar, String str) {
        this.mRNCWebViewManagerImpl.e0(rVar, str);
    }

    @Override // U1.p
    @L1.a(name = "webviewDebuggingEnabled")
    public void setWebviewDebuggingEnabled(r rVar, boolean z3) {
        this.mRNCWebViewManagerImpl.g0(rVar, z3);
    }

    @Override // U1.p
    public void stopLoading(r rVar) {
        rVar.getWebView().stopLoading();
    }
}
