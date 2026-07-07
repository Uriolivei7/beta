package com.facebook.imagepipeline.memory;

import R0.E;
import R0.F;

/* JADX INFO: loaded from: classes.dex */
public class NativeMemoryChunkPool extends f {
    public NativeMemoryChunkPool(a0.d dVar, E e4, F f3) {
        super(dVar, e4, f3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.imagepipeline.memory.a
    /* JADX INFO: renamed from: D, reason: merged with bridge method [inline-methods] */
    public NativeMemoryChunk f(int i3) {
        return new NativeMemoryChunk(i3);
    }
}
