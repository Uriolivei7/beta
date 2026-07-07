package com.facebook.react.uimanager;

import android.view.View;
import com.facebook.react.bridge.ColorPropConverter;
import com.facebook.react.bridge.DynamicFromObject;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

/* JADX INFO: renamed from: com.facebook.react.uimanager.g, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0430g implements Q0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected final BaseViewManager f7477a;

    public AbstractC0430g(BaseViewManager<Object, ? extends U> baseViewManager) {
        D2.h.f(baseViewManager, "mViewManager");
        this.f7477a = baseViewManager;
    }

    @Override // com.facebook.react.uimanager.Q0
    public void a(View view, String str, ReadableArray readableArray) {
        D2.h.f(view, "view");
        D2.h.f(str, "commandName");
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    @Override // com.facebook.react.uimanager.Q0
    public void b(View view, String str, Object obj) {
        Integer color;
        D2.h.f(view, "view");
        D2.h.f(str, "propName");
        switch (str.hashCode()) {
            case -2018402664:
                if (str.equals("mixBlendMode")) {
                    this.f7477a.setMixBlendMode(view, (String) obj);
                    break;
                }
                break;
            case -1898517556:
                if (str.equals("onPointerEnterCapture")) {
                    Boolean bool = (Boolean) obj;
                    this.f7477a.setPointerEnterCapture(view, bool != null ? bool.booleanValue() : false);
                    break;
                }
                break;
            case -1721943862:
                if (str.equals("translateX")) {
                    Double d4 = (Double) obj;
                    this.f7477a.setTranslateX(view, d4 != null ? (float) d4.doubleValue() : 0.0f);
                    break;
                }
                break;
            case -1721943861:
                if (str.equals("translateY")) {
                    Double d5 = (Double) obj;
                    this.f7477a.setTranslateY(view, d5 != null ? (float) d5.doubleValue() : 0.0f);
                    break;
                }
                break;
            case -1589741021:
                if (str.equals("shadowColor")) {
                    BaseViewManager baseViewManager = this.f7477a;
                    color = obj != null ? ColorPropConverter.getColor(obj, view.getContext()) : 0;
                    D2.h.c(color);
                    baseViewManager.setShadowColor(view, color.intValue());
                    break;
                }
                break;
            case -1489432511:
                if (str.equals("outlineColor")) {
                    this.f7477a.setOutlineColor(view, (Integer) obj);
                    break;
                }
                break;
            case -1474494833:
                if (str.equals("outlineStyle")) {
                    this.f7477a.setOutlineStyle(view, (String) obj);
                    break;
                }
                break;
            case -1471148380:
                if (str.equals("outlineWidth")) {
                    Double d6 = (Double) obj;
                    this.f7477a.setOutlineWidth(view, d6 != null ? (float) d6.doubleValue() : Float.NaN);
                    break;
                }
                break;
            case -1351902487:
                if (str.equals("onClick")) {
                    Boolean bool2 = (Boolean) obj;
                    this.f7477a.setClick(view, bool2 != null ? bool2.booleanValue() : false);
                    break;
                }
                break;
            case -1274492040:
                if (str.equals("filter")) {
                    this.f7477a.setFilter(view, (ReadableArray) obj);
                    break;
                }
                break;
            case -1267206133:
                if (str.equals("opacity")) {
                    Double d7 = (Double) obj;
                    this.f7477a.setOpacity(view, d7 != null ? (float) d7.doubleValue() : 1.0f);
                    break;
                }
                break;
            case -1247970794:
                if (str.equals("onPointerOutCapture")) {
                    Boolean bool3 = (Boolean) obj;
                    this.f7477a.setPointerOutCapture(view, bool3 != null ? bool3.booleanValue() : false);
                    break;
                }
                break;
            case -1228066334:
                if (str.equals("borderTopLeftRadius")) {
                    Double d8 = (Double) obj;
                    this.f7477a.setBorderTopLeftRadius(view, d8 != null ? (float) d8.doubleValue() : Float.NaN);
                    break;
                }
                break;
            case -1219666915:
                if (str.equals("onClickCapture")) {
                    Boolean bool4 = (Boolean) obj;
                    this.f7477a.setClickCapture(view, bool4 != null ? bool4.booleanValue() : false);
                    break;
                }
                break;
            case -1036769289:
                if (str.equals("onPointerMoveCapture")) {
                    Boolean bool5 = (Boolean) obj;
                    this.f7477a.setPointerMoveCapture(view, bool5 != null ? bool5.booleanValue() : false);
                    break;
                }
                break;
            case -908189618:
                if (str.equals("scaleX")) {
                    Double d9 = (Double) obj;
                    this.f7477a.setScaleX(view, d9 != null ? (float) d9.doubleValue() : 1.0f);
                    break;
                }
                break;
            case -908189617:
                if (str.equals("scaleY")) {
                    Double d10 = (Double) obj;
                    this.f7477a.setScaleY(view, d10 != null ? (float) d10.doubleValue() : 1.0f);
                    break;
                }
                break;
            case -877170387:
                if (str.equals("testID")) {
                    this.f7477a.setTestId(view, (String) obj);
                    break;
                }
                break;
            case -781597262:
                if (str.equals("transformOrigin")) {
                    this.f7477a.setTransformOrigin(view, (ReadableArray) obj);
                    break;
                }
                break;
            case -731417480:
                if (str.equals("zIndex")) {
                    Double d11 = (Double) obj;
                    this.f7477a.setZIndex(view, d11 != null ? (float) d11.doubleValue() : 0.0f);
                    break;
                }
                break;
            case -112141555:
                if (str.equals("onPointerLeaveCapture")) {
                    Boolean bool6 = (Boolean) obj;
                    this.f7477a.setPointerLeaveCapture(view, bool6 != null ? bool6.booleanValue() : false);
                    break;
                }
                break;
            case -101663499:
                if (str.equals("accessibilityHint")) {
                    this.f7477a.setAccessibilityHint(view, (String) obj);
                    break;
                }
                break;
            case -101359900:
                if (str.equals("accessibilityRole")) {
                    this.f7477a.setAccessibilityRole(view, (String) obj);
                    break;
                }
                break;
            case -80891667:
                if (str.equals("renderToHardwareTextureAndroid")) {
                    Boolean bool7 = (Boolean) obj;
                    this.f7477a.setRenderToHardwareTexture(view, bool7 != null ? bool7.booleanValue() : false);
                    break;
                }
                break;
            case -40300674:
                if (str.equals("rotation")) {
                    Double d12 = (Double) obj;
                    this.f7477a.setRotation(view, d12 != null ? (float) d12.doubleValue() : 0.0f);
                    break;
                }
                break;
            case -4379043:
                if (str.equals("elevation")) {
                    Double d13 = (Double) obj;
                    this.f7477a.setElevation(view, d13 != null ? (float) d13.doubleValue() : 0.0f);
                    break;
                }
                break;
            case 3506294:
                if (str.equals("role")) {
                    this.f7477a.setRole(view, (String) obj);
                    break;
                }
                break;
            case 17941018:
                if (str.equals("onPointerEnter")) {
                    Boolean bool8 = (Boolean) obj;
                    this.f7477a.setPointerEnter(view, bool8 != null ? bool8.booleanValue() : false);
                    break;
                }
                break;
            case 24119801:
                if (str.equals("onPointerLeave")) {
                    Boolean bool9 = (Boolean) obj;
                    this.f7477a.setPointerLeave(view, bool9 != null ? bool9.booleanValue() : false);
                    break;
                }
                break;
            case 36255470:
                if (str.equals("accessibilityLiveRegion")) {
                    this.f7477a.setAccessibilityLiveRegion(view, (String) obj);
                    break;
                }
                break;
            case 132353428:
                if (str.equals("onPointerOverCapture")) {
                    Boolean bool10 = (Boolean) obj;
                    this.f7477a.setPointerOverCapture(view, bool10 != null ? bool10.booleanValue() : false);
                    break;
                }
                break;
            case 317346576:
                if (str.equals("onPointerOut")) {
                    Boolean bool11 = (Boolean) obj;
                    this.f7477a.setPointerOut(view, bool11 != null ? bool11.booleanValue() : false);
                    break;
                }
                break;
            case 333432965:
                if (str.equals("borderTopRightRadius")) {
                    Double d14 = (Double) obj;
                    this.f7477a.setBorderTopRightRadius(view, d14 != null ? (float) d14.doubleValue() : Float.NaN);
                    break;
                }
                break;
            case 581268560:
                if (str.equals("borderBottomLeftRadius")) {
                    Double d15 = (Double) obj;
                    this.f7477a.setBorderBottomLeftRadius(view, d15 != null ? (float) d15.doubleValue() : Float.NaN);
                    break;
                }
                break;
            case 588239831:
                if (str.equals("borderBottomRightRadius")) {
                    Double d16 = (Double) obj;
                    this.f7477a.setBorderBottomRightRadius(view, d16 != null ? (float) d16.doubleValue() : Float.NaN);
                    break;
                }
                break;
            case 743055051:
                if (str.equals("boxShadow")) {
                    this.f7477a.setBoxShadow(view, (ReadableArray) obj);
                    break;
                }
                break;
            case 746986311:
                if (str.equals("importantForAccessibility")) {
                    this.f7477a.setImportantForAccessibility(view, (String) obj);
                    break;
                }
                break;
            case 1052666732:
                if (str.equals("transform")) {
                    this.f7477a.setTransform(view, (ReadableArray) obj);
                    break;
                }
                break;
            case 1146842694:
                if (str.equals("accessibilityLabel")) {
                    this.f7477a.setAccessibilityLabel(view, (String) obj);
                    break;
                }
                break;
            case 1153872867:
                if (str.equals("accessibilityState")) {
                    this.f7477a.setViewState(view, (ReadableMap) obj);
                    break;
                }
                break;
            case 1156088003:
                if (str.equals("accessibilityValue")) {
                    this.f7477a.setAccessibilityValue(view, (ReadableMap) obj);
                    break;
                }
                break;
            case 1247744079:
                if (str.equals("onPointerMove")) {
                    Boolean bool12 = (Boolean) obj;
                    this.f7477a.setPointerMove(view, bool12 != null ? bool12.booleanValue() : false);
                    break;
                }
                break;
            case 1247809874:
                if (str.equals("onPointerOver")) {
                    Boolean bool13 = (Boolean) obj;
                    this.f7477a.setPointerOver(view, bool13 != null ? bool13.booleanValue() : false);
                    break;
                }
                break;
            case 1287124693:
                if (str.equals("backgroundColor")) {
                    BaseViewManager baseViewManager2 = this.f7477a;
                    color = obj != null ? ColorPropConverter.getColor(obj, view.getContext()) : 0;
                    D2.h.c(color);
                    baseViewManager2.setBackgroundColor(view, color.intValue());
                    break;
                }
                break;
            case 1349188574:
                if (str.equals("borderRadius")) {
                    Double d17 = (Double) obj;
                    this.f7477a.setBorderRadius(view, d17 != null ? (float) d17.doubleValue() : Float.NaN);
                    break;
                }
                break;
            case 1407295349:
                if (str.equals("outlineOffset")) {
                    Double d18 = (Double) obj;
                    this.f7477a.setOutlineOffset(view, d18 != null ? (float) d18.doubleValue() : Float.NaN);
                    break;
                }
                break;
            case 1505602511:
                if (str.equals("accessibilityActions")) {
                    this.f7477a.setAccessibilityActions(view, (ReadableArray) obj);
                    break;
                }
                break;
            case 1761903244:
                if (str.equals("accessibilityCollection")) {
                    this.f7477a.setAccessibilityCollection(view, (ReadableMap) obj);
                    break;
                }
                break;
            case 1865277756:
                if (str.equals("accessibilityLabelledBy")) {
                    this.f7477a.setAccessibilityLabelledBy(view, new DynamicFromObject(obj));
                    break;
                }
                break;
            case 1993034687:
                if (str.equals("accessibilityCollectionItem")) {
                    this.f7477a.setAccessibilityCollectionItem(view, (ReadableMap) obj);
                    break;
                }
                break;
            case 2045685618:
                if (str.equals("nativeID")) {
                    this.f7477a.setNativeId(view, (String) obj);
                    break;
                }
                break;
        }
    }
}
