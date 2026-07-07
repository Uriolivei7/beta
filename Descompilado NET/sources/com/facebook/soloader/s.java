package com.facebook.soloader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ClosedByInterruptException;

/* JADX INFO: loaded from: classes.dex */
public final class s {

    protected static class a extends UnsatisfiedLinkError {
        a(String str) {
            super(str);
        }
    }

    public static String[] a(h hVar) {
        return hVar instanceof i ? c((i) hVar) : b(hVar);
    }

    private static String[] b(h hVar) {
        long jG;
        long jG2;
        String str;
        long j3;
        long j4;
        long jG3;
        String str2;
        long j5;
        long jD;
        long jD2;
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(8);
        byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
        long jG4 = g(hVar, byteBufferAllocate, 0L);
        if (jG4 != 1179403647) {
            throw new a("file is not ELF: magic is 0x" + Long.toHexString(jG4) + ", it should be " + Long.toHexString(1179403647L));
        }
        boolean z3 = h(hVar, byteBufferAllocate, 4L) == 1;
        if (h(hVar, byteBufferAllocate, 5L) == 2) {
            byteBufferAllocate.order(ByteOrder.BIG_ENDIAN);
        }
        long jG5 = z3 ? g(hVar, byteBufferAllocate, 28L) : d(hVar, byteBufferAllocate, 32L);
        long jF = z3 ? f(hVar, byteBufferAllocate, 44L) : f(hVar, byteBufferAllocate, 56L);
        int iF = f(hVar, byteBufferAllocate, z3 ? 42L : 54L);
        if (jF == 65535) {
            long jG6 = z3 ? g(hVar, byteBufferAllocate, 32L) : d(hVar, byteBufferAllocate, 40L);
            jF = z3 ? g(hVar, byteBufferAllocate, jG6 + 28) : g(hVar, byteBufferAllocate, jG6 + 44);
        }
        long j6 = jG5;
        long j7 = 0;
        while (true) {
            if (j7 >= jF) {
                jG = 0;
                break;
            }
            if ((z3 ? g(hVar, byteBufferAllocate, j6) : g(hVar, byteBufferAllocate, j6)) == 2) {
                jG = z3 ? g(hVar, byteBufferAllocate, j6 + 4) : d(hVar, byteBufferAllocate, j6 + 8);
            } else {
                j6 += (long) iF;
                j7++;
            }
        }
        if (jG == 0) {
            throw new a("ELF file does not contain dynamic linking information");
        }
        long j8 = jG;
        int i3 = 0;
        long jG7 = 0;
        do {
            jG2 = z3 ? g(hVar, byteBufferAllocate, j8) : d(hVar, byteBufferAllocate, j8);
            if (jG2 == 1) {
                if (i3 == Integer.MAX_VALUE) {
                    throw new a("malformed DT_NEEDED section");
                }
                i3++;
                str = "malformed DT_NEEDED section";
            } else if (jG2 == 5) {
                str = "malformed DT_NEEDED section";
                jG7 = z3 ? g(hVar, byteBufferAllocate, j8 + 4) : d(hVar, byteBufferAllocate, j8 + 8);
            } else {
                str = "malformed DT_NEEDED section";
            }
            j8 += z3 ? 8L : 16L;
        } while (jG2 != 0);
        if (jG7 == 0) {
            throw new a("Dynamic section string-table not found");
        }
        long j9 = jG5;
        int i4 = 0;
        while (true) {
            if (i4 >= jF) {
                j3 = jG;
                j4 = 0;
                jG3 = 0;
                break;
            }
            if ((z3 ? g(hVar, byteBufferAllocate, j9) : g(hVar, byteBufferAllocate, j9)) == 1) {
                if (z3) {
                    j5 = jF;
                    jD = g(hVar, byteBufferAllocate, j9 + 8);
                } else {
                    j5 = jF;
                    jD = d(hVar, byteBufferAllocate, j9 + 16);
                }
                if (z3) {
                    j3 = jG;
                    jD2 = g(hVar, byteBufferAllocate, j9 + 20);
                } else {
                    j3 = jG;
                    jD2 = d(hVar, byteBufferAllocate, j9 + 40);
                }
                if (jD <= jG7 && jG7 < jD2 + jD) {
                    jG3 = (z3 ? g(hVar, byteBufferAllocate, j9 + 4) : d(hVar, byteBufferAllocate, j9 + 8)) + (jG7 - jD);
                    j4 = 0;
                }
            } else {
                j5 = jF;
                j3 = jG;
            }
            j9 += (long) iF;
            i4++;
            jF = j5;
            jG = j3;
        }
        if (jG3 == j4) {
            throw new a("did not find file offset of DT_STRTAB table");
        }
        String[] strArr = new String[i3];
        long j10 = j3;
        int i5 = 0;
        while (true) {
            long jG8 = z3 ? g(hVar, byteBufferAllocate, j10) : d(hVar, byteBufferAllocate, j10);
            if (jG8 == 1) {
                strArr[i5] = e(hVar, byteBufferAllocate, (z3 ? g(hVar, byteBufferAllocate, j10 + 4) : d(hVar, byteBufferAllocate, j10 + 8)) + jG3);
                if (i5 == Integer.MAX_VALUE) {
                    throw new a(str);
                }
                i5++;
                str2 = str;
            } else {
                str2 = str;
            }
            j10 += z3 ? 8L : 16L;
            if (jG8 == 0) {
                if (i5 == i3) {
                    return strArr;
                }
                throw new a(str2);
            }
            str = str2;
        }
    }

    private static String[] c(i iVar) throws ClosedByInterruptException {
        int i3 = 0;
        while (true) {
            try {
                return b(iVar);
            } catch (ClosedByInterruptException e4) {
                i3++;
                if (i3 > 4) {
                    throw e4;
                }
                Thread.interrupted();
                p.c("MinElf", "retrying extract_DT_NEEDED due to ClosedByInterruptException", e4);
                iVar.a();
            }
        }
    }

    private static long d(h hVar, ByteBuffer byteBuffer, long j3) {
        i(hVar, byteBuffer, 8, j3);
        return byteBuffer.getLong();
    }

    private static String e(h hVar, ByteBuffer byteBuffer, long j3) {
        StringBuilder sb = new StringBuilder();
        while (true) {
            long j4 = 1 + j3;
            short sH = h(hVar, byteBuffer, j3);
            if (sH == 0) {
                return sb.toString();
            }
            sb.append((char) sH);
            j3 = j4;
        }
    }

    private static int f(h hVar, ByteBuffer byteBuffer, long j3) {
        i(hVar, byteBuffer, 2, j3);
        return byteBuffer.getShort() & 65535;
    }

    private static long g(h hVar, ByteBuffer byteBuffer, long j3) {
        i(hVar, byteBuffer, 4, j3);
        return ((long) byteBuffer.getInt()) & 4294967295L;
    }

    private static short h(h hVar, ByteBuffer byteBuffer, long j3) {
        i(hVar, byteBuffer, 1, j3);
        return (short) (byteBuffer.get() & 255);
    }

    private static void i(h hVar, ByteBuffer byteBuffer, int i3, long j3) {
        int iZ;
        byteBuffer.position(0);
        byteBuffer.limit(i3);
        while (byteBuffer.remaining() > 0 && (iZ = hVar.Z(byteBuffer, j3)) != -1) {
            j3 += (long) iZ;
        }
        if (byteBuffer.remaining() > 0) {
            throw new a("ELF file truncated");
        }
        byteBuffer.position(0);
    }
}
