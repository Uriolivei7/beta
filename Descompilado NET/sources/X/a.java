package X;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes.dex */
public abstract class a {
    public static long a(InputStream inputStream, OutputStream outputStream) throws IOException {
        k.g(inputStream);
        k.g(outputStream);
        byte[] bArr = new byte[4096];
        long j3 = 0;
        while (true) {
            int i3 = inputStream.read(bArr);
            if (i3 == -1) {
                return j3;
            }
            outputStream.write(bArr, 0, i3);
            j3 += (long) i3;
        }
    }

    public static int b(InputStream inputStream, byte[] bArr, int i3, int i4) throws IOException {
        k.g(inputStream);
        k.g(bArr);
        if (i4 < 0) {
            throw new IndexOutOfBoundsException("len is negative");
        }
        int i5 = 0;
        while (i5 < i4) {
            int i6 = inputStream.read(bArr, i3 + i5, i4 - i5);
            if (i6 == -1) {
                break;
            }
            i5 += i6;
        }
        return i5;
    }
}
