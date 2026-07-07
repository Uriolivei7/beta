package com.facebook.react.bridge.queue;

import D2.h;
import kotlin.enums.EnumEntries;
import kotlin.jvm.internal.DefaultConstructorMarker;
import w2.AbstractC0764a;

/* JADX INFO: loaded from: classes.dex */
public final class MessageQueueThreadSpec {
    public static final long DEFAULT_STACK_SIZE_BYTES = 0;
    private final String name;
    private final long stackSize;
    private final ThreadType threadType;
    public static final Companion Companion = new Companion(null);
    private static final MessageQueueThreadSpec MAIN_UI_SPEC = new MessageQueueThreadSpec(ThreadType.MAIN_UI, "main_ui", 0, 4, null);

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final MessageQueueThreadSpec mainThreadSpec() {
            return MessageQueueThreadSpec.MAIN_UI_SPEC;
        }

        public final MessageQueueThreadSpec newBackgroundThreadSpec(String str) {
            h.f(str, "name");
            return new MessageQueueThreadSpec(ThreadType.NEW_BACKGROUND, str, 0L, 4, null);
        }

        public final MessageQueueThreadSpec newUIBackgroundTreadSpec(String str) {
            h.f(str, "name");
            return new MessageQueueThreadSpec(ThreadType.NEW_BACKGROUND, str, 0L, 4, null);
        }

        private Companion() {
        }

        public final MessageQueueThreadSpec newBackgroundThreadSpec(String str, long j3) {
            h.f(str, "name");
            return new MessageQueueThreadSpec(ThreadType.NEW_BACKGROUND, str, j3, null);
        }
    }

    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    /* JADX WARN: Unknown enum class pattern. Please report as an issue! */
    public static final class ThreadType {
        private static final /* synthetic */ EnumEntries $ENTRIES;
        private static final /* synthetic */ ThreadType[] $VALUES;
        public static final ThreadType MAIN_UI = new ThreadType("MAIN_UI", 0);
        public static final ThreadType NEW_BACKGROUND = new ThreadType("NEW_BACKGROUND", 1);

        private static final /* synthetic */ ThreadType[] $values() {
            return new ThreadType[]{MAIN_UI, NEW_BACKGROUND};
        }

        static {
            ThreadType[] threadTypeArr$values = $values();
            $VALUES = threadTypeArr$values;
            $ENTRIES = AbstractC0764a.a(threadTypeArr$values);
        }

        private ThreadType(String str, int i3) {
        }

        public static EnumEntries getEntries() {
            return $ENTRIES;
        }

        public static ThreadType valueOf(String str) {
            return (ThreadType) Enum.valueOf(ThreadType.class, str);
        }

        public static ThreadType[] values() {
            return (ThreadType[]) $VALUES.clone();
        }
    }

    public /* synthetic */ MessageQueueThreadSpec(ThreadType threadType, String str, long j3, DefaultConstructorMarker defaultConstructorMarker) {
        this(threadType, str, j3);
    }

    public static final MessageQueueThreadSpec mainThreadSpec() {
        return Companion.mainThreadSpec();
    }

    public static final MessageQueueThreadSpec newBackgroundThreadSpec(String str) {
        return Companion.newBackgroundThreadSpec(str);
    }

    public static final MessageQueueThreadSpec newUIBackgroundTreadSpec(String str) {
        return Companion.newUIBackgroundTreadSpec(str);
    }

    public final String getName() {
        return this.name;
    }

    public final long getStackSize() {
        return this.stackSize;
    }

    public final ThreadType getThreadType() {
        return this.threadType;
    }

    private MessageQueueThreadSpec(ThreadType threadType, String str, long j3) {
        this.threadType = threadType;
        this.name = str;
        this.stackSize = j3;
    }

    public static final MessageQueueThreadSpec newBackgroundThreadSpec(String str, long j3) {
        return Companion.newBackgroundThreadSpec(str, j3);
    }

    /* synthetic */ MessageQueueThreadSpec(ThreadType threadType, String str, long j3, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(threadType, str, (i3 & 4) != 0 ? 0L : j3);
    }
}
