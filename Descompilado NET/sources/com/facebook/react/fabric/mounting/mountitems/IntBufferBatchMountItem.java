package com.facebook.react.fabric.mounting.mountitems;

import com.facebook.react.bridge.ReactMarker;
import com.facebook.react.bridge.ReactMarkerConstants;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.fabric.FabricUIManager;
import com.facebook.react.fabric.events.EventEmitterWrapper;
import com.facebook.react.uimanager.A0;
import d2.C0518a;
import r1.C0670b;

/* JADX INFO: loaded from: classes.dex */
final class IntBufferBatchMountItem implements a {

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    static final String f6838g = "IntBufferBatchMountItem";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f6839a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f6840b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int[] f6841c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Object[] f6842d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final int f6843e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final int f6844f;

    IntBufferBatchMountItem(int i3, int[] iArr, Object[] objArr, int i4) {
        this.f6839a = i3;
        this.f6840b = i4;
        this.f6841c = iArr;
        this.f6842d = objArr;
        this.f6843e = iArr.length;
        this.f6844f = objArr.length;
    }

    private void b(String str) {
        C0518a.c(0L, "IntBufferBatchMountItem::" + str);
        int i3 = this.f6840b;
        if (i3 > 0) {
            ReactMarker.logFabricMarker(ReactMarkerConstants.FABRIC_BATCH_EXECUTION_START, null, i3);
        }
    }

    private void c() {
        int i3 = this.f6840b;
        if (i3 > 0) {
            ReactMarker.logFabricMarker(ReactMarkerConstants.FABRIC_BATCH_EXECUTION_END, null, i3);
        }
        C0518a.i(0L);
    }

    private static String d(int i3) {
        return i3 == 2 ? "CREATE" : i3 == 4 ? "DELETE" : i3 == 8 ? "INSERT" : i3 == 16 ? "REMOVE" : i3 == 32 ? "UPDATE_PROPS" : i3 == 64 ? "UPDATE_STATE" : i3 == 128 ? "UPDATE_LAYOUT" : i3 == 512 ? "UPDATE_PADDING" : i3 == 1024 ? "UPDATE_OVERFLOW_INSET" : i3 == 256 ? "UPDATE_EVENT_EMITTER" : "UNKNOWN";
    }

    @Override // com.facebook.react.fabric.mounting.mountitems.a
    public boolean a() {
        return this.f6843e == 0;
    }

