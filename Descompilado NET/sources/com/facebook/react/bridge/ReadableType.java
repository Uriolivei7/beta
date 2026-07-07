package com.facebook.react.bridge;

import kotlin.enums.EnumEntries;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: loaded from: classes.dex */
public final class ReadableType {
    private static final /* synthetic */ EnumEntries $ENTRIES;
    private static final /* synthetic */ ReadableType[] $VALUES;
    public static final ReadableType Null = new ReadableType("Null", 0);
    public static final ReadableType Boolean = new ReadableType("Boolean", 1);
    public static final ReadableType Number = new ReadableType("Number", 2);
    public static final ReadableType String = new ReadableType("String", 3);
    public static final ReadableType Map = new ReadableType("Map", 4);
    public static final ReadableType Array = new ReadableType("Array", 5);

    private static final /* synthetic */ ReadableType[] $values() {
        return new ReadableType[]{Null, Boolean, Number, String, Map, Array};
    }

    static {
        ReadableType[] readableTypeArr$values = $values();
        $VALUES = readableTypeArr$values;
        $ENTRIES = AbstractC0764a.a(readableTypeArr$values);
    }

    private ReadableType(String str, int i3) {
    }

    public static EnumEntries getEntries() {
        return $ENTRIES;
    }

    public static ReadableType valueOf(String str) {
        return (ReadableType) Enum.valueOf(ReadableType.class, str);
    }

    public static ReadableType[] values() {
        return (ReadableType[]) $VALUES.clone();
    }
}
