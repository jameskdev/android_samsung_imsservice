# WHAT IS THIS?
These are files used to enable IMS (VoLTE) for Samsung Galaxy A21s (SM-A217N).
Original files were taken from Samsung Firmware A217NKSU6CUG1, with extensive modifications for enabling its use on AOSP ROMs.
While VoLTE has been confirmed to work on LineageOS 18.1 for A21s, additional works are needed for the following:
1. Enabling its use without setting SELinux to permissive
2. Testing it on other Android versions and other Galaxy devices
3. Enabling Video Call
4. Enabling RCS
5. Bug fixes & Stabilizations

While it has been tested only on A21s Korean version, it is likely that this will also work on other Samsung devices (albeit with some modifications.)
It's most likely to be compatible with trebilized Exynos devices. 
However, it doesn't mean that compatibility with Snapdragon or MediaTek devices is completely ruled out. As they seem to use the same RIL interface (libsec-ril), 
there is some chance that this will work with these devices (again, though, with some modifications.)

# WHAT MODIFICATIONS WERE MADE
1. Removing reference to Samsung's internal APIs (SemSystemProperties, etc)
2. Adding dependencies in the imsmanager.jar (com.samsung.android.EmergencyMode, com.samsung.android.feature)
3. Re-working imsservice so that it extends from Google's standard IMS Compat Activity namespace (android.telephony.ims.compat.*)

# HOW TO BUILD

0. REQUIREMENTS: JDK17 (Or higher, though not tested on versions other than 17), apktool, Android SDK with platform-30 (Android 11 Platform) installed, 7zip
1. Setup Environment
> export JAVA_HOME=(path to your JDK17 installation)
> export SDK_HOME=(path to your Android SDK installation. ex. ~/Android/Sdk)
> export APKTOOL_JAR_PATH=(path to the apktool.jar that you downloaded)

2. Build
> ./build.sh

3. This will generate the smali files that were used to patch imsservice.apk from Samsung Firmware A217NKSU6CUG1 for SM-A217N.

# HOW TO APPLY PATCH
The APK inside proprietary_vendor_samsung_ims has already been patched, so you only need to do this if you want to re-create the changes.
1. Obtain imsservice.apk from stock A217NKSU6CUG1 firmware
2. Decompile it using apktool
3. apply patch to smali files by running 'patch -p1 < smali_patch.diff'
4. remove following files inside smali folder:
> smali/com/sec/internal/google/-$$Lambda$GoogleImsService$GyhyR-v54YAwXDpQD-tEf5Wlrh0.smali
> smali/com/sec/internal/google/-$$Lambda$GoogleImsService$j4oR8wOS3QH8MgHgpOwnrA0XLGA.smali
> smali/com/sec/internal/google/-$$Lambda$GoogleImsService$orSTyMQHhF4e_hxk_v5pUUv6Hs0.smali
> smali/com/sec/internal/google/-$$Lambda$GoogleImsService$xMi-4NpfdpNYkU0AZXK7IS7B9uw.smali
> smali/com/sec/internal/google/-$$Lambda$htemI6hNv3kq1UVGrXpRlPIVXRU.smali
5. Generate the needed smali files by building the java files (explained above)
6. Copy over all the files inside smali_out into the decompiled APK's smali folder & overwrite all files
7. Replace AndroidManifest.xml with the one inside this repository
8. Re-build the decompiled APK and sign it using signapk with the appropriate key for your ROM.

# Shenanigans behind Samsung's IMS services
Samsung's IMS service was incompatible with AOSP ROMS, which is why VoLTE never worked on Galaxy devices running AOSP based ROMS
(such as GSI, LineageOS, /e/ os, etc.)

Instead of extending from android.telephony.ims.ImsService (or android.telephony.ims.compat.ImsService), Samsung modified the IMS apis to make it work
with the proprietary IMS implementation. This can be seen at class com.android.internal.telephony.ims.ImsServiceControllerCompat (located inside telephony-common.jar), 
which attempts to bind with "ims6" service (com.sec.internal.google.GoogleImsService)

Also, Samsung uses its own callback implementation(com.android.ims.internal.ISecImsMmTelEventListener) for sending IMS related notifications (such as incoming calls, etc) 
instead of PendingIntent used by AOSP's IMS Compat interface, so additional works were done to make it use this PendingIntent.

Moreover, Samsung's IMS services are very closely linked to Samsung's CMC (Call-and-message Continuity, also known as "Call or Text on other device") feature, making it
extra complicated. Lots of grunt work is needed to remove all the unnecessary stuff & only leave the right stuff needed for VoLTE & other necessary IMS features on Galaxy :(

