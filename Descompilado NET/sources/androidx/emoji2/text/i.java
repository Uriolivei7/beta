package androidx.emoji2.text;

import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.MetaKeyKeyListener;
import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;
import androidx.emoji2.text.f;
import androidx.emoji2.text.o;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
final class i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final f.j f4792a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final o f4793b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private f.e f4794c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final boolean f4795d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final int[] f4796e;

    private static final class a {
        static int a(CharSequence charSequence, int i3, int i4) {
            int length = charSequence.length();
            if (i3 < 0 || length < i3 || i4 < 0) {
                return -1;
            }
            while (true) {
                boolean z3 = false;
                while (i4 != 0) {
                    i3--;
                    if (i3 < 0) {
                        return z3 ? -1 : 0;
                    }
                    char cCharAt = charSequence.charAt(i3);
                    if (z3) {
                        if (!Character.isHighSurrogate(cCharAt)) {
                            return -1;
                        }
                        i4--;
                    } else if (!Character.isSurrogate(cCharAt)) {
                        i4--;
                    } else {
                        if (Character.isHighSurrogate(cCharAt)) {
                            return -1;
                        }
                        z3 = true;
                    }
                }
                return i3;
            }
        }

        static int b(CharSequence charSequence, int i3, int i4) {
            int length = charSequence.length();
            if (i3 < 0 || length < i3 || i4 < 0) {
                return -1;
            }
            while (true) {
                boolean z3 = false;
                while (i4 != 0) {
                    if (i3 >= length) {
                        if (z3) {
                            return -1;
                        }
                        return length;
                    }
                    char cCharAt = charSequence.charAt(i3);
                    if (z3) {
                        if (!Character.isLowSurrogate(cCharAt)) {
                            return -1;
                        }
                        i4--;
                        i3++;
                    } else if (!Character.isSurrogate(cCharAt)) {
                        i4--;
                        i3++;
                    } else {
                        if (Character.isLowSurrogate(cCharAt)) {
                            return -1;
                        }
                        i3++;
                        z3 = true;
                    }
                }
                return i3;
            }
        }
    }

    private static class b implements c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public s f4797a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final f.j f4798b;

        b(s sVar, f.j jVar) {
            this.f4797a = sVar;
            this.f4798b = jVar;
        }

        @Override // androidx.emoji2.text.i.c
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public s b() {
            return this.f4797a;
        }

        @Override // androidx.emoji2.text.i.c
        public boolean c(CharSequence charSequence, int i3, int i4, q qVar) {
            if (qVar.k()) {
                return true;
            }
            if (this.f4797a == null) {
                this.f4797a = new s(charSequence instanceof Spannable ? (Spannable) charSequence : new SpannableString(charSequence));
            }
            this.f4797a.setSpan(this.f4798b.a(qVar), i3, i4, 33);
            return true;
        }
    }

    private interface c {
        Object b();

        boolean c(CharSequence charSequence, int i3, int i4, q qVar);
    }

    private static class d implements c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final String f4799a;

        d(String str) {
            this.f4799a = str;
        }

        @Override // androidx.emoji2.text.i.c
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public d b() {
            return this;
        }

        @Override // androidx.emoji2.text.i.c
        public boolean c(CharSequence charSequence, int i3, int i4, q qVar) {
            if (!TextUtils.equals(charSequence.subSequence(i3, i4), this.f4799a)) {
                return true;
            }
            qVar.l(true);
            return false;
        }
    }

    static final class e {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private int f4800a = 1;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final o.a f4801b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private o.a f4802c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private o.a f4803d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private int f4804e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private int f4805f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private final boolean f4806g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private final int[] f4807h;

        e(o.a aVar, boolean z3, int[] iArr) {
            this.f4801b = aVar;
            this.f4802c = aVar;
            this.f4806g = z3;
            this.f4807h = iArr;
        }

        private static boolean d(int i3) {
            return i3 == 65039;
        }

        private static boolean f(int i3) {
            return i3 == 65038;
        }

        private int g() {
            this.f4800a = 1;
            this.f4802c = this.f4801b;
            this.f4805f = 0;
            return 1;
        }

        private boolean h() {
            if (this.f4802c.b().j() || d(this.f4804e)) {
                return true;
            }
            if (this.f4806g) {
                if (this.f4807h == null) {
                    return true;
                }
                if (Arrays.binarySearch(this.f4807h, this.f4802c.b().b(0)) < 0) {
                    return true;
                }
            }
            return false;
        }

        int a(int i3) {
            o.a aVarA = this.f4802c.a(i3);
            int iG = 2;
            if (this.f4800a != 2) {
                if (aVarA == null) {
                    iG = g();
                } else {
                    this.f4800a = 2;
                    this.f4802c = aVarA;
                    this.f4805f = 1;
                }
            } else if (aVarA != null) {
                this.f4802c = aVarA;
                this.f4805f++;
            } else if (f(i3)) {
                iG = g();
            } else if (!d(i3)) {
                if (this.f4802c.b() != null) {
                    iG = 3;
                    if (this.f4805f != 1 || h()) {
                        this.f4803d = this.f4802c;
                        g();
                    } else {
                        iG = g();
                    }
                } else {
                    iG = g();
                }
            }
            this.f4804e = i3;
            return iG;
        }

        q b() {
            return this.f4802c.b();
        }

        q c() {
            return this.f4803d.b();
        }

        boolean e() {
            return this.f4800a == 2 && this.f4802c.b() != null && (this.f4805f > 1 || h());
        }
    }

    i(o oVar, f.j jVar, f.e eVar, boolean z3, int[] iArr, Set set) {
        this.f4792a = jVar;
        this.f4793b = oVar;
        this.f4794c = eVar;
        this.f4795d = z3;
        this.f4796e = iArr;
        g(set);
    }

    private static boolean a(Editable editable, KeyEvent keyEvent, boolean z3) {
        j[] jVarArr;
        if (f(keyEvent)) {
            return false;
        }
        int selectionStart = Selection.getSelectionStart(editable);
        int selectionEnd = Selection.getSelectionEnd(editable);
        if (!e(selectionStart, selectionEnd) && (jVarArr = (j[]) editable.getSpans(selectionStart, selectionEnd, j.class)) != null && jVarArr.length > 0) {
            for (j jVar : jVarArr) {
                int spanStart = editable.getSpanStart(jVar);
                int spanEnd = editable.getSpanEnd(jVar);
                if ((z3 && spanStart == selectionStart) || ((!z3 && spanEnd == selectionStart) || (selectionStart > spanStart && selectionStart < spanEnd))) {
                    editable.delete(spanStart, spanEnd);
                    return true;
                }
            }
        }
        return false;
    }

    static boolean b(InputConnection inputConnection, Editable editable, int i3, int i4, boolean z3) {
        int iMax;
        int iMin;
        if (editable != null && inputConnection != null && i3 >= 0 && i4 >= 0) {
            int selectionStart = Selection.getSelectionStart(editable);
            int selectionEnd = Selection.getSelectionEnd(editable);
            if (e(selectionStart, selectionEnd)) {
                return false;
            }
            if (z3) {
                iMax = a.a(editable, selectionStart, Math.max(i3, 0));
                iMin = a.b(editable, selectionEnd, Math.max(i4, 0));
                if (iMax == -1 || iMin == -1) {
                    return false;
                }
            } else {
                iMax = Math.max(selectionStart - i3, 0);
                iMin = Math.min(selectionEnd + i4, editable.length());
            }
            j[] jVarArr = (j[]) editable.getSpans(iMax, iMin, j.class);
            if (jVarArr != null && jVarArr.length > 0) {
                for (j jVar : jVarArr) {
                    int spanStart = editable.getSpanStart(jVar);
                    int spanEnd = editable.getSpanEnd(jVar);
                    iMax = Math.min(spanStart, iMax);
                    iMin = Math.max(spanEnd, iMin);
                }
                int iMax2 = Math.max(iMax, 0);
                int iMin2 = Math.min(iMin, editable.length());
                inputConnection.beginBatchEdit();
                editable.delete(iMax2, iMin2);
                inputConnection.endBatchEdit();
                return true;
            }
        }
        return false;
    }

    static boolean c(Editable editable, int i3, KeyEvent keyEvent) {
        if (!(i3 != 67 ? i3 != 112 ? false : a(editable, keyEvent, true) : a(editable, keyEvent, false))) {
            return false;
        }
        MetaKeyKeyListener.adjustMetaAfterKeypress(editable);
        return true;
    }

    private boolean d(CharSequence charSequence, int i3, int i4, q qVar) {
        if (qVar.d() == 0) {
            qVar.m(this.f4794c.a(charSequence, i3, i4, qVar.h()));
        }
        return qVar.d() == 2;
    }

    private static boolean e(int i3, int i4) {
        return i3 == -1 || i4 == -1 || i3 != i4;
    }

    private static boolean f(KeyEvent keyEvent) {
        return !KeyEvent.metaStateHasNoModifiers(keyEvent.getMetaState());
    }

    private void g(Set set) {
        if (set.isEmpty()) {
            return;
        }
        Iterator it = set.iterator();
        while (it.hasNext()) {
            int[] iArr = (int[]) it.next();
            String str = new String(iArr, 0, iArr.length);
            i(str, 0, str.length(), 1, true, new d(str));
        }
    }

    private Object i(CharSequence charSequence, int i3, int i4, int i5, boolean z3, c cVar) {
        int iCharCount;
        e eVar = new e(this.f4793b.f(), this.f4795d, this.f4796e);
        int i6 = 0;
        boolean zC = true;
        int iCodePointAt = Character.codePointAt(charSequence, i3);
        loop0: while (true) {
            iCharCount = i3;
            while (i3 < i4 && i6 < i5 && zC) {
                int iA = eVar.a(iCodePointAt);
                if (iA == 1) {
                    iCharCount += Character.charCount(Character.codePointAt(charSequence, iCharCount));
                    if (iCharCount < i4) {
                        iCodePointAt = Character.codePointAt(charSequence, iCharCount);
                    }
                    i3 = iCharCount;
                } else if (iA == 2) {
                    i3 += Character.charCount(iCodePointAt);
                    if (i3 < i4) {
                        iCodePointAt = Character.codePointAt(charSequence, i3);
                    }
                } else if (iA == 3) {
                    if (z3 || !d(charSequence, iCharCount, i3, eVar.c())) {
                        zC = cVar.c(charSequence, iCharCount, i3, eVar.c());
                        i6++;
                    }
                }
            }
            break loop0;
        }
        if (eVar.e() && i6 < i5 && zC && (z3 || !d(charSequence, iCharCount, i3, eVar.b()))) {
            cVar.c(charSequence, iCharCount, i3, eVar.b());
        }
        return cVar.b();
    }

    CharSequence h(CharSequence charSequence, int i3, int i4, int i5, boolean z3) {
        s sVar;
        j[] jVarArr;
        boolean z4 = charSequence instanceof p;
        if (z4) {
            ((p) charSequence).a();
        }
        if (!z4) {
            try {
                sVar = charSequence instanceof Spannable ? new s((Spannable) charSequence) : (!(charSequence instanceof Spanned) || ((Spanned) charSequence).nextSpanTransition(i3 + (-1), i4 + 1, j.class) > i4) ? null : new s(charSequence);
            } finally {
                if (z4) {
                    ((p) charSequence).d();
                }
            }
        }
        if (sVar != null && (jVarArr = (j[]) sVar.getSpans(i3, i4, j.class)) != null && jVarArr.length > 0) {
            for (j jVar : jVarArr) {
                int spanStart = sVar.getSpanStart(jVar);
                int spanEnd = sVar.getSpanEnd(jVar);
                if (spanStart != i4) {
                    sVar.removeSpan(jVar);
                }
                i3 = Math.min(spanStart, i3);
                i4 = Math.max(spanEnd, i4);
            }
        }
        int i6 = i4;
        if (i3 != i6 && i3 < charSequence.length()) {
            if (i5 != Integer.MAX_VALUE && sVar != null) {
                i5 -= ((j[]) sVar.getSpans(0, sVar.length(), j.class)).length;
            }
            s sVar2 = (s) i(charSequence, i3, i6, i5, z3, new b(sVar, this.f4792a));
            if (sVar2 == null) {
                if (z4) {
                    ((p) charSequence).d();
                }
                return charSequence;
            }
            Spannable spannableB = sVar2.b();
            if (z4) {
                ((p) charSequence).d();
            }
            return spannableB;
        }
        return charSequence;
    }
}
