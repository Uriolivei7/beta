package com.facebook.react.uimanager;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import java.util.Arrays;
import java.util.HashSet;

/* JADX INFO: loaded from: classes.dex */
public final class Z0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final Z0 f7436a = new Z0();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final int[] f7437b = {8, 4, 5, 1, 3, 0, 2};

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final int[] f7438c = {8, 7, 6, 4, 5, 1, 3, 0, 2};

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final HashSet f7439d = new HashSet(Arrays.asList("alignSelf", "alignItems", "collapsable", "flex", "flexBasis", "flexDirection", "flexGrow", "rowGap", "columnGap", "gap", "flexShrink", "flexWrap", "justifyContent", "alignContent", "display", "position", "right", "top", "bottom", "left", "start", "end", "width", "height", "minWidth", "maxWidth", "minHeight", "maxHeight", "margin", "marginVertical", "marginHorizontal", "marginLeft", "marginRight", "marginTop", "marginBottom", "marginStart", "marginEnd", "padding", "paddingVertical", "paddingHorizontal", "paddingLeft", "paddingRight", "paddingTop", "paddingBottom", "paddingStart", "paddingEnd"));

    private Z0() {
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public static final boolean a(ReadableMap readableMap, String str) {
        ReadableType type;
        D2.h.f(readableMap, "map");
        D2.h.f(str, "prop");
        if (f7439d.contains(str)) {
            return true;
        }
        if (D2.h.b("pointerEvents", str)) {
            String string = readableMap.getString(str);
            return D2.h.b("auto", string) || D2.h.b("box-none", string);
        }
        switch (str.hashCode()) {
            case -1989576717:
                return str.equals("borderRightColor") && readableMap.getType("borderRightColor") == ReadableType.Number && readableMap.getInt("borderRightColor") == 0;
            case -1971292586:
                if (str.equals("borderRightWidth")) {
                    return readableMap.isNull("borderRightWidth") || readableMap.getDouble("borderRightWidth") == 0.0d;
                }
                return false;
            case -1470826662:
                return str.equals("borderTopColor") && readableMap.getType("borderTopColor") == ReadableType.Number && readableMap.getInt("borderTopColor") == 0;
            case -1452542531:
                if (str.equals("borderTopWidth")) {
                    return readableMap.isNull("borderTopWidth") || readableMap.getDouble("borderTopWidth") == 0.0d;
                }
                return false;
            case -1308858324:
                return str.equals("borderBottomColor") && readableMap.getType("borderBottomColor") == ReadableType.Number && readableMap.getInt("borderBottomColor") == 0;
            case -1290574193:
                if (str.equals("borderBottomWidth")) {
                    return readableMap.isNull("borderBottomWidth") || readableMap.getDouble("borderBottomWidth") == 0.0d;
                }
                return false;
            case -1267206133:
                if (str.equals("opacity")) {
                    return readableMap.isNull("opacity") || readableMap.getDouble("opacity") == 1.0d;
                }
                return false;
            case -242276144:
                return str.equals("borderLeftColor") && readableMap.getType("borderLeftColor") == ReadableType.Number && readableMap.getInt("borderLeftColor") == 0;
            case -223992013:
                if (str.equals("borderLeftWidth")) {
                    return readableMap.isNull("borderLeftWidth") || readableMap.getDouble("borderLeftWidth") == 0.0d;
                }
                return false;
            case 306963138:
                return str.equals("borderBlockStartColor") && readableMap.getType("borderBlockStartColor") == ReadableType.Number && readableMap.getInt("borderBlockStartColor") == 0;
            case 529642498:
                if (str.equals("overflow")) {
                    return readableMap.isNull("overflow") || D2.h.b("visible", readableMap.getString("overflow"));
                }
                return false;
            case 684610594:
                return str.equals("borderBlockColor") && readableMap.getType("borderBlockColor") == ReadableType.Number && readableMap.getInt("borderBlockColor") == 0;
            case 741115130:
                if (str.equals("borderWidth")) {
                    return readableMap.isNull("borderWidth") || readableMap.getDouble("borderWidth") == 0.0d;
                }
                return false;
            case 762983977:
                return str.equals("borderBlockEndColor") && readableMap.getType("borderBlockEndColor") == ReadableType.Number && readableMap.getInt("borderBlockEndColor") == 0;
            case 1349188574:
                if (!str.equals("borderRadius")) {
                    return false;
                }
                if (!readableMap.hasKey("backgroundColor") || (((type = readableMap.getType("backgroundColor")) != ReadableType.Number || readableMap.getInt("backgroundColor") == 0) && type == ReadableType.Null)) {
                    return !readableMap.hasKey("borderWidth") || readableMap.isNull("borderWidth") || readableMap.getDouble("borderWidth") == 0.0d;
                }
                return false;
            default:
                return false;
        }
    }
}
