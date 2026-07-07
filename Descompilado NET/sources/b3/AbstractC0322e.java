package b3;

/* JADX INFO: renamed from: b3.e, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0322e {
    public static final byte[] a(String str) {
        D2.h.f(str, "$this$asUtf8ToByteArray");
        byte[] bytes = str.getBytes(K2.d.f816b);
        D2.h.e(bytes, "(this as java.lang.String).getBytes(charset)");
        return bytes;
    }

    public static final String b(byte[] bArr) {
        D2.h.f(bArr, "$this$toUtf8String");
        return new String(bArr, K2.d.f816b);
    }
}
