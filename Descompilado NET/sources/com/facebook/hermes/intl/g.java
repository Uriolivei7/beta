package com.facebook.hermes.intl;

import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class g {

    public enum a {
        BOOLEAN,
        STRING
    }

    public static Object a(String str, Object obj, Object obj2, Object obj3, Object obj4) throws B0.e {
        if (B0.d.n(obj)) {
            return obj4;
        }
        if (!B0.d.k(obj)) {
            throw new B0.e(str + " value is invalid.");
        }
        double dF = B0.d.f(obj);
        if (!Double.isNaN(dF) && dF <= B0.d.f(obj3) && dF >= B0.d.f(obj2)) {
            return obj;
        }
        throw new B0.e(str + " value is invalid.");
    }

    public static Object b(Object obj, String str, Object obj2, Object obj3, Object obj4) {
        return a(str, B0.d.a(obj, str), obj2, obj3, obj4);
    }

    public static Object c(Object obj, String str, a aVar, Object obj2, Object obj3) throws B0.e {
        Object objA = B0.d.a(obj, str);
        if (B0.d.n(objA)) {
            return obj3;
        }
        if (B0.d.j(objA)) {
            objA = "";
        }
        if (aVar == a.BOOLEAN && !B0.d.i(objA)) {
            throw new B0.e("Boolean option expected but not found");
        }
        if (aVar == a.STRING && !B0.d.m(objA)) {
            throw new B0.e("String option expected but not found");
        }
        if (B0.d.n(obj2) || Arrays.asList((Object[]) obj2).contains(objA)) {
            return objA;
        }
        throw new B0.e("String option expected but not found");
    }

    public static Enum d(Class cls, Object obj) {
        if (B0.d.n(obj)) {
            return Enum.valueOf(cls, "UNDEFINED");
        }
        if (B0.d.j(obj)) {
            return null;
        }
        String strH = B0.d.h(obj);
        if (strH.equals("2-digit")) {
            return Enum.valueOf(cls, "DIGIT2");
        }
        for (Enum r3 : (Enum[]) cls.getEnumConstants()) {
            if (r3.name().compareToIgnoreCase(strH) == 0) {
                return r3;
            }
        }
        return null;
    }
}
