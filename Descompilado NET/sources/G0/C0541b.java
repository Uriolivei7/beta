package g0;

import androidx.activity.result.d;
import java.io.UnsupportedEncodingException;

/* JADX INFO: renamed from: g0.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0541b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final boolean f9414a = true;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final boolean f9415b = e();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static boolean f9416c = false;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final byte[] f9417d = a("RIFF");

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final byte[] f9418e = a("WEBP");

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final byte[] f9419f = a("VP8 ");

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final byte[] f9420g = a("VP8L");

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static final byte[] f9421h = a("VP8X");

    private static byte[] a(String str) {
        try {
            return str.getBytes("ASCII");
        } catch (UnsupportedEncodingException e4) {
            throw new RuntimeException("ASCII not found!", e4);
        }
    }

    public static boolean b(byte[] bArr, int i3) {
        return j(bArr, i3 + 12, f9421h) && ((bArr[i3 + 20] & 2) == 2);
    }

    public static boolean c(byte[] bArr, int i3, int i4) {
        return i4 >= 21 && j(bArr, i3 + 12, f9421h);
    }

    public static boolean d(byte[] bArr, int i3) {
        return j(bArr, i3 + 12, f9421h) && ((bArr[i3 + 20] & 16) == 16);
    }

    private static boolean e() {
        return true;
    }

    public static boolean f(byte[] bArr, int i3) {
        return j(bArr, i3 + 12, f9420g);
    }

    public static boolean g(byte[] bArr, int i3) {
        return j(bArr, i3 + 12, f9419f);
    }

    public static boolean h(byte[] bArr, int i3, int i4) {
        return i4 >= 20 && j(bArr, i3, f9417d) && j(bArr, i3 + 8, f9418e);
    }

    public static InterfaceC0540a i() {
        if (f9416c) {
            return null;
        }
        try {
            d.a(Class.forName("com.facebook.webpsupport.WebpBitmapFactoryImpl").newInstance());
        } catch (Throwable unused) {
        }
        f9416c = true;
        return null;
    }

    private static boolean j(byte[] bArr, int i3, byte[] bArr2) {
        if (bArr2 == null || bArr == null || bArr2.length + i3 > bArr.length) {
            return false;
        }
        for (int i4 = 0; i4 < bArr2.length; i4++) {
            if (bArr[i4 + i3] != bArr2[i4]) {
                return false;
            }
        }
        return true;
    }
}
