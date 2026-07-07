package Z0;

import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public final class h {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final h f2809a = new h();

    private h() {
    }

    public static final int a(int i3) {
        return j.a(i3);
    }

    public static final int b(InputStream inputStream) {
        D2.h.f(inputStream, "inputStream");
        try {
            int iD = f2809a.d(inputStream);
            if (iD == 0) {
                return 0;
            }
            return j.d(inputStream, iD);
        } catch (IOException unused) {
            return 0;
        }
    }

    private final boolean c(int i3) {
        switch (i3) {
            case 192:
            case 193:
            case 194:
            case 195:
            case 197:
            case 198:
            case 199:
            case 201:
            case 202:
            case 203:
            case 205:
            case 206:
            case 207:
                return true;
            case 196:
            case 200:
            case 204:
            default:
                return false;
        }
    }

    private final int d(InputStream inputStream) throws IOException {
        if (e(inputStream, 225)) {
            int iA = i.a(inputStream, 2, false);
            if (iA - 2 > 6) {
                int iA2 = i.a(inputStream, 4, false);
                int iA3 = i.a(inputStream, 2, false);
                int i3 = iA - 8;
                if (iA2 == 1165519206 && iA3 == 0) {
                    return i3;
                }
            }
        }
        return 0;
    }

    public static final boolean e(InputStream inputStream, int i3) throws IOException {
        D2.h.f(inputStream, "inputStream");
        while (i.a(inputStream, 1, false) == 255) {
            int iA = 255;
            while (iA == 255) {
                iA = i.a(inputStream, 1, false);
            }
            if ((i3 == 192 && f2809a.c(iA)) || iA == i3) {
                return true;
            }
            if (iA != 1 && iA != 216) {
                if (iA == 217 || iA == 218) {
                    break;
                }
                inputStream.skip(i.a(inputStream, 2, false) - 2);
            }
        }
        return false;
    }
}
