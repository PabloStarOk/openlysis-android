-keep class com.openlysis.data.analysis.model.message.MessageType { *; }

# SignalR library
-keep class com.microsoft.signalr.** { *; }
-keep interface com.microsoft.signalr.** { *; }

# MessagePack library
-keep class org.msgpack.** { *; }
-keep interface org.msgpack.** { *; }
-dontwarn org.msgpack.**

# Jackson library
-keep class com.fasterxml.jackson.** { *; }
-keep interface com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.databind.**
-keep @com.fasterxml.jackson.annotation.JsonCreator class *
-keep class * {
    @com.fasterxml.jackson.annotation.JsonCreator *;
}

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int d(...);
    public static int i(...);
}