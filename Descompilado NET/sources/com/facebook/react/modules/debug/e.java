package com.facebook.react.modules.debug;

import java.util.ArrayList;
import java.util.Iterator;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public abstract class e {
    /* JADX INFO: Access modifiers changed from: private */
    public static final void d(ArrayList arrayList, long j3) {
        int size = arrayList.size();
        int i3 = 0;
        for (int i4 = 0; i4 < size; i4++) {
            if (((Number) arrayList.get(i4)).longValue() < j3) {
                i3++;
            }
        }
        if (i3 > 0) {
            int i5 = size - i3;
            for (int i6 = 0; i6 < i5; i6++) {
                arrayList.set(i6, arrayList.get(i6 + i3));
            }
            AbstractC0717n.K(arrayList, i3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final long e(ArrayList arrayList, long j3, long j4) {
        Iterator it = arrayList.iterator();
        D2.h.e(it, "iterator(...)");
        long j5 = -1;
        while (it.hasNext()) {
            Object next = it.next();
            D2.h.e(next, "next(...)");
            long jLongValue = ((Number) next).longValue();
            if (j3 <= jLongValue && jLongValue < j4) {
                j5 = jLongValue;
            } else if (jLongValue >= j4) {
                break;
            }
        }
        return j5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean f(ArrayList arrayList, long j3, long j4) {
        if (arrayList != null && arrayList.isEmpty()) {
            return false;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            long jLongValue = ((Number) it.next()).longValue();
            if (j3 <= jLongValue && jLongValue < j4) {
                return true;
            }
        }
        return false;
    }
}
