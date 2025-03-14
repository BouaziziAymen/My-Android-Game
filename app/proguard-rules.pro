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
-keep class com.badlogic.gdx.physics.box2d.World{
    *;
}

# Keep all classes in the com.example package
-keep class com.evolgames.dollmutilationgame.entities.properties.** { *; }
-keep class com.evolgames.dollmutilationgame.entities.serialization.infos.** { *; }
-keep class com.evolgames.dollmutilationgame.entities.serialization.serializers.** { *; }
-keep class com.evolgames.dollmutilationgame.userinterface.model.** { *; }
-keep class com.evolgames.dollmutilationgame.entities.hand.** { *; }
-keep class com.evolgames.dollmutilationgame.entities.usage.** { *; }
-keep class com.evolgames.dollmutilationgame.entities.init.BodyInitImpl
-keep class com.evolgames.dollmutilationgame.entities.basics.SpecialEntityType { *; }
-keep class com.evolgames.dollmutilationgame.entities.basics.GroupType { *; }
-keep class com.evolgames.dollmutilationgame.entities.commandtemplate.EntityDestructionCommand
-keep class com.evolgames.dollmutilationgame.entities.contact.Pair
-keep class com.evolgames.dollmutilationgame.entities.cut.PointsFreshCut
-keep class com.evolgames.dollmutilationgame.entities.cut.CutPoint
-keep class com.evolgames.dollmutilationgame.entities.cut.SegmentFreshCut