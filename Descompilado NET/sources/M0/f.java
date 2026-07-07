package M0;

import O0.j;
import X.k;
import X.p;
import a0.InterfaceC0207a;
import f0.C0533d;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public class f {

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private boolean f884g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final InterfaceC0207a f885h;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f880c = 0;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f879b = 0;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private int f881d = 0;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private int f883f = 0;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private int f882e = 0;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private int f878a = 0;

    public f(InterfaceC0207a interfaceC0207a) {
        this.f885h = (InterfaceC0207a) k.g(interfaceC0207a);
    }

    private boolean a(InputStream inputStream) throws Throwable {
        int i3;
        int i4 = this.f882e;
        while (this.f878a != 6 && (i3 = inputStream.read()) != -1) {
            try {
                int i5 = this.f880c;
                this.f880c = i5 + 1;
                if (this.f884g) {
                    this.f878a = 6;
                    this.f884g = false;
                    return false;
                }
                int i6 = this.f878a;
                if (i6 != 0) {
                    if (i6 != 1) {
                        if (i6 != 2) {
                            if (i6 != 3) {
                                if (i6 == 4) {
                                    this.f878a = 5;
                                } else if (i6 != 5) {
                                    k.i(false);
                                } else {
                                    int i7 = ((this.f879b << 8) + i3) - 2;
                                    C0533d.a(inputStream, i7);
                                    this.f880c += i7;
                                    this.f878a = 2;
                                }
                            } else if (i3 == 255) {
                                this.f878a = 3;
                            } else if (i3 == 0) {
                                this.f878a = 2;
                            } else if (i3 == 217) {
                                this.f884g = true;
                                f(i5 - 1);
                                this.f878a = 2;
                            } else {
                                if (i3 == 218) {
                                    f(i5 - 1);
                                }
                                if (b(i3)) {
                                    this.f878a = 4;
                                } else {
                                    this.f878a = 2;
                                }
                            }
                        } else if (i3 == 255) {
                            this.f878a = 3;
                        }
                    } else if (i3 == 216) {
                        this.f878a = 2;
                    } else {
                        this.f878a = 6;
                    }
                } else if (i3 == 255) {
                    this.f878a = 1;
                } else {
                    this.f878a = 6;
                }
                this.f879b = i3;
            } catch (IOException e4) {
                p.a(e4);
            }
            return (this.f878a == 6 || this.f882e == i4) ? false : true;
        }
        if (this.f878a == 6) {
            return false;
        }
    }

    private static boolean b(int i3) {
        if (i3 == 1) {
            return false;
        }
        return ((i3 >= 208 && i3 <= 215) || i3 == 217 || i3 == 216) ? false : true;
    }

    private void f(int i3) {
        int i4 = this.f881d;
        if (i4 > 0) {
            this.f883f = i3;
        }
        this.f881d = i4 + 1;
        this.f882e = i4;
    }

    public int c() {
        return this.f883f;
    }

    public int d() {
        return this.f882e;
    }

    public boolean e() {
        return this.f884g;
    }

    public boolean g(j jVar) {
        if (this.f878a == 6 || jVar.c0() <= this.f880c) {
            return false;
        }
        a0.g gVar = new a0.g(jVar.X(), (byte[]) this.f885h.get(16384), this.f885h);
        try {
            C0533d.a(gVar, this.f880c);
            return a(gVar);
        } catch (IOException e4) {
            p.a(e4);
            return false;
        } finally {
            X.b.b(gVar);
        }
    }
}
