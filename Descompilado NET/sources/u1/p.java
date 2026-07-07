package U1;

import android.view.View;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.W0;

/* JADX INFO: loaded from: classes.dex */
public interface p extends W0 {
    void clearCache(View view, boolean z3);

    void clearFormData(View view);

    void clearHistory(View view);

    void goBack(View view);

    void goForward(View view);

    void injectJavaScript(View view, String str);

    void loadUrl(View view, String str);

    void postMessage(View view, String str);

    void reload(View view);

    void requestFocus(View view);

    void setAllowFileAccess(View view, boolean z3);

    void setAllowFileAccessFromFileURLs(View view, boolean z3);

    void setAllowUniversalAccessFromFileURLs(View view, boolean z3);

    void setAllowingReadAccessToURL(View view, String str);

    void setAllowsAirPlayForMediaPlayback(View view, boolean z3);

    void setAllowsBackForwardNavigationGestures(View view, boolean z3);

    void setAllowsFullscreenVideo(View view, boolean z3);

    void setAllowsInlineMediaPlayback(View view, boolean z3);

    void setAllowsLinkPreview(View view, boolean z3);

    void setAllowsPictureInPictureMediaPlayback(View view, boolean z3);

    void setAllowsProtectedMedia(View view, boolean z3);

    void setAndroidLayerType(View view, String str);

    void setApplicationNameForUserAgent(View view, String str);

    void setAutoManageStatusBarEnabled(View view, boolean z3);

    void setAutomaticallyAdjustContentInsets(View view, boolean z3);

    void setBasicAuthCredential(View view, ReadableMap readableMap);

    void setBounces(View view, boolean z3);

    void setCacheEnabled(View view, boolean z3);

    void setCacheMode(View view, String str);

    void setContentInset(View view, ReadableMap readableMap);

    void setContentInsetAdjustmentBehavior(View view, String str);

    void setContentMode(View view, String str);

    void setDataDetectorTypes(View view, ReadableArray readableArray);

    void setDecelerationRate(View view, double d4);

    void setDirectionalLockEnabled(View view, boolean z3);

    void setDomStorageEnabled(View view, boolean z3);

    void setDownloadingMessage(View view, String str);

    void setEnableApplePay(View view, boolean z3);

    void setForceDarkOn(View view, boolean z3);

    void setFraudulentWebsiteWarningEnabled(View view, boolean z3);

    void setGeolocationEnabled(View view, boolean z3);

    void setHasOnFileDownload(View view, boolean z3);

    void setHasOnOpenWindowEvent(View view, boolean z3);

    void setHasOnScroll(View view, boolean z3);

    void setHideKeyboardAccessoryView(View view, boolean z3);

    void setIncognito(View view, boolean z3);

    void setInjectedJavaScript(View view, String str);

    void setInjectedJavaScriptBeforeContentLoaded(View view, String str);

    void setInjectedJavaScriptBeforeContentLoadedForMainFrameOnly(View view, boolean z3);

    void setInjectedJavaScriptForMainFrameOnly(View view, boolean z3);

    void setInjectedJavaScriptObject(View view, String str);

    void setJavaScriptCanOpenWindowsAutomatically(View view, boolean z3);

    void setJavaScriptEnabled(View view, boolean z3);

    void setKeyboardDisplayRequiresUserAction(View view, boolean z3);

    void setLackPermissionToDownloadMessage(View view, String str);

    void setLimitsNavigationsToAppBoundDomains(View view, boolean z3);

    void setMediaCapturePermissionGrantType(View view, String str);

    void setMediaPlaybackRequiresUserAction(View view, boolean z3);

    void setMenuItems(View view, ReadableArray readableArray);

    void setMessagingEnabled(View view, boolean z3);

    void setMessagingModuleName(View view, String str);

    void setMinimumFontSize(View view, int i3);

    void setMixedContentMode(View view, String str);

    void setNestedScrollEnabled(View view, boolean z3);

    void setNewSource(View view, ReadableMap readableMap);

    void setOverScrollMode(View view, String str);

    void setPagingEnabled(View view, boolean z3);

    void setPullToRefreshEnabled(View view, boolean z3);

    void setRefreshControlLightMode(View view, boolean z3);

    void setSaveFormDataDisabled(View view, boolean z3);

    void setScalesPageToFit(View view, boolean z3);

    void setScrollEnabled(View view, boolean z3);

    void setSetBuiltInZoomControls(View view, boolean z3);

    void setSetDisplayZoomControls(View view, boolean z3);

    void setSetSupportMultipleWindows(View view, boolean z3);

    void setSharedCookiesEnabled(View view, boolean z3);

    void setShowsHorizontalScrollIndicator(View view, boolean z3);

    void setShowsVerticalScrollIndicator(View view, boolean z3);

    void setSuppressMenuItems(View view, ReadableArray readableArray);

    void setTextInteractionEnabled(View view, boolean z3);

    void setTextZoom(View view, int i3);

    void setThirdPartyCookiesEnabled(View view, boolean z3);

    void setUseSharedProcessPool(View view, boolean z3);

    void setUserAgent(View view, String str);

    void setWebviewDebuggingEnabled(View view, boolean z3);

    void stopLoading(View view);
}
