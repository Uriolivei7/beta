package androidx.emoji2.text;

import android.graphics.Typeface;
import android.util.SparseArray;
import java.nio.ByteBuffer;
import z.C0781b;

/* JADX INFO: loaded from: classes.dex */
public final class o {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final C0781b f4831a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final char[] f4832b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final a f4833c = new a(1024);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Typeface f4834d;

    static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final SparseArray f4835a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private q f4836b;

        private a() {
            this(1);
        }

        a a(int i3) {
            SparseArray sparseArray = this.f4835a;
            if (sparseArray == null) {
                return null;
            }
            return (a) sparseArray.get(i3);
        }

        final q b() {
            return this.f4836b;
        }

        void c(q qVar, int i3, int i4) {
            a aVarA = a(qVar.b(i3));
            if (aVarA == null) {
                aVarA = new a();
                this.f4835a.put(qVar.b(i3), aVarA);
            }
            if (i4 > i3) {
                aVarA.c(qVar, i3 + 1, i4);
            } else {
                aVarA.f4836b = qVar;
            }
        }

        a(int i3) {
            this.f4835a = new SparseArray(i3);
        }
    }

    private o(Typeface typeface, C0781b c0781b) {
        this.f4834d = typeface;
        this.f4831a = c0781b;
        this.f4832b = new char[c0781b.k() * 2];
        a(c0781b);
    }

    private void a(C0781b c0781b) {
        int iK = c0781b.k();
        for (int i3 = 0; i3 < iK; i3++) {
            q qVar = new q(this, i3);
            Character.toChars(qVar.f(), this.f4832b, i3 * 2);
            h(qVar);
        }
    }

    public static o b(Typeface typeface, ByteBuffer byteBuffer) {
        try {
            androidx.core.os.h.a("EmojiCompat.MetadataRepo.create");
            return new o(typeface, n.b(byteBuffer));
        } finally {
            androidx.core.os.h.b();
        }
    }

    public char[] c() {
        return this.f4832b;
    }

    public C0781b d() {
        return this.f4831a;
    }

    int e() {
        return this.f4831a.l();
    }

    a f() {
        return this.f4833c;
    }

    Typeface g() {
        return this.f4834d;
    }

    void h(q qVar) {
        q.g.h(qVar, "emoji metadata cannot be null");
        q.g.b(qVar.c() > 0, "invalid metadata codepoint length");
        this.f4833c.c(qVar, 0, qVar.c() - 1);
    }
}
