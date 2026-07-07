package z;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: renamed from: z.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0781b extends c {
    public static C0781b h(ByteBuffer byteBuffer) {
        return i(byteBuffer, new C0781b());
    }

    public static C0781b i(ByteBuffer byteBuffer, C0781b c0781b) {
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        return c0781b.f(byteBuffer.getInt(byteBuffer.position()) + byteBuffer.position(), byteBuffer);
    }

    public C0781b f(int i3, ByteBuffer byteBuffer) {
        g(i3, byteBuffer);
        return this;
    }

    public void g(int i3, ByteBuffer byteBuffer) {
        c(i3, byteBuffer);
    }

    public C0780a j(C0780a c0780a, int i3) {
        int iB = b(6);
        if (iB != 0) {
            return c0780a.f(a(d(iB) + (i3 * 4)), this.f11022b);
        }
        return null;
    }

    public int k() {
        int iB = b(6);
        if (iB != 0) {
            return e(iB);
        }
        return 0;
    }

    public int l() {
        int iB = b(4);
        if (iB != 0) {
            return this.f11022b.getInt(iB + this.f11021a);
        }
        return 0;
    }
}
