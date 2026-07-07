package com.facebook.react.bridge;

import kotlin.enums.EnumEntries;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: loaded from: classes.dex */
public final class MemoryPressure {
    private static final /* synthetic */ EnumEntries $ENTRIES;
    private static final /* synthetic */ MemoryPressure[] $VALUES;
    public static final MemoryPressure UI_HIDDEN = new MemoryPressure("UI_HIDDEN", 0);
    public static final MemoryPressure MODERATE = new MemoryPressure("MODERATE", 1);
    public static final MemoryPressure CRITICAL = new MemoryPressure("CRITICAL", 2);

    private static final /* synthetic */ MemoryPressure[] $values() {
        return new MemoryPressure[]{UI_HIDDEN, MODERATE, CRITICAL};
    }

    static {
        MemoryPressure[] memoryPressureArr$values = $values();
        $VALUES = memoryPressureArr$values;
        $ENTRIES = AbstractC0764a.a(memoryPressureArr$values);
    }

    private MemoryPressure(String str, int i3) {
    }

    public static EnumEntries getEntries() {
        return $ENTRIES;
    }

    public static MemoryPressure valueOf(String str) {
        return (MemoryPressure) Enum.valueOf(MemoryPressure.class, str);
    }

    public static MemoryPressure[] values() {
        return (MemoryPressure[]) $VALUES.clone();
    }
}
