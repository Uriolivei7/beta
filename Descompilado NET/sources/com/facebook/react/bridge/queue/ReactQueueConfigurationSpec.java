package com.facebook.react.bridge.queue;

import D2.h;
import com.facebook.react.bridge.queue.MessageQueueThreadSpec;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class ReactQueueConfigurationSpec {
    public static final Companion Companion = new Companion(null);
    private final MessageQueueThreadSpec jSQueueThreadSpec;
    private final MessageQueueThreadSpec nativeModulesQueueThreadSpec;

    public static final class Builder {
        private MessageQueueThreadSpec jsQueueSpec;
        private MessageQueueThreadSpec nativeModulesQueueSpec;

        public final ReactQueueConfigurationSpec build() {
            MessageQueueThreadSpec messageQueueThreadSpec = this.nativeModulesQueueSpec;
            if (messageQueueThreadSpec == null) {
                throw new IllegalStateException("Required value was null.");
            }
            MessageQueueThreadSpec messageQueueThreadSpec2 = this.jsQueueSpec;
            if (messageQueueThreadSpec2 != null) {
                return new ReactQueueConfigurationSpec(messageQueueThreadSpec, messageQueueThreadSpec2);
            }
            throw new IllegalStateException("Required value was null.");
        }

        public final Builder setJSQueueThreadSpec(MessageQueueThreadSpec messageQueueThreadSpec) {
            if (this.jsQueueSpec != null) {
                throw new IllegalStateException("Setting JS queue multiple times!");
            }
            this.jsQueueSpec = messageQueueThreadSpec;
            return this;
        }

        public final Builder setNativeModulesQueueThreadSpec(MessageQueueThreadSpec messageQueueThreadSpec) {
            if (this.nativeModulesQueueSpec != null) {
                throw new IllegalStateException("Setting native modules queue spec multiple times!");
            }
            this.nativeModulesQueueSpec = messageQueueThreadSpec;
            return this;
        }
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Builder builder() {
            return new Builder();
        }

        public final ReactQueueConfigurationSpec createDefault() {
            MessageQueueThreadSpec.Companion companion = MessageQueueThreadSpec.Companion;
            return new ReactQueueConfigurationSpec(companion.newBackgroundThreadSpec("native_modules"), companion.newBackgroundThreadSpec("js"));
        }

        private Companion() {
        }
    }

    public ReactQueueConfigurationSpec(MessageQueueThreadSpec messageQueueThreadSpec, MessageQueueThreadSpec messageQueueThreadSpec2) {
        h.f(messageQueueThreadSpec, "nativeModulesQueueThreadSpec");
        h.f(messageQueueThreadSpec2, "jSQueueThreadSpec");
        this.nativeModulesQueueThreadSpec = messageQueueThreadSpec;
        this.jSQueueThreadSpec = messageQueueThreadSpec2;
    }

    public static final ReactQueueConfigurationSpec createDefault() {
        return Companion.createDefault();
    }

    public final MessageQueueThreadSpec getJSQueueThreadSpec() {
        return this.jSQueueThreadSpec;
    }

    public final MessageQueueThreadSpec getNativeModulesQueueThreadSpec() {
        return this.nativeModulesQueueThreadSpec;
    }
}
