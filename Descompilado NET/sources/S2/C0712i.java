package s2;

import java.lang.reflect.Array;

/* JADX INFO: renamed from: s2.i, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
class C0712i {
    public static final Object[] a(Object[] objArr, int i3) {
        D2.h.f(objArr, "reference");
        Object objNewInstance = Array.newInstance(objArr.getClass().getComponentType(), i3);
        D2.h.d(objNewInstance, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.arrayOfNulls>");
        return (Object[]) objNewInstance;
    }

    public static final void b(int i3, int i4) {
        if (i3 <= i4) {
            return;
        }
        throw new IndexOutOfBoundsException("toIndex (" + i3 + ") is greater than size (" + i4 + ").");
    }
}
