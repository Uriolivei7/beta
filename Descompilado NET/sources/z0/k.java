package Z0;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import r2.C0686i;
import r2.p;
import s2.AbstractC0695C;
import s2.AbstractC0711h;

/* JADX INFO: loaded from: classes.dex */
public final class k {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final k f2816a = new k();

    private k() {
    }

    private final boolean a(byte[] bArr, String str) {
        if (bArr.length != str.length()) {
            return false;
        }
        Iterable iterableO = AbstractC0711h.o(bArr);
        if (!(iterableO instanceof Collection) || !((Collection) iterableO).isEmpty()) {
            Iterator it = iterableO.iterator();
            while (it.hasNext()) {
                int iA = ((AbstractC0695C) it).a();
                if (((byte) str.charAt(iA)) != bArr[iA]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static final int b(InputStream inputStream) {
        D2.h.f(inputStream, "stream");
        k kVar = f2816a;
        return (kVar.e(inputStream) << 8) | kVar.e(inputStream);
    }

    private final String c(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b4 : bArr) {
            sb.append((char) (p.a(b4) & 65535));
        }
        String string = sb.toString();
        D2.h.e(string, "toString(...)");
        return string;
    }

    private final int d(InputStream inputStream) {
        int iE = e(inputStream);
        int iE2 = e(inputStream);
        return (e(inputStream) << 24) | (e(inputStream) << 16) | (iE2 << 8) | iE;
    }

    private final int e(InputStream inputStream) {
        return inputStream.read() & 255;
    }

    public static final C0686i f(InputStream inputStream) {
        k kVar;
        D2.h.f(inputStream, "stream");
        byte[] bArr = new byte[4];
        try {
        } catch (IOException e4) {
            e4.printStackTrace();
        }
        try {
            try {
                inputStream.read(bArr);
                kVar = f2816a;
            } catch (IOException e5) {
                e5.printStackTrace();
                inputStream.close();
            }
            if (!kVar.a(bArr, "RIFF")) {
                return null;
            }
            kVar.d(inputStream);
            inputStream.read(bArr);
            if (!kVar.a(bArr, "WEBP")) {
                try {
                    inputStream.close();
                } catch (IOException e6) {
                    e6.printStackTrace();
                }
                return null;
            }
            inputStream.read(bArr);
            String strC = kVar.c(bArr);
            int iHashCode = strC.hashCode();
            if (iHashCode != 2640674) {
                if (iHashCode != 2640718) {
                    if (iHashCode == 2640730 && strC.equals("VP8X")) {
                        C0686i c0686iI = kVar.i(inputStream);
                        try {
                            inputStream.close();
                        } catch (IOException e7) {
                            e7.printStackTrace();
                        }
                        return c0686iI;
                    }
                } else if (strC.equals("VP8L")) {
                    C0686i c0686iH = kVar.h(inputStream);
                    try {
                        inputStream.close();
                    } catch (IOException e8) {
                        e8.printStackTrace();
                    }
                    return c0686iH;
                }
            } else if (strC.equals("VP8 ")) {
                C0686i c0686iG = kVar.g(inputStream);
                try {
                    inputStream.close();
                } catch (IOException e9) {
                    e9.printStackTrace();
                }
                return c0686iG;
            }
            inputStream.close();
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e10) {
                e10.printStackTrace();
            }
        }
    }

    private final C0686i g(InputStream inputStream) throws IOException {
        inputStream.skip(7L);
        int iE = e(inputStream);
        int iE2 = e(inputStream);
        int iE3 = e(inputStream);
        if (iE == 157 && iE2 == 1 && iE3 == 42) {
            return new C0686i(Integer.valueOf(b(inputStream)), Integer.valueOf(b(inputStream)));
        }
        return null;
    }

    private final C0686i h(InputStream inputStream) throws IOException {
        d(inputStream);
        if (e(inputStream) != 47) {
            return null;
        }
        int i3 = inputStream.read() & 255;
        int i4 = inputStream.read();
        return new C0686i(Integer.valueOf((i3 | ((i4 & 63) << 8)) + 1), Integer.valueOf((((inputStream.read() & 15) << 10) | ((inputStream.read() & 255) << 2) | ((i4 & 192) >> 6)) + 1));
    }

    private final C0686i i(InputStream inputStream) throws IOException {
        inputStream.skip(8L);
        return new C0686i(Integer.valueOf(j(inputStream) + 1), Integer.valueOf(j(inputStream) + 1));
    }

    private final int j(InputStream inputStream) {
        return (e(inputStream) << 16) | (e(inputStream) << 8) | e(inputStream);
    }
}
