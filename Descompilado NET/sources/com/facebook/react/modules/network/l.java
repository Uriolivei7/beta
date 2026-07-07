package com.facebook.react.modules.network;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class l {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a f7022c = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final CharsetDecoder f7023a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private byte[] f7024b;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public l(Charset charset) {
        D2.h.f(charset, "charset");
        CharsetDecoder charsetDecoderNewDecoder = charset.newDecoder();
        D2.h.e(charsetDecoderNewDecoder, "newDecoder(...)");
        this.f7023a = charsetDecoderNewDecoder;
    }

    public final String a(byte[] bArr, int i3) {
        D2.h.f(bArr, "data");
        byte[] bArr2 = this.f7024b;
        if (bArr2 != null) {
            byte[] bArr3 = new byte[bArr2.length + i3];
            System.arraycopy(bArr2, 0, bArr3, 0, bArr2.length);
            System.arraycopy(bArr, 0, bArr3, bArr2.length, i3);
            i3 += bArr2.length;
            bArr = bArr3;
        }
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr, 0, i3);
        boolean z3 = false;
        int i4 = 0;
        CharBuffer charBufferDecode = null;
        while (!z3 && i4 < 4) {
            try {
                charBufferDecode = this.f7023a.decode(byteBufferWrap);
                z3 = true;
            } catch (CharacterCodingException unused) {
                i4++;
                byteBufferWrap = ByteBuffer.wrap(bArr, 0, i3 - i4);
            }
        }
        if (!z3 || i4 <= 0) {
            this.f7024b = null;
        } else {
            byte[] bArr4 = new byte[i4];
            System.arraycopy(bArr, i3 - i4, bArr4, 0, i4);
            this.f7024b = bArr4;
        }
        if (!z3) {
            Y.a.I("ReactNative", "failed to decode string from byte array");
            return "";
        }
        if (charBufferDecode == null) {
            return "";
        }
        char[] cArrArray = charBufferDecode.array();
        D2.h.e(cArrArray, "array(...)");
        return new String(cArrArray, 0, charBufferDecode.length());
    }
}