    @Override // com.facebook.react.fabric.mounting.mountitems.MountItem
    public void execute(n1.d dVar) {
        int i3;
        int i4;
        long j3;
        int i5;
        int i6;
        int i7;
        n1.g gVarF = dVar.f(this.f6839a);
        if (gVarF == null) {
            Y.a.o(f6838g, "Skipping batch of MountItems; no SurfaceMountingManager found for [%d].", Integer.valueOf(this.f6839a));
            return;
        }
        if (gVarF.u()) {
            Y.a.o(f6838g, "Skipping batch of MountItems; was stopped [%d].", Integer.valueOf(this.f6839a));
            return;
        }
        if (C0670b.e()) {
            Y.a.c(f6838g, "Executing IntBufferBatchMountItem on surface [%d]", Integer.valueOf(this.f6839a));
        }
        b("mountViews");
        int i8 = 0;
        int i9 = 0;
        while (i8 < this.f6843e) {
            int[] iArr = this.f6841c;
            int i10 = i8 + 1;
            int i11 = iArr[i8];
            int i12 = i11 & (-2);
            if ((i11 & 1) != 0) {
                int i13 = iArr[i10];
                i10 = i8 + 2;
                i3 = i13;
            } else {
                i3 = 1;
            }
            long j4 = 0;
            C0518a.d(0L, "IntBufferBatchMountItem::mountInstructions::" + d(i12), new String[]{"numInstructions", String.valueOf(i3)}, 2);
            int i14 = i9;
            i8 = i10;
            int i15 = 0;
            while (i15 < i3) {
                if (i12 == 2) {
                    String strA = f.a((String) this.f6842d[i14]);
                    int[] iArr2 = this.f6841c;
                    int i16 = iArr2[i8];
                    Object[] objArr = this.f6842d;
                    ReadableMap readableMap = (ReadableMap) objArr[i14 + 1];
                    int i17 = i14 + 3;
                    A0 a02 = (A0) objArr[i14 + 2];
                    i14 += 4;
                    int i18 = i8 + 2;
                    i4 = i15;
                    gVarF.g(strA, i16, readableMap, a02, (EventEmitterWrapper) objArr[i17], iArr2[i8 + 1] == 1);
                    i8 = i18;
                } else {
                    i4 = i15;
                    if (i12 == 4) {
                        gVarF.i(this.f6841c[i8]);
                        i8++;
                    } else if (i12 == 8) {
                        int[] iArr3 = this.f6841c;
                        int i19 = iArr3[i8];
                        int i20 = i8 + 2;
                        int i21 = iArr3[i8 + 1];
                        i8 += 3;
                        gVarF.e(i21, i19, iArr3[i20]);
                    } else if (i12 == 16) {
                        int[] iArr4 = this.f6841c;
                        int i22 = iArr4[i8];
                        int i23 = i8 + 2;
                        int i24 = iArr4[i8 + 1];
                        i8 += 3;
                        gVarF.E(i22, i24, iArr4[i23]);
                    } else {
                        if (i12 == 32) {
                            i6 = i8 + 1;
                            i7 = i14 + 1;
                            gVarF.O(this.f6841c[i8], (ReadableMap) this.f6842d[i14]);
                        } else if (i12 == 64) {
                            i6 = i8 + 1;
                            i7 = i14 + 1;
                            gVarF.P(this.f6841c[i8], (A0) this.f6842d[i14]);
                        } else {
                            if (i12 == 128) {
                                int[] iArr5 = this.f6841c;
                                j3 = 0;
                                gVarF.L(iArr5[i8], iArr5[i8 + 1], iArr5[i8 + 2], iArr5[i8 + 3], iArr5[i8 + 4], iArr5[i8 + 5], iArr5[i8 + 6], iArr5[i8 + 7]);
                                i8 += 8;
                            } else {
                                j3 = 0;
                                if (i12 == 512) {
                                    int[] iArr6 = this.f6841c;
                                    i5 = i8 + 5;
                                    gVarF.N(iArr6[i8], iArr6[i8 + 1], iArr6[i8 + 2], iArr6[i8 + 3], iArr6[i8 + 4]);
                                } else if (i12 == 1024) {
                                    int[] iArr7 = this.f6841c;
                                    i5 = i8 + 5;
                                    gVarF.M(iArr7[i8], iArr7[i8 + 1], iArr7[i8 + 2], iArr7[i8 + 3], iArr7[i8 + 4]);
                                } else {
                                    if (i12 != 256) {
                                        throw new IllegalArgumentException("Invalid type argument to IntBufferBatchMountItem: " + i12 + " at index: " + i8);
                                    }
                                    gVarF.K(this.f6841c[i8], (EventEmitterWrapper) this.f6842d[i14]);
                                    i8++;
                                    i14++;
                                }
                                i8 = i5;
                            }
                            i15 = i4 + 1;
                            j4 = j3;
                        }
                        i8 = i6;
                        i14 = i7;
                    }
                }
                j3 = 0;
                i15 = i4 + 1;
                j4 = j3;
            }
            C0518a.i(j4);
            i9 = i14;
        }
        c();
    }

    @Override // com.facebook.react.fabric.mounting.mountitems.MountItem
    public int getSurfaceId() {
        return this.f6839a;
    }

