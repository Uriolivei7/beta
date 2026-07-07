package androidx.emoji2.text;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import z.C0781b;

/* JADX INFO: loaded from: classes.dex */
abstract class n {

    private static class a implements c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final ByteBuffer f4828a;

        a(ByteBuffer byteBuffer) {
            this.f4828a = byteBuffer;
            byteBuffer.order(ByteOrder.BIG_ENDIAN);
        }

        @Override // androidx.emoji2.text.n.c
        public void a(int i3) {
            ByteBuffer byteBuffer = this.f4828a;
            byteBuffer.position(byteBuffer.position() + i3);
        }

        @Override // androidx.emoji2.text.n.c
        public int b() {
            return n.d(this.f4828a.getShort());
        }

        @Override // androidx.emoji2.text.n.c
        public long c() {
            return n.c(this.f4828a.getInt());
        }

        @Override // androidx.emoji2.text.n.c
        public int d() {
            return this.f4828a.getInt();
        }

        @Override // androidx.emoji2.text.n.c
        public long e() {
            return this.f4828a.position();
        }
    }

    private static class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final long f4829a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final long f4830b;

        b(long j3, long j4) {
            this.f4829a = j3;
            this.f4830b = j4;
        }

        long a() {
            return this.f4829a;
        }
    }

    private interface c {
        void a(int i3);

        int b();

        long c();

        int d();

        long e();
    }

    private static b a(c cVar) throws IOException {
        long jC;
        cVar.a(4);
        int iB = cVar.b();
        if (iB > 100) {
            throw new IOException("Cannot read metadata.");
        }
        cVar.a(6);
        int i3 = 0;
        while (true) {
            if (i3 >= iB) {
                jC = -1;
                break;
            }
            int iD = cVar.d();
            cVar.a(4);
            jC = cVar.c();
            cVar.a(4);
            if (1835365473 == iD) {
                break;
            }
            i3++;
        }
        if (jC != -1) {
            cVar.a((int) (jC - cVar.e()));
            cVar.a(12);
            long jC2 = cVar.c();
            for (int i4 = 0; i4 < jC2; i4++) {
                int iD2 = cVar.d();
                long jC3 = cVar.c();
                long jC4 = cVar.c();
                if (1164798569 == iD2 || 1701669481 == iD2) {
                    return new b(jC3 + jC, jC4);
                }
            }
        }
        throw new IOException("Cannot read metadata.");
    }

    static C0781b b(ByteBuffer byteBuffer) {
        ByteBuffer byteBufferDuplicate = byteBuffer.duplicate();
        byteBufferDuplicate.position((int) a(new a(byteBufferDuplicate)).a());
        return C0781b.h(byteBufferDuplicate);
    }

    static long c(int i3) {
        return ((long) i3) & 4294967295L;
    }

    static int d(short s3) {
        return s3 & 65535;
    }
}
