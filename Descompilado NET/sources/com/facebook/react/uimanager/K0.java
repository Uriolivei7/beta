package com.facebook.react.uimanager;

import e1.C0527d;
import g1.C0542a;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import r1.C0670b;

/* JADX INFO: loaded from: classes.dex */
public class K0 {
    static Map a(V0 v02) {
        Map mapB = J0.b();
        mapB.put("ViewManagerNames", new ArrayList(v02.b()));
        mapB.put("LazyViewManagersEnabled", Boolean.TRUE);
        return mapB;
    }

    static Map b(List list, Map map, Map map2) {
        Map mapB = J0.b();
        Map mapA = J0.a();
        Map mapC = J0.c();
        if (map != null) {
            map.putAll(mapA);
        }
        if (map2 != null) {
            map2.putAll(mapC);
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            ViewManager viewManager = (ViewManager) it.next();
            String name = viewManager.getName();
            Map mapC2 = c(viewManager, null, null, map, map2);
            if (!mapC2.isEmpty()) {
                mapB.put(name, mapC2);
            }
        }
        mapB.put("genericBubblingEventTypes", mapA);
        mapB.put("genericDirectEventTypes", mapC);
        return mapB;
    }

    static Map c(ViewManager viewManager, Map map, Map map2, Map map3, Map map4) {
        HashMap mapB = C0527d.b();
        Map<String, Object> exportedCustomBubblingEventTypeConstants = viewManager.getExportedCustomBubblingEventTypeConstants();
        if (exportedCustomBubblingEventTypeConstants != null) {
            if (C0670b.f() && C0670b.p()) {
                exportedCustomBubblingEventTypeConstants = e(exportedCustomBubblingEventTypeConstants);
            }
            f(map3, exportedCustomBubblingEventTypeConstants);
            f(exportedCustomBubblingEventTypeConstants, map);
            mapB.put("bubblingEventTypes", exportedCustomBubblingEventTypeConstants);
        } else if (map != null) {
            mapB.put("bubblingEventTypes", map);
        }
        Map<String, Object> exportedCustomDirectEventTypeConstants = viewManager.getExportedCustomDirectEventTypeConstants();
        g(viewManager.getName(), exportedCustomDirectEventTypeConstants);
        if (exportedCustomDirectEventTypeConstants != null) {
            if (C0670b.f() && C0670b.p()) {
                exportedCustomDirectEventTypeConstants = e(exportedCustomDirectEventTypeConstants);
            }
            f(map4, exportedCustomDirectEventTypeConstants);
            f(exportedCustomDirectEventTypeConstants, map2);
            mapB.put("directEventTypes", exportedCustomDirectEventTypeConstants);
        } else if (map2 != null) {
            mapB.put("directEventTypes", map2);
        }
        Map<String, Object> exportedViewConstants = viewManager.getExportedViewConstants();
        if (exportedViewConstants != null) {
            mapB.put("Constants", exportedViewConstants);
        }
        Map<String, Integer> commandsMap = viewManager.getCommandsMap();
        if (commandsMap != null) {
            mapB.put("Commands", commandsMap);
        }
        Map<String, String> nativeProps = viewManager.getNativeProps();
        if (!nativeProps.isEmpty()) {
            mapB.put("NativeProps", nativeProps);
        }
        return mapB;
    }

    public static Map d() {
        return C0527d.e("bubblingEventTypes", J0.a(), "directEventTypes", J0.c());
    }

    static Map e(Map map) {
        if (map == null) {
            return null;
        }
        HashSet<String> hashSet = new HashSet();
        for (Object obj : map.keySet()) {
            if (obj instanceof String) {
                String str = (String) obj;
                if (!str.startsWith("top")) {
                    hashSet.add(str);
                }
            }
        }
        if (!(map instanceof HashMap)) {
            map = new HashMap(map);
        }
        for (String str2 : hashSet) {
            map.put("top" + (str2.startsWith("on") ? str2.substring(2) : str2.substring(0, 1).toUpperCase() + str2.substring(1)), map.get(str2));
        }
        return map;
    }

    private static void f(Map map, Map map2) {
        if (map == null || map2 == null || map2.isEmpty()) {
            return;
        }
        for (Object obj : map2.keySet()) {
            Object obj2 = map2.get(obj);
            Object obj3 = map.get(obj);
            if (obj3 != null && (obj2 instanceof Map) && (obj3 instanceof Map)) {
                if (!(obj3 instanceof HashMap)) {
                    HashMap map3 = new HashMap((Map) obj3);
                    map.replace(obj, map3);
                    obj3 = map3;
                }
                f((Map) obj3, (Map) obj2);
            } else {
                map.put(obj, obj2);
            }
        }
    }

    private static void g(String str, Map map) {
        String str2;
        if (!C0542a.f9423b || map == null) {
            return;
        }
        for (String str3 : map.keySet()) {
            Object obj = map.get(str3);
            if (obj != null && (obj instanceof Map) && (str2 = (String) ((Map) obj).get("registrationName")) != null && str3.startsWith("top") && str2.startsWith("on") && !str3.substring(3).equals(str2.substring(2))) {
                Y.a.m("UIManagerModuleConstantsHelper", String.format("Direct event name for '%s' doesn't correspond to the naming convention, expected 'topEventName'->'onEventName', got '%s'->'%s'", str, str3, str2));
            }
        }
    }
}