    public String toString() {
        int i3;
        int i4;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("IntBufferBatchMountItem [surface:%d]:\n", Integer.valueOf(this.f6839a)));
            int i5 = 0;
            int i6 = 0;
            while (i5 < this.f6843e) {
                int[] iArr = this.f6841c;
                int i7 = i5 + 1;
                int i8 = iArr[i5];
                int i9 = i8 & (-2);
                int i10 = 1;
                if ((i8 & 1) != 0) {
                    i10 = iArr[i7];
                    i7 = i5 + 2;
                }
                i5 = i7;
                for (int i11 = 0; i11 < i10; i11++) {
                    if (i9 == 2) {
                        String strA = f.a((String) this.f6842d[i6]);
                        i6 += 4;
                        int i12 = i5 + 1;
                        Integer numValueOf = Integer.valueOf(this.f6841c[i5]);
                        i5 += 2;
                        sb.append(String.format("CREATE [%d] - layoutable:%d - %s\n", numValueOf, Integer.valueOf(this.f6841c[i12]), strA));
                    } else if (i9 == 4) {
                        sb.append(String.format("DELETE [%d]\n", Integer.valueOf(this.f6841c[i5])));
                        i5++;
                    } else if (i9 == 8) {
                        Integer numValueOf2 = Integer.valueOf(this.f6841c[i5]);
                        int i13 = i5 + 2;
                        Integer numValueOf3 = Integer.valueOf(this.f6841c[i5 + 1]);
                        i5 += 3;
                        sb.append(String.format("INSERT [%d]->[%d] @%d\n", numValueOf2, numValueOf3, Integer.valueOf(this.f6841c[i13])));
                    } else if (i9 == 16) {
                        Integer numValueOf4 = Integer.valueOf(this.f6841c[i5]);
                        int i14 = i5 + 2;
                        Integer numValueOf5 = Integer.valueOf(this.f6841c[i5 + 1]);
                        i5 += 3;
                        sb.append(String.format("REMOVE [%d]->[%d] @%d\n", numValueOf4, numValueOf5, Integer.valueOf(this.f6841c[i14])));
                    } else {
                        if (i9 == 32) {
                            i3 = i6 + 1;
                            Object obj = this.f6842d[i6];
                            i4 = i5 + 1;
                            sb.append(String.format("UPDATE PROPS [%d]: %s\n", Integer.valueOf(this.f6841c[i5]), FabricUIManager.IS_DEVELOPMENT_ENVIRONMENT ? obj != null ? obj.toString() : "<null>" : "<hidden>"));
                        } else if (i9 == 64) {
                            i3 = i6 + 1;
                            A0 a02 = (A0) this.f6842d[i6];
                            i4 = i5 + 1;
                            sb.append(String.format("UPDATE STATE [%d]: %s\n", Integer.valueOf(this.f6841c[i5]), FabricUIManager.IS_DEVELOPMENT_ENVIRONMENT ? a02 != null ? a02.toString() : "<null>" : "<hidden>"));
                        } else if (i9 == 128) {
                            int[] iArr2 = this.f6841c;
                            int i15 = iArr2[i5];
                            int i16 = iArr2[i5 + 1];
                            int i17 = iArr2[i5 + 2];
                            int i18 = iArr2[i5 + 3];
                            int i19 = iArr2[i5 + 4];
                            int i20 = iArr2[i5 + 5];
                            int i21 = i5 + 7;
                            int i22 = iArr2[i5 + 6];
                            i5 += 8;
                            sb.append(String.format("UPDATE LAYOUT [%d]->[%d]: x:%d y:%d w:%d h:%d displayType:%d layoutDirection: %d\n", Integer.valueOf(i16), Integer.valueOf(i15), Integer.valueOf(i17), Integer.valueOf(i18), Integer.valueOf(i19), Integer.valueOf(i20), Integer.valueOf(i22), Integer.valueOf(iArr2[i21])));
                        } else if (i9 == 512) {
                            Integer numValueOf6 = Integer.valueOf(this.f6841c[i5]);
                            Integer numValueOf7 = Integer.valueOf(this.f6841c[i5 + 1]);
                            Integer numValueOf8 = Integer.valueOf(this.f6841c[i5 + 2]);
                            int i23 = i5 + 4;
                            Integer numValueOf9 = Integer.valueOf(this.f6841c[i5 + 3]);
                            i5 += 5;
                            sb.append(String.format("UPDATE PADDING [%d]: top:%d right:%d bottom:%d left:%d\n", numValueOf6, numValueOf7, numValueOf8, numValueOf9, Integer.valueOf(this.f6841c[i23])));
                        } else if (i9 == 1024) {
                            Integer numValueOf10 = Integer.valueOf(this.f6841c[i5]);
                            Integer numValueOf11 = Integer.valueOf(this.f6841c[i5 + 1]);
                            Integer numValueOf12 = Integer.valueOf(this.f6841c[i5 + 2]);
                            int i24 = i5 + 4;
                            Integer numValueOf13 = Integer.valueOf(this.f6841c[i5 + 3]);
                            i5 += 5;
                            sb.append(String.format("UPDATE OVERFLOWINSET [%d]: left:%d top:%d right:%d bottom:%d\n", numValueOf10, numValueOf11, numValueOf12, numValueOf13, Integer.valueOf(this.f6841c[i24])));
                        } else {
                            if (i9 != 256) {
                                Y.a.m(f6838g, "String so far: " + sb.toString());
                                throw new IllegalArgumentException("Invalid type argument to IntBufferBatchMountItem: " + i9 + " at index: " + i5);
                            }
                            i6++;
                            sb.append(String.format("UPDATE EVENTEMITTER [%d]\n", Integer.valueOf(this.f6841c[i5])));
                            i5++;
                        }
                        i5 = i4;
                        i6 = i3;
                    }
                }
            }
            return sb.toString();
        } catch (Exception e4) {
            Y.a.n(f6838g, "Caught exception trying to print", e4);
            StringBuilder sb2 = new StringBuilder();
            for (int i25 = 0; i25 < this.f6843e; i25++) {
                sb2.append(this.f6841c[i25]);
                sb2.append(", ");
            }
            Y.a.m(f6838g, sb2.toString());
            for (int i26 = 0; i26 < this.f6844f; i26++) {
                String str = f6838g;
                Object obj2 = this.f6842d[i26];
                Y.a.m(str, obj2 != null ? obj2.toString() : "null");
            }
            return "";
        }
    }
}
