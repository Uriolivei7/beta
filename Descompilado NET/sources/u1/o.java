package U1;

import android.view.View;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.AbstractC0430g;
import com.facebook.react.uimanager.BaseViewManager;
import com.facebook.react.uimanager.U;

/* JADX INFO: loaded from: classes.dex */
public class o extends AbstractC0430g {
    public o(BaseViewManager<Object, ? extends U> baseViewManager) {
        super(baseViewManager);
    }

    @Override // com.facebook.react.uimanager.AbstractC0430g, com.facebook.react.uimanager.Q0
    public void a(View view, String str, ReadableArray readableArray) {
        str.hashCode();
        switch (str) {
            case "goBack":
                ((p) this.f7477a).goBack(view);
                break;
            case "stopLoading":
                ((p) this.f7477a).stopLoading(view);
                break;
            case "reload":
                ((p) this.f7477a).reload(view);
                break;
            case "clearCache":
                ((p) this.f7477a).clearCache(view, readableArray.getBoolean(0));
                break;
            case "goForward":
                ((p) this.f7477a).goForward(view);
                break;
            case "clearFormData":
                ((p) this.f7477a).clearFormData(view);
                break;
            case "loadUrl":
                ((p) this.f7477a).loadUrl(view, readableArray.getString(0));
                break;
            case "clearHistory":
                ((p) this.f7477a).clearHistory(view);
                break;
            case "requestFocus":
                ((p) this.f7477a).requestFocus(view);
                break;
            case "postMessage":
                ((p) this.f7477a).postMessage(view, readableArray.getString(0));
                break;
            case "injectJavaScript":
                ((p) this.f7477a).injectJavaScript(view, readableArray.getString(0));
                break;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.facebook.react.uimanager.AbstractC0430g, com.facebook.react.uimanager.Q0
    public void b(View view, String str, Object obj) {
        str.hashCode();
        switch (str) {
            case "allowFileAccessFromFileURLs":
                ((p) this.f7477a).setAllowFileAccessFromFileURLs(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "sharedCookiesEnabled":
                ((p) this.f7477a).setSharedCookiesEnabled(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "allowsPictureInPictureMediaPlayback":
                ((p) this.f7477a).setAllowsPictureInPictureMediaPlayback(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "allowsProtectedMedia":
                ((p) this.f7477a).setAllowsProtectedMedia(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "saveFormDataDisabled":
                ((p) this.f7477a).setSaveFormDataDisabled(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "textInteractionEnabled":
                ((p) this.f7477a).setTextInteractionEnabled(view, obj != null ? ((Boolean) obj).booleanValue() : true);
                break;
            case "injectedJavaScriptBeforeContentLoaded":
                ((p) this.f7477a).setInjectedJavaScriptBeforeContentLoaded(view, obj != null ? (String) obj : null);
                break;
            case "directionalLockEnabled":
                ((p) this.f7477a).setDirectionalLockEnabled(view, obj != null ? ((Boolean) obj).booleanValue() : true);
                break;
            case "javaScriptEnabled":
                ((p) this.f7477a).setJavaScriptEnabled(view, obj != null ? ((Boolean) obj).booleanValue() : true);
                break;
            case "messagingEnabled":
                ((p) this.f7477a).setMessagingEnabled(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "dataDetectorTypes":
                ((p) this.f7477a).setDataDetectorTypes(view, (ReadableArray) obj);
                break;
            case "menuItems":
                ((p) this.f7477a).setMenuItems(view, (ReadableArray) obj);
                break;
            case "incognito":
                ((p) this.f7477a).setIncognito(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "allowingReadAccessToURL":
                ((p) this.f7477a).setAllowingReadAccessToURL(view, obj != null ? (String) obj : null);
                break;
            case "overScrollMode":
                ((p) this.f7477a).setOverScrollMode(view, obj != null ? (String) obj : null);
                break;
            case "scrollEnabled":
                ((p) this.f7477a).setScrollEnabled(view, obj != null ? ((Boolean) obj).booleanValue() : true);
                break;
            case "keyboardDisplayRequiresUserAction":
                ((p) this.f7477a).setKeyboardDisplayRequiresUserAction(view, obj != null ? ((Boolean) obj).booleanValue() : true);
                break;
            case "domStorageEnabled":
                ((p) this.f7477a).setDomStorageEnabled(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "allowsLinkPreview":
                ((p) this.f7477a).setAllowsLinkPreview(view, obj != null ? ((Boolean) obj).booleanValue() : true);
                break;
            case "useSharedProcessPool":
                ((p) this.f7477a).setUseSharedProcessPool(view, obj != null ? ((Boolean) obj).booleanValue() : true);
                break;
            case "textZoom":
                ((p) this.f7477a).setTextZoom(view, obj != null ? ((Double) obj).intValue() : 0);
                break;
            case "showsVerticalScrollIndicator":
                ((p) this.f7477a).setShowsVerticalScrollIndicator(view, obj != null ? ((Boolean) obj).booleanValue() : true);
                break;
            case "forceDarkOn":
                ((p) this.f7477a).setForceDarkOn(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "minimumFontSize":
                ((p) this.f7477a).setMinimumFontSize(view, obj != null ? ((Double) obj).intValue() : 0);
                break;
            case "hideKeyboardAccessoryView":
                ((p) this.f7477a).setHideKeyboardAccessoryView(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "allowUniversalAccessFromFileURLs":
                ((p) this.f7477a).setAllowUniversalAccessFromFileURLs(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "mediaCapturePermissionGrantType":
                ((p) this.f7477a).setMediaCapturePermissionGrantType(view, (String) obj);
                break;
            case "newSource":
                ((p) this.f7477a).setNewSource(view, (ReadableMap) obj);
                break;
            case "hasOnFileDownload":
                ((p) this.f7477a).setHasOnFileDownload(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "cacheMode":
                ((p) this.f7477a).setCacheMode(view, (String) obj);
                break;
            case "pagingEnabled":
                ((p) this.f7477a).setPagingEnabled(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "contentMode":
                ((p) this.f7477a).setContentMode(view, (String) obj);
                break;
            case "messagingModuleName":
                ((p) this.f7477a).setMessagingModuleName(view, obj != null ? (String) obj : null);
                break;
            case "hasOnOpenWindowEvent":
                ((p) this.f7477a).setHasOnOpenWindowEvent(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "javaScriptCanOpenWindowsAutomatically":
                ((p) this.f7477a).setJavaScriptCanOpenWindowsAutomatically(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "setDisplayZoomControls":
                ((p) this.f7477a).setSetDisplayZoomControls(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "allowsFullscreenVideo":
                ((p) this.f7477a).setAllowsFullscreenVideo(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "nestedScrollEnabled":
                ((p) this.f7477a).setNestedScrollEnabled(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "injectedJavaScriptBeforeContentLoadedForMainFrameOnly":
                ((p) this.f7477a).setInjectedJavaScriptBeforeContentLoadedForMainFrameOnly(view, obj != null ? ((Boolean) obj).booleanValue() : true);
                break;
            case "hasOnScroll":
                ((p) this.f7477a).setHasOnScroll(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "bounces":
                ((p) this.f7477a).setBounces(view, obj != null ? ((Boolean) obj).booleanValue() : true);
                break;
            case "setSupportMultipleWindows":
                ((p) this.f7477a).setSetSupportMultipleWindows(view, obj != null ? ((Boolean) obj).booleanValue() : true);
                break;
            case "lackPermissionToDownloadMessage":
                ((p) this.f7477a).setLackPermissionToDownloadMessage(view, obj != null ? (String) obj : null);
                break;
            case "injectedJavaScript":
                ((p) this.f7477a).setInjectedJavaScript(view, obj != null ? (String) obj : null);
                break;
            case "automaticallyAdjustContentInsets":
                ((p) this.f7477a).setAutomaticallyAdjustContentInsets(view, obj != null ? ((Boolean) obj).booleanValue() : true);
                break;
            case "userAgent":
                ((p) this.f7477a).setUserAgent(view, obj != null ? (String) obj : null);
                break;
            case "allowsInlineMediaPlayback":
                ((p) this.f7477a).setAllowsInlineMediaPlayback(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "cacheEnabled":
                ((p) this.f7477a).setCacheEnabled(view, obj != null ? ((Boolean) obj).booleanValue() : true);
                break;
            case "injectedJavaScriptForMainFrameOnly":
                ((p) this.f7477a).setInjectedJavaScriptForMainFrameOnly(view, obj != null ? ((Boolean) obj).booleanValue() : true);
                break;
            case "webviewDebuggingEnabled":
                ((p) this.f7477a).setWebviewDebuggingEnabled(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "injectedJavaScriptObject":
                ((p) this.f7477a).setInjectedJavaScriptObject(view, obj != null ? (String) obj : null);
                break;
            case "applicationNameForUserAgent":
                ((p) this.f7477a).setApplicationNameForUserAgent(view, obj != null ? (String) obj : null);
                break;
            case "mixedContentMode":
                ((p) this.f7477a).setMixedContentMode(view, (String) obj);
                break;
            case "contentInset":
                ((p) this.f7477a).setContentInset(view, (ReadableMap) obj);
                break;
            case "allowsBackForwardNavigationGestures":
                ((p) this.f7477a).setAllowsBackForwardNavigationGestures(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "allowsAirPlayForMediaPlayback":
                ((p) this.f7477a).setAllowsAirPlayForMediaPlayback(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "allowFileAccess":
                ((p) this.f7477a).setAllowFileAccess(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "limitsNavigationsToAppBoundDomains":
                ((p) this.f7477a).setLimitsNavigationsToAppBoundDomains(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "setBuiltInZoomControls":
                ((p) this.f7477a).setSetBuiltInZoomControls(view, obj != null ? ((Boolean) obj).booleanValue() : true);
                break;
            case "pullToRefreshEnabled":
                ((p) this.f7477a).setPullToRefreshEnabled(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "refreshControlLightMode":
                ((p) this.f7477a).setRefreshControlLightMode(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "fraudulentWebsiteWarningEnabled":
                ((p) this.f7477a).setFraudulentWebsiteWarningEnabled(view, obj != null ? ((Boolean) obj).booleanValue() : true);
                break;
            case "geolocationEnabled":
                ((p) this.f7477a).setGeolocationEnabled(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "downloadingMessage":
                ((p) this.f7477a).setDownloadingMessage(view, obj != null ? (String) obj : null);
                break;
            case "basicAuthCredential":
                ((p) this.f7477a).setBasicAuthCredential(view, (ReadableMap) obj);
                break;
            case "enableApplePay":
                ((p) this.f7477a).setEnableApplePay(view, obj != null ? ((Boolean) obj).booleanValue() : false);
                break;
            case "mediaPlaybackRequiresUserAction":
                ((p) this.f7477a).setMediaPlaybackRequiresUserAction(view, obj != null ? ((Boolean) obj).booleanValue() : true);
                break;
            case "thirdPartyCookiesEnabled":
                ((p) this.f7477a).setThirdPartyCookiesEnabled(view, obj != null ? ((Boolean) obj).booleanValue() : true);
                break;
            case "autoManageStatusBarEnabled":
                ((p) this.f7477a).setAutoManageStatusBarEnabled(view, obj != null ? ((Boolean) obj).booleanValue() : true);
                break;
            case "androidLayerType":
                ((p) this.f7477a).setAndroidLayerType(view, (String) obj);
                break;
            case "suppressMenuItems":
                ((p) this.f7477a).setSuppressMenuItems(view, (ReadableArray) obj);
                break;
            case "showsHorizontalScrollIndicator":
                ((p) this.f7477a).setShowsHorizontalScrollIndicator(view, obj != null ? ((Boolean) obj).booleanValue() : true);
                break;
            case "scalesPageToFit":
                ((p) this.f7477a).setScalesPageToFit(view, obj != null ? ((Boolean) obj).booleanValue() : true);
                break;
            case "decelerationRate":
                ((p) this.f7477a).setDecelerationRate(view, obj == null ? 0.0d : ((Double) obj).doubleValue());
                break;
            case "contentInsetAdjustmentBehavior":
                ((p) this.f7477a).setContentInsetAdjustmentBehavior(view, (String) obj);
                break;
            default:
                super.b(view, str, obj);
                break;
        }
    }
}
