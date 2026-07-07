package com.facebook.react.runtime;

import a1.C0210a;
import java.util.Objects;

/* JADX INFO: renamed from: com.facebook.react.runtime.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
class C0393a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    volatile Object f7149a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    Object f7150b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private volatile b f7151c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private volatile String f7152d;

    /* JADX INFO: renamed from: com.facebook.react.runtime.a$a, reason: collision with other inner class name */
    interface InterfaceC0109a {
        Object get();
    }

    /* JADX INFO: renamed from: com.facebook.react.runtime.a$b */
    enum b {
        Init,
        Creating,
        Success,
        Failure
    }

    public C0393a(Object obj) {
        this.f7149a = obj;
        this.f7150b = obj;
        this.f7151c = b.Init;
        this.f7152d = "";
    }

    public synchronized Object a() {
        return C0210a.c(this.f7149a);
    }

    public synchronized Object b() {
        Object objA;
        objA = a();
        e();
        return objA;
    }

    public synchronized Object c() {
        return this.f7149a;
    }

    public Object d(InterfaceC0109a interfaceC0109a) {
        boolean z3;
        Object objA;
        Object objA2;
        synchronized (this) {
            try {
                b bVar = this.f7151c;
                b bVar2 = b.Success;
                if (bVar == bVar2) {
                    return a();
                }
                if (this.f7151c == b.Failure) {
                    throw new RuntimeException("BridgelessAtomicRef: Failed to create object. Reason: " + this.f7152d);
                }
                b bVar3 = this.f7151c;
                b bVar4 = b.Creating;
                boolean z4 = false;
                if (bVar3 != bVar4) {
                    this.f7151c = bVar4;
                    z3 = true;
                } else {
                    z3 = false;
                }
                if (z3) {
                    try {
                        this.f7149a = interfaceC0109a.get();
                        synchronized (this) {
                            this.f7151c = bVar2;
                            notifyAll();
                            objA = a();
                        }
                        return objA;
                    } catch (RuntimeException e4) {
                        synchronized (this) {
                            this.f7151c = b.Failure;
                            this.f7152d = Objects.toString(e4.getMessage(), "null");
                            notifyAll();
                            throw new RuntimeException("BridgelessAtomicRef: Failed to create object.", e4);
                        }
                    }
                }
                synchronized (this) {
                    while (this.f7151c == b.Creating) {
                        try {
                            wait();
                        } catch (InterruptedException unused) {
                            z4 = true;
                        }
                    }
                    if (z4) {
                        Thread.currentThread().interrupt();
                    }
                    if (this.f7151c == b.Failure) {
                        throw new RuntimeException("BridgelessAtomicRef: Failed to create object. Reason: " + this.f7152d);
                    }
                    objA2 = a();
                }
                return objA2;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public synchronized void e() {
        this.f7149a = this.f7150b;
        this.f7151c = b.Init;
        this.f7152d = "";
    }

    public C0393a() {
        this(null);
    }
}
