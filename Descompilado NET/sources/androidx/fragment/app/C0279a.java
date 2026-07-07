package androidx.fragment.app;

import android.util.Log;
import androidx.fragment.app.F;
import androidx.fragment.app.x;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.chromium.support_lib_boundary.WebSettingsBoundaryInterface;

/* JADX INFO: renamed from: androidx.fragment.app.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
final class C0279a extends F implements x.l {

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    final x f5056t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    boolean f5057u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    int f5058v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    boolean f5059w;

    C0279a(x xVar) {
        super(xVar.r0(), xVar.t0() != null ? xVar.t0().k().getClassLoader() : null);
        this.f5058v = -1;
        this.f5059w = false;
        this.f5056t = xVar;
    }

    @Override // androidx.fragment.app.x.l
    public boolean a(ArrayList arrayList, ArrayList arrayList2) {
        if (x.G0(2)) {
            Log.v("FragmentManager", "Run: " + this);
        }
        arrayList.add(this);
        arrayList2.add(Boolean.FALSE);
        if (!this.f4890i) {
            return true;
        }
        this.f5056t.i(this);
        return true;
    }

    @Override // androidx.fragment.app.F
    public int f() {
        return o(false);
    }

    @Override // androidx.fragment.app.F
    public int g() {
        return o(true);
    }

    @Override // androidx.fragment.app.F
    public void h() {
        j();
        this.f5056t.b0(this, false);
    }

    @Override // androidx.fragment.app.F
    public void i() {
        j();
        this.f5056t.b0(this, true);
    }

    @Override // androidx.fragment.app.F
    void k(int i3, Fragment fragment, String str, int i4) {
        super.k(i3, fragment, str, i4);
        fragment.f4958u = this.f5056t;
    }

    @Override // androidx.fragment.app.F
    public F l(Fragment fragment) {
        x xVar = fragment.f4958u;
        if (xVar == null || xVar == this.f5056t) {
            return super.l(fragment);
        }
        throw new IllegalStateException("Cannot remove Fragment attached to a different FragmentManager. Fragment " + fragment.toString() + " is already attached to a FragmentManager.");
    }

    void n(int i3) {
        if (this.f4890i) {
            if (x.G0(2)) {
                Log.v("FragmentManager", "Bump nesting in " + this + " by " + i3);
            }
            int size = this.f4884c.size();
            for (int i4 = 0; i4 < size; i4++) {
                F.a aVar = (F.a) this.f4884c.get(i4);
                Fragment fragment = aVar.f4902b;
                if (fragment != null) {
                    fragment.f4957t += i3;
                    if (x.G0(2)) {
                        Log.v("FragmentManager", "Bump nesting of " + aVar.f4902b + " to " + aVar.f4902b.f4957t);
                    }
                }
            }
        }
    }

    int o(boolean z3) {
        if (this.f5057u) {
            throw new IllegalStateException("commit already called");
        }
        if (x.G0(2)) {
            Log.v("FragmentManager", "Commit: " + this);
            PrintWriter printWriter = new PrintWriter(new K("FragmentManager"));
            p("  ", printWriter);
            printWriter.close();
        }
        this.f5057u = true;
        if (this.f4890i) {
            this.f5058v = this.f5056t.l();
        } else {
            this.f5058v = -1;
        }
        this.f5056t.Y(this, z3);
        return this.f5058v;
    }

    public void p(String str, PrintWriter printWriter) {
        q(str, printWriter, true);
    }

    public void q(String str, PrintWriter printWriter, boolean z3) {
        String str2;
        if (z3) {
            printWriter.print(str);
            printWriter.print("mName=");
            printWriter.print(this.f4892k);
            printWriter.print(" mIndex=");
            printWriter.print(this.f5058v);
            printWriter.print(" mCommitted=");
            printWriter.println(this.f5057u);
            if (this.f4889h != 0) {
                printWriter.print(str);
                printWriter.print("mTransition=#");
                printWriter.print(Integer.toHexString(this.f4889h));
            }
            if (this.f4885d != 0 || this.f4886e != 0) {
                printWriter.print(str);
                printWriter.print("mEnterAnim=#");
                printWriter.print(Integer.toHexString(this.f4885d));
                printWriter.print(" mExitAnim=#");
                printWriter.println(Integer.toHexString(this.f4886e));
            }
            if (this.f4887f != 0 || this.f4888g != 0) {
                printWriter.print(str);
                printWriter.print("mPopEnterAnim=#");
                printWriter.print(Integer.toHexString(this.f4887f));
                printWriter.print(" mPopExitAnim=#");
                printWriter.println(Integer.toHexString(this.f4888g));
            }
            if (this.f4893l != 0 || this.f4894m != null) {
                printWriter.print(str);
                printWriter.print("mBreadCrumbTitleRes=#");
                printWriter.print(Integer.toHexString(this.f4893l));
                printWriter.print(" mBreadCrumbTitleText=");
                printWriter.println(this.f4894m);
            }
            if (this.f4895n != 0 || this.f4896o != null) {
                printWriter.print(str);
                printWriter.print("mBreadCrumbShortTitleRes=#");
                printWriter.print(Integer.toHexString(this.f4895n));
                printWriter.print(" mBreadCrumbShortTitleText=");
                printWriter.println(this.f4896o);
            }
        }
        if (this.f4884c.isEmpty()) {
            return;
        }
        printWriter.print(str);
        printWriter.println("Operations:");
        int size = this.f4884c.size();
        for (int i3 = 0; i3 < size; i3++) {
            F.a aVar = (F.a) this.f4884c.get(i3);
            switch (aVar.f4901a) {
                case WebSettingsBoundaryInterface.ForceDarkBehavior.FORCE_DARK_ONLY /* 0 */:
                    str2 = "NULL";
                    break;
                case 1:
                    str2 = "ADD";
                    break;
                case 2:
                    str2 = "REPLACE";
                    break;
                case 3:
                    str2 = "REMOVE";
                    break;
                case 4:
                    str2 = "HIDE";
                    break;
                case 5:
                    str2 = "SHOW";
                    break;
                case 6:
                    str2 = "DETACH";
                    break;
                case 7:
                    str2 = "ATTACH";
                    break;
                case 8:
                    str2 = "SET_PRIMARY_NAV";
                    break;
                case 9:
                    str2 = "UNSET_PRIMARY_NAV";
                    break;
                case 10:
                    str2 = "OP_SET_MAX_LIFECYCLE";
                    break;
                default:
                    str2 = "cmd=" + aVar.f4901a;
                    break;
            }
            printWriter.print(str);
            printWriter.print("  Op #");
            printWriter.print(i3);
            printWriter.print(": ");
            printWriter.print(str2);
            printWriter.print(" ");
            printWriter.println(aVar.f4902b);
            if (z3) {
                if (aVar.f4904d != 0 || aVar.f4905e != 0) {
                    printWriter.print(str);
                    printWriter.print("enterAnim=#");
                    printWriter.print(Integer.toHexString(aVar.f4904d));
                    printWriter.print(" exitAnim=#");
                    printWriter.println(Integer.toHexString(aVar.f4905e));
                }
                if (aVar.f4906f != 0 || aVar.f4907g != 0) {
                    printWriter.print(str);
                    printWriter.print("popEnterAnim=#");
                    printWriter.print(Integer.toHexString(aVar.f4906f));
                    printWriter.print(" popExitAnim=#");
                    printWriter.println(Integer.toHexString(aVar.f4907g));
                }
            }
        }
    }

    void r() {
        int size = this.f4884c.size();
        for (int i3 = 0; i3 < size; i3++) {
            F.a aVar = (F.a) this.f4884c.get(i3);
            Fragment fragment = aVar.f4902b;
            if (fragment != null) {
                fragment.f4952o = this.f5059w;
                fragment.v1(false);
                fragment.u1(this.f4889h);
                fragment.x1(this.f4897p, this.f4898q);
            }
            switch (aVar.f4901a) {
                case 1:
                    fragment.r1(aVar.f4904d, aVar.f4905e, aVar.f4906f, aVar.f4907g);
                    this.f5056t.j1(fragment, false);
                    this.f5056t.j(fragment);
                    break;
                case 2:
                default:
                    throw new IllegalArgumentException("Unknown cmd: " + aVar.f4901a);
                case 3:
                    fragment.r1(aVar.f4904d, aVar.f4905e, aVar.f4906f, aVar.f4907g);
                    this.f5056t.c1(fragment);
                    break;
                case 4:
                    fragment.r1(aVar.f4904d, aVar.f4905e, aVar.f4906f, aVar.f4907g);
                    this.f5056t.D0(fragment);
                    break;
                case 5:
                    fragment.r1(aVar.f4904d, aVar.f4905e, aVar.f4906f, aVar.f4907g);
                    this.f5056t.j1(fragment, false);
                    this.f5056t.n1(fragment);
                    break;
                case 6:
                    fragment.r1(aVar.f4904d, aVar.f4905e, aVar.f4906f, aVar.f4907g);
                    this.f5056t.w(fragment);
                    break;
                case 7:
                    fragment.r1(aVar.f4904d, aVar.f4905e, aVar.f4906f, aVar.f4907g);
                    this.f5056t.j1(fragment, false);
                    this.f5056t.n(fragment);
                    break;
                case 8:
                    this.f5056t.l1(fragment);
                    break;
                case 9:
                    this.f5056t.l1(null);
                    break;
                case 10:
                    this.f5056t.k1(fragment, aVar.f4909i);
                    break;
            }
        }
    }

    void s() {
        for (int size = this.f4884c.size() - 1; size >= 0; size--) {
            F.a aVar = (F.a) this.f4884c.get(size);
            Fragment fragment = aVar.f4902b;
            if (fragment != null) {
                fragment.f4952o = this.f5059w;
                fragment.v1(true);
                fragment.u1(x.g1(this.f4889h));
                fragment.x1(this.f4898q, this.f4897p);
            }
            switch (aVar.f4901a) {
                case 1:
                    fragment.r1(aVar.f4904d, aVar.f4905e, aVar.f4906f, aVar.f4907g);
                    this.f5056t.j1(fragment, true);
                    this.f5056t.c1(fragment);
                    break;
                case 2:
                default:
                    throw new IllegalArgumentException("Unknown cmd: " + aVar.f4901a);
                case 3:
                    fragment.r1(aVar.f4904d, aVar.f4905e, aVar.f4906f, aVar.f4907g);
                    this.f5056t.j(fragment);
                    break;
                case 4:
                    fragment.r1(aVar.f4904d, aVar.f4905e, aVar.f4906f, aVar.f4907g);
                    this.f5056t.n1(fragment);
                    break;
                case 5:
                    fragment.r1(aVar.f4904d, aVar.f4905e, aVar.f4906f, aVar.f4907g);
                    this.f5056t.j1(fragment, true);
                    this.f5056t.D0(fragment);
                    break;
                case 6:
                    fragment.r1(aVar.f4904d, aVar.f4905e, aVar.f4906f, aVar.f4907g);
                    this.f5056t.n(fragment);
                    break;
                case 7:
                    fragment.r1(aVar.f4904d, aVar.f4905e, aVar.f4906f, aVar.f4907g);
                    this.f5056t.j1(fragment, true);
                    this.f5056t.w(fragment);
                    break;
                case 8:
                    this.f5056t.l1(null);
                    break;
                case 9:
                    this.f5056t.l1(fragment);
                    break;
                case 10:
                    this.f5056t.k1(fragment, aVar.f4908h);
                    break;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x00b6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    androidx.fragment.app.Fragment t(java.util.ArrayList r17, androidx.fragment.app.Fragment r18) {
        /*
            r16 = this;
            r0 = r16
            r1 = r17
            r3 = r18
            r4 = 0
        L7:
            java.util.ArrayList r5 = r0.f4884c
            int r5 = r5.size()
            if (r4 >= r5) goto Lbe
            java.util.ArrayList r5 = r0.f4884c
            java.lang.Object r5 = r5.get(r4)
            androidx.fragment.app.F$a r5 = (androidx.fragment.app.F.a) r5
            int r6 = r5.f4901a
            r7 = 1
            if (r6 == r7) goto Lb6
            r8 = 2
            r9 = 0
            r10 = 3
            r11 = 9
            if (r6 == r8) goto L5a
            if (r6 == r10) goto L43
            r8 = 6
            if (r6 == r8) goto L43
            r8 = 7
            if (r6 == r8) goto Lb6
            r8 = 8
            if (r6 == r8) goto L31
            goto Lbb
        L31:
            java.util.ArrayList r6 = r0.f4884c
            androidx.fragment.app.F$a r8 = new androidx.fragment.app.F$a
            r8.<init>(r11, r3, r7)
            r6.add(r4, r8)
            r5.f4903c = r7
            int r4 = r4 + 1
            androidx.fragment.app.Fragment r3 = r5.f4902b
            goto Lbb
        L43:
            androidx.fragment.app.Fragment r6 = r5.f4902b
            r1.remove(r6)
            androidx.fragment.app.Fragment r5 = r5.f4902b
            if (r5 != r3) goto Lbb
            java.util.ArrayList r3 = r0.f4884c
            androidx.fragment.app.F$a r6 = new androidx.fragment.app.F$a
            r6.<init>(r11, r5)
            r3.add(r4, r6)
            int r4 = r4 + 1
            r3 = r9
            goto Lbb
        L5a:
            androidx.fragment.app.Fragment r6 = r5.f4902b
            int r8 = r6.f4963z
            int r12 = r17.size()
            int r12 = r12 - r7
            r13 = 0
        L64:
            if (r12 < 0) goto La4
            java.lang.Object r14 = r1.get(r12)
            androidx.fragment.app.Fragment r14 = (androidx.fragment.app.Fragment) r14
            int r15 = r14.f4963z
            if (r15 != r8) goto La1
            if (r14 != r6) goto L74
            r13 = r7
            goto La1
        L74:
            if (r14 != r3) goto L83
            java.util.ArrayList r3 = r0.f4884c
            androidx.fragment.app.F$a r15 = new androidx.fragment.app.F$a
            r15.<init>(r11, r14, r7)
            r3.add(r4, r15)
            int r4 = r4 + 1
            r3 = r9
        L83:
            androidx.fragment.app.F$a r15 = new androidx.fragment.app.F$a
            r15.<init>(r10, r14, r7)
            int r2 = r5.f4904d
            r15.f4904d = r2
            int r2 = r5.f4906f
            r15.f4906f = r2
            int r2 = r5.f4905e
            r15.f4905e = r2
            int r2 = r5.f4907g
            r15.f4907g = r2
            java.util.ArrayList r2 = r0.f4884c
            r2.add(r4, r15)
            r1.remove(r14)
            int r4 = r4 + r7
        La1:
            int r12 = r12 + (-1)
            goto L64
        La4:
            if (r13 == 0) goto Lae
            java.util.ArrayList r2 = r0.f4884c
            r2.remove(r4)
            int r4 = r4 + (-1)
            goto Lbb
        Lae:
            r5.f4901a = r7
            r5.f4903c = r7
            r1.add(r6)
            goto Lbb
        Lb6:
            androidx.fragment.app.Fragment r2 = r5.f4902b
            r1.add(r2)
        Lbb:
            int r4 = r4 + r7
            goto L7
        Lbe:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.C0279a.t(java.util.ArrayList, androidx.fragment.app.Fragment):androidx.fragment.app.Fragment");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("BackStackEntry{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        if (this.f5058v >= 0) {
            sb.append(" #");
            sb.append(this.f5058v);
        }
        if (this.f4892k != null) {
            sb.append(" ");
            sb.append(this.f4892k);
        }
        sb.append("}");
        return sb.toString();
    }

    public String u() {
        return this.f4892k;
    }

    public void v() {
        if (this.f4900s != null) {
            for (int i3 = 0; i3 < this.f4900s.size(); i3++) {
                ((Runnable) this.f4900s.get(i3)).run();
            }
            this.f4900s = null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0027  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x002d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    androidx.fragment.app.Fragment w(java.util.ArrayList r6, androidx.fragment.app.Fragment r7) {
        /*
            r5 = this;
            java.util.ArrayList r0 = r5.f4884c
            int r0 = r0.size()
            r1 = 1
            int r0 = r0 - r1
        L8:
            if (r0 < 0) goto L35
            java.util.ArrayList r2 = r5.f4884c
            java.lang.Object r2 = r2.get(r0)
            androidx.fragment.app.F$a r2 = (androidx.fragment.app.F.a) r2
            int r3 = r2.f4901a
            if (r3 == r1) goto L2d
            r4 = 3
            if (r3 == r4) goto L27
            switch(r3) {
                case 6: goto L27;
                case 7: goto L2d;
                case 8: goto L25;
                case 9: goto L22;
                case 10: goto L1d;
                default: goto L1c;
            }
        L1c:
            goto L32
        L1d:
            androidx.lifecycle.g$b r3 = r2.f4908h
            r2.f4909i = r3
            goto L32
        L22:
            androidx.fragment.app.Fragment r7 = r2.f4902b
            goto L32
        L25:
            r7 = 0
            goto L32
        L27:
            androidx.fragment.app.Fragment r2 = r2.f4902b
            r6.add(r2)
            goto L32
        L2d:
            androidx.fragment.app.Fragment r2 = r2.f4902b
            r6.remove(r2)
        L32:
            int r0 = r0 + (-1)
            goto L8
        L35:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.C0279a.w(java.util.ArrayList, androidx.fragment.app.Fragment):androidx.fragment.app.Fragment");
    }
}
