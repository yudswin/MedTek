# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class androidx.hilt.** { *; }
-keep class com.medtek.main.Hilt_** { *; }
-keep @dagger.hilt.codegen.** class * { *; }
-keep @dagger.hilt.android.** class * { *; }

# Keep annotations
-keepattributes *Annotation*

# Prevent obfuscation of generated classes
-keepnames class com.medtek.main.** { *; }
-keepnames class * extends dagger.hilt.android.HiltAndroidApp
-keepnames class * extends dagger.hilt.android.AndroidEntryPoint
