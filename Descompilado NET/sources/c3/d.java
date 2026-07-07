package c3;

import D2.h;
import java.security.MessageDigest;

/* JADX INFO: loaded from: classes.dex */
public abstract class d {

    public static final class a implements c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final MessageDigest f5702a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ String f5703b;

        a(String str) {
            this.f5703b = str;
            this.f5702a = MessageDigest.getInstance(str);
        }

        @Override // c3.c
        public byte[] a() {
            return this.f5702a.digest();
        }

        @Override // c3.c
        public void b(byte[] bArr, int i3, int i4) {
            h.f(bArr, "input");
            this.f5702a.update(bArr, i3, i4);
        }
    }

    public static final c a(String str) {
        h.f(str, "algorithm");
        return new a(str);
    }
}
