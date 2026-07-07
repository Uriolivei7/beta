package com.facebook.react.devsupport;

import java.io.EOFException;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
class V {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final b3.k f6660a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final String f6661b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private long f6662c;

    public interface a {
        void a(Map map, long j3, long j4);

        void b(Map map, b3.i iVar, boolean z3);
    }

    public V(b3.k kVar, String str) {
        this.f6660a = kVar;
        this.f6661b = str;
    }

    private void a(b3.i iVar, boolean z3, a aVar) throws EOFException {
        long jD0 = iVar.d0(b3.l.e("\r\n\r\n"));
        if (jD0 == -1) {
            aVar.b(null, iVar, z3);
            return;
        }
        b3.i iVar2 = new b3.i();
        b3.i iVar3 = new b3.i();
        iVar.x(iVar2, jD0);
        iVar.s(r0.v());
        iVar.S(iVar3);
        aVar.b(c(iVar2), iVar3, z3);
    }

    private void b(Map map, long j3, boolean z3, a aVar) {
        if (map == null || aVar == null) {
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis - this.f6662c > 16 || z3) {
            this.f6662c = jCurrentTimeMillis;
            aVar.a(map, j3, map.get("Content-Length") != null ? Long.parseLong((String) map.get("Content-Length")) : 0L);
        }
    }

    private Map c(b3.i iVar) {
        HashMap map = new HashMap();
        for (String str : iVar.O().split("\r\n")) {
            int iIndexOf = str.indexOf(":");
            if (iIndexOf != -1) {
                map.put(str.substring(0, iIndexOf).trim(), str.substring(iIndexOf + 1).trim());
            }
        }
        return map;
    }

    public boolean d(a aVar) throws EOFException {
        boolean z3;
        long j3;
        b3.l lVarE = b3.l.e("\r\n--" + this.f6661b + "\r\n");
        b3.l lVarE2 = b3.l.e("\r\n--" + this.f6661b + "--\r\n");
        b3.l lVarE3 = b3.l.e("\r\n\r\n");
        b3.i iVar = new b3.i();
        long j4 = 0L;
        long jV = 0L;
        long jF0 = 0L;
        Map mapC = null;
        while (true) {
            long jMax = Math.max(j4 - ((long) lVarE2.v()), jV);
            long jE0 = iVar.e0(lVarE, jMax);
            if (jE0 == -1) {
                jE0 = iVar.e0(lVarE2, jMax);
                z3 = true;
            } else {
                z3 = false;
            }
            if (jE0 == -1) {
                long jF02 = iVar.F0();
                if (mapC == null) {
                    long jE02 = iVar.e0(lVarE3, jMax);
                    if (jE02 >= 0) {
                        this.f6660a.x(iVar, jE02);
                        b3.i iVar2 = new b3.i();
                        j3 = jV;
                        iVar.D(iVar2, jMax, jE02 - jMax);
                        jF0 = iVar2.F0() + ((long) lVarE3.v());
                        mapC = c(iVar2);
                    } else {
                        j3 = jV;
                    }
                } else {
                    j3 = jV;
                    b(mapC, iVar.F0() - jF0, false, aVar);
                }
                if (this.f6660a.x(iVar, 4096) <= 0) {
                    return false;
                }
                j4 = jF02;
                jV = j3;
            } else {
                long j5 = jV;
                long j6 = jE0 - j5;
                if (j5 > 0) {
                    b3.i iVar3 = new b3.i();
                    iVar.s(j5);
                    iVar.x(iVar3, j6);
                    b(mapC, iVar3.F0() - jF0, true, aVar);
                    a(iVar3, z3, aVar);
                    jF0 = 0;
                    mapC = null;
                } else {
                    iVar.s(jE0);
                }
                if (z3) {
                    return true;
                }
                jV = lVarE.v();
                j4 = jV;
            }
        }
    }
}
