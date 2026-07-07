package b3;

import java.io.OutputStream;
import java.nio.channels.WritableByteChannel;

/* JADX INFO: loaded from: classes.dex */
public interface j extends D, WritableByteChannel {
    j E(int i3);

    j L(int i3);

    j R(byte[] bArr);

    long T(F f3);

    j U();

    i e();

    @Override // b3.D, java.io.Flushable
    void flush();

    j h0(String str);

    j i0(long j3);

    OutputStream j0();

    j k(byte[] bArr, int i3, int i4);

    j n(long j3);

    j t();

    j u(l lVar);

    j w(int i3);
}
