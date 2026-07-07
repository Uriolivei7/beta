package D0;

import D2.h;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import s2.AbstractC0695C;
import s2.AbstractC0711h;

/* JADX INFO: loaded from: classes.dex */
public final class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final f f160a = new f();

    private f() {
    }

    public static final byte[] a(String str) {
        h.f(str, "value");
        try {
            Charset charsetForName = Charset.forName("ASCII");
            h.e(charsetForName, "forName(...)");
            byte[] bytes = str.getBytes(charsetForName);
            h.e(bytes, "getBytes(...)");
            return bytes;
        } catch (UnsupportedEncodingException e4) {
            throw new RuntimeException("ASCII not found!", e4);
        }
    }

    public static final boolean b(byte[] bArr, byte[] bArr2, int i3) {
        h.f(bArr, "byteArray");
        h.f(bArr2, "pattern");
        if (bArr2.length + i3 > bArr.length) {
            return false;
        }
        Iterable iterableO = AbstractC0711h.o(bArr2);
        if (!(iterableO instanceof Collection) || !((Collection) iterableO).isEmpty()) {
            Iterator it = iterableO.iterator();
            while (it.hasNext()) {
                int iA = ((AbstractC0695C) it).a();
                if (bArr[i3 + iA] != bArr2[iA]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static final boolean c(byte[] bArr, byte[] bArr2) {
        h.f(bArr, "byteArray");
        h.f(bArr2, "pattern");
        return b(bArr, bArr2, 0);
    }
}
