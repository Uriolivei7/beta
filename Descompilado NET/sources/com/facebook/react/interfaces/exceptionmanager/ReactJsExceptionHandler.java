package com.facebook.react.interfaces.exceptionmanager;

import D2.h;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableNativeMap;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface ReactJsExceptionHandler {

    public interface ProcessedError {

        public interface StackFrame {
            Integer getColumn();

            String getFile();

            Integer getLineNumber();

            String getMethodName();
        }

        String getComponentStack();

        ReadableMap getExtraData();

        int getId();

        String getMessage();

        String getName();

        String getOriginalMessage();

        List<StackFrame> getStack();

        boolean isFatal();
    }

    private static final class ProcessedErrorImpl implements ProcessedError {
        private final String componentStack;
        private final ReadableNativeMap extraData;
        private final int id;
        private final boolean isFatal;
        private final String message;
        private final String name;
        private final String originalMessage;
        private final ArrayList<ProcessedErrorStackFrameImpl> stack;

        public ProcessedErrorImpl(String str, String str2, String str3, String str4, ArrayList<ProcessedErrorStackFrameImpl> arrayList, int i3, boolean z3, ReadableNativeMap readableNativeMap) {
            h.f(str, "message");
            h.f(arrayList, "stack");
            h.f(readableNativeMap, "extraData");
            this.message = str;
            this.originalMessage = str2;
            this.name = str3;
            this.componentStack = str4;
            this.stack = arrayList;
            this.id = i3;
            this.isFatal = z3;
            this.extraData = readableNativeMap;
        }

        public final String component1() {
            return this.message;
        }

        public final String component2() {
            return this.originalMessage;
        }

        public final String component3() {
            return this.name;
        }

        public final String component4() {
            return this.componentStack;
        }

        public final ArrayList<ProcessedErrorStackFrameImpl> component5() {
            return this.stack;
        }

        public final int component6() {
            return this.id;
        }

        public final boolean component7() {
            return this.isFatal;
        }

        public final ReadableNativeMap component8() {
            return this.extraData;
        }

        public final ProcessedErrorImpl copy(String str, String str2, String str3, String str4, ArrayList<ProcessedErrorStackFrameImpl> arrayList, int i3, boolean z3, ReadableNativeMap readableNativeMap) {
            h.f(str, "message");
            h.f(arrayList, "stack");
            h.f(readableNativeMap, "extraData");
            return new ProcessedErrorImpl(str, str2, str3, str4, arrayList, i3, z3, readableNativeMap);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ProcessedErrorImpl)) {
                return false;
            }
            ProcessedErrorImpl processedErrorImpl = (ProcessedErrorImpl) obj;
            return h.b(this.message, processedErrorImpl.message) && h.b(this.originalMessage, processedErrorImpl.originalMessage) && h.b(this.name, processedErrorImpl.name) && h.b(this.componentStack, processedErrorImpl.componentStack) && h.b(this.stack, processedErrorImpl.stack) && this.id == processedErrorImpl.id && this.isFatal == processedErrorImpl.isFatal && h.b(this.extraData, processedErrorImpl.extraData);
        }

        @Override // com.facebook.react.interfaces.exceptionmanager.ReactJsExceptionHandler.ProcessedError
        public String getComponentStack() {
            return this.componentStack;
        }

        @Override // com.facebook.react.interfaces.exceptionmanager.ReactJsExceptionHandler.ProcessedError
        public int getId() {
            return this.id;
        }

        @Override // com.facebook.react.interfaces.exceptionmanager.ReactJsExceptionHandler.ProcessedError
        public String getMessage() {
            return this.message;
        }

        @Override // com.facebook.react.interfaces.exceptionmanager.ReactJsExceptionHandler.ProcessedError
        public String getName() {
            return this.name;
        }

        @Override // com.facebook.react.interfaces.exceptionmanager.ReactJsExceptionHandler.ProcessedError
        public String getOriginalMessage() {
            return this.originalMessage;
        }

        public int hashCode() {
            int iHashCode = this.message.hashCode() * 31;
            String str = this.originalMessage;
            int iHashCode2 = (iHashCode + (str == null ? 0 : str.hashCode())) * 31;
            String str2 = this.name;
            int iHashCode3 = (iHashCode2 + (str2 == null ? 0 : str2.hashCode())) * 31;
            String str3 = this.componentStack;
            return ((((((((iHashCode3 + (str3 != null ? str3.hashCode() : 0)) * 31) + this.stack.hashCode()) * 31) + Integer.hashCode(this.id)) * 31) + Boolean.hashCode(this.isFatal)) * 31) + this.extraData.hashCode();
        }

        @Override // com.facebook.react.interfaces.exceptionmanager.ReactJsExceptionHandler.ProcessedError
        public boolean isFatal() {
            return this.isFatal;
        }

        public String toString() {
            return "ProcessedErrorImpl(message=" + this.message + ", originalMessage=" + this.originalMessage + ", name=" + this.name + ", componentStack=" + this.componentStack + ", stack=" + this.stack + ", id=" + this.id + ", isFatal=" + this.isFatal + ", extraData=" + this.extraData + ")";
        }

        @Override // com.facebook.react.interfaces.exceptionmanager.ReactJsExceptionHandler.ProcessedError
        public ReadableNativeMap getExtraData() {
            return this.extraData;
        }

        @Override // com.facebook.react.interfaces.exceptionmanager.ReactJsExceptionHandler.ProcessedError
        public ArrayList<ProcessedErrorStackFrameImpl> getStack() {
            return this.stack;
        }
    }

    private static final class ProcessedErrorStackFrameImpl implements ProcessedError.StackFrame {
        private final Integer column;
        private final String file;
        private final Integer lineNumber;
        private final String methodName;

        public ProcessedErrorStackFrameImpl(String str, String str2, Integer num, Integer num2) {
            h.f(str2, "methodName");
            this.file = str;
            this.methodName = str2;
            this.lineNumber = num;
            this.column = num2;
        }

        public static /* synthetic */ ProcessedErrorStackFrameImpl copy$default(ProcessedErrorStackFrameImpl processedErrorStackFrameImpl, String str, String str2, Integer num, Integer num2, int i3, Object obj) {
            if ((i3 & 1) != 0) {
                str = processedErrorStackFrameImpl.file;
            }
            if ((i3 & 2) != 0) {
                str2 = processedErrorStackFrameImpl.methodName;
            }
            if ((i3 & 4) != 0) {
                num = processedErrorStackFrameImpl.lineNumber;
            }
            if ((i3 & 8) != 0) {
                num2 = processedErrorStackFrameImpl.column;
            }
            return processedErrorStackFrameImpl.copy(str, str2, num, num2);
        }

        public final String component1() {
            return this.file;
        }

        public final String component2() {
            return this.methodName;
        }

        public final Integer component3() {
            return this.lineNumber;
        }

        public final Integer component4() {
            return this.column;
        }

        public final ProcessedErrorStackFrameImpl copy(String str, String str2, Integer num, Integer num2) {
            h.f(str2, "methodName");
            return new ProcessedErrorStackFrameImpl(str, str2, num, num2);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ProcessedErrorStackFrameImpl)) {
                return false;
            }
            ProcessedErrorStackFrameImpl processedErrorStackFrameImpl = (ProcessedErrorStackFrameImpl) obj;
            return h.b(this.file, processedErrorStackFrameImpl.file) && h.b(this.methodName, processedErrorStackFrameImpl.methodName) && h.b(this.lineNumber, processedErrorStackFrameImpl.lineNumber) && h.b(this.column, processedErrorStackFrameImpl.column);
        }

        @Override // com.facebook.react.interfaces.exceptionmanager.ReactJsExceptionHandler.ProcessedError.StackFrame
        public Integer getColumn() {
            return this.column;
        }

        @Override // com.facebook.react.interfaces.exceptionmanager.ReactJsExceptionHandler.ProcessedError.StackFrame
        public String getFile() {
            return this.file;
        }

        @Override // com.facebook.react.interfaces.exceptionmanager.ReactJsExceptionHandler.ProcessedError.StackFrame
        public Integer getLineNumber() {
            return this.lineNumber;
        }

        @Override // com.facebook.react.interfaces.exceptionmanager.ReactJsExceptionHandler.ProcessedError.StackFrame
        public String getMethodName() {
            return this.methodName;
        }

        public int hashCode() {
            String str = this.file;
            int iHashCode = (((str == null ? 0 : str.hashCode()) * 31) + this.methodName.hashCode()) * 31;
            Integer num = this.lineNumber;
            int iHashCode2 = (iHashCode + (num == null ? 0 : num.hashCode())) * 31;
            Integer num2 = this.column;
            return iHashCode2 + (num2 != null ? num2.hashCode() : 0);
        }

        public String toString() {
            return "ProcessedErrorStackFrameImpl(file=" + this.file + ", methodName=" + this.methodName + ", lineNumber=" + this.lineNumber + ", column=" + this.column + ")";
        }
    }

    void reportJsException(ProcessedError processedError);
}
