package a0;

import java.nio.ByteBuffer;

/* JADX INFO: renamed from: a0.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0208b implements q.e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final C0208b f2850a = new C0208b();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static int f2851b = 16384;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final ThreadLocal f2852c = new a();

    /* JADX INFO: renamed from: a0.b$a */
    class a extends ThreadLocal {
        a() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public ByteBuffer initialValue() {
            return ByteBuffer.allocate(C0208b.f2851b);
        }
    }

    public static int e() {
        return f2851b;
    }

    @Override // q.e
    /* JADX INFO: renamed from: d, reason: merged with bridge method [inline-methods] */
    public ByteBuffer b() {
        return (ByteBuffer) f2852c.get();
    }

    @Override // q.e
    /* JADX INFO: renamed from: f, reason: merged with bridge method [inline-methods] */
    public boolean a(ByteBuffer byteBuffer) {
        return true;
    }
}
