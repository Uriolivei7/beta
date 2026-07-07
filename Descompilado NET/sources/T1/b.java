package T1;

import D2.h;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final b f2346a = new b();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final Pattern f2347b = Pattern.compile("\\b((?:seg-\\d+(?:_\\d+)?|\\d+)\\.js)");

    private b() {
    }

    public static final String a(String str, ReadableArray readableArray) {
        h.f(str, "message");
        h.f(readableArray, "stack");
        StringBuilder sb = new StringBuilder(str);
        sb.append(", stack:\n");
        int size = readableArray.size();
        for (int i3 = 0; i3 < size; i3++) {
            ReadableMap map = readableArray.getMap(i3);
            if (map != null) {
                sb.append(map.getString("methodName"));
                sb.append("@");
                sb.append(f2346a.b(map));
                if (map.hasKey("lineNumber") && !map.isNull("lineNumber") && map.getType("lineNumber") == ReadableType.Number) {
                    sb.append(map.getInt("lineNumber"));
                } else {
                    sb.append(-1);
                }
                if (map.hasKey("column") && !map.isNull("column") && map.getType("column") == ReadableType.Number) {
                    sb.append(":");
                    sb.append(map.getInt("column"));
                }
                sb.append("\n");
            }
        }
        String string = sb.toString();
        h.e(string, "toString(...)");
        return string;
    }

    private final String b(ReadableMap readableMap) {
        String string;
        if (!readableMap.hasKey("file") || readableMap.isNull("file") || readableMap.getType("file") != ReadableType.String || (string = readableMap.getString("file")) == null) {
            return "";
        }
        Matcher matcher = f2347b.matcher(string);
        if (!matcher.find()) {
            return "";
        }
        return matcher.group(1) + ":";
    }
}
