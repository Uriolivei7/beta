package z;

import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected int f11021a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected ByteBuffer f11022b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f11023c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private int f11024d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    d f11025e = d.a();

    protected int a(int i3) {
        return i3 + this.f11022b.getInt(i3);
    }

    protected int b(int i3) {
        if (i3 < this.f11024d) {
            return this.f11022b.getShort(this.f11023c + i3);
        }
        return 0;
    }

    protected void c(int i3, ByteBuffer byteBuffer) {
        this.f11022b = byteBuffer;
        if (byteBuffer == null) {
            this.f11021a = 0;
            this.f11023c = 0;
            this.f11024d = 0;
        } else {
            this.f11021a = i3;
            int i4 = i3 - byteBuffer.getInt(i3);
            this.f11023c = i4;
            this.f11024d = this.f11022b.getShort(i4);
        }
    }

    protected int d(int i3) {
        int i4 = i3 + this.f11021a;
        return i4 + this.f11022b.getInt(i4) + 4;
    }

    protected int e(int i3) {
        int i4 = i3 + this.f11021a;
        return this.f11022b.getInt(i4 + this.f11022b.getInt(i4));
    }
}
