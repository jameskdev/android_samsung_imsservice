# Files needed for IMS enablement on Samsung Galaxy Devices

PRODUCT_SOONG_NAMESPACES += \
    vendor/samsung/ims

PRODUCT_COPY_FILES += \
    vendor/samsung/ims/proprietary/system/bin/charon:$(TARGET_COPY_OUT_SYSTEM)/bin/charon \
    vendor/samsung/ims/proprietary/system/bin/connfwexe:$(TARGET_COPY_OUT_SYSTEM)/bin/connfwexe \
    vendor/samsung/ims/proprietary/system/bin/multiclientd:$(TARGET_COPY_OUT_SYSTEM)/bin/multiclientd \
    vendor/samsung/ims/proprietary/system/bin/imsd:$(TARGET_COPY_OUT_SYSTEM)/bin/imsd \
    vendor/samsung/ims/proprietary/system/bin/smdexe:$(TARGET_COPY_OUT_SYSTEM)/bin/smdexe

PRODUCT_COPY_FILES += \
    vendor/samsung/ims/proprietary/system/etc/epdg_apns_conf.xml:$(TARGET_COPY_OUT_SYSTEM)/etc/epdg_apns_conf.xml \
    vendor/samsung/ims/proprietary/system/etc/strongswan.conf:$(TARGET_COPY_OUT_SYSTEM)/etc/strongswan.conf
    
PRODUCT_COPY_FILES += \
    vendor/samsung/ims/proprietary/system/etc/epdg/Certification/VDS/ca.crt:$(TARGET_COPY_OUT_SYSTEM)/etc/epdg/Certification/VDS/ca.crt \
    vendor/samsung/ims/proprietary/system/etc/epdg/Certification/KDI/ca.crt:$(TARGET_COPY_OUT_SYSTEM)/etc/epdg/Certification/KDI/ca.crt \
    vendor/samsung/ims/proprietary/system/etc/epdg/Certification/AUT/ca.crt:$(TARGET_COPY_OUT_SYSTEM)/etc/epdg/Certification/AUT/ca.crt \
    vendor/samsung/ims/proprietary/system/etc/epdg/Certification/ZVV/ca_vivo_epdg.crt:$(TARGET_COPY_OUT_SYSTEM)/etc/epdg/Certification/ZVV/ca_vivo_epdg.crt \
    vendor/samsung/ims/proprietary/system/etc/epdg/Certification/XEH/ca.crt:$(TARGET_COPY_OUT_SYSTEM)/etc/epdg/Certification/XEH/ca.crt \
    vendor/samsung/ims/proprietary/system/etc/epdg/Certification/XFA/ca.crt:$(TARGET_COPY_OUT_SYSTEM)/etc/epdg/Certification/XFA/ca.crt \
    vendor/samsung/ims/proprietary/system/etc/epdg/Certification/TEL/ca.crt:$(TARGET_COPY_OUT_SYSTEM)/etc/epdg/Certification/TEL/ca.crt \
    vendor/samsung/ims/proprietary/system/etc/epdg/Certification/ZTO/ca_vivo_epdg.crt:$(TARGET_COPY_OUT_SYSTEM)/etc/epdg/Certification/ZTO/ca_vivo_epdg.crt \
    vendor/samsung/ims/proprietary/system/etc/epdg/Certification/DIGI/ca.crt:$(TARGET_COPY_OUT_SYSTEM)/etc/epdg/Certification/DIGI/ca.crt \
    vendor/samsung/ims/proprietary/system/etc/epdg/Certification/DNA/ca.crt:$(TARGET_COPY_OUT_SYSTEM)/etc/epdg/Certification/DNA/ca.crt \
    vendor/samsung/ims/proprietary/system/etc/epdg/Certification/GCF/ca.crt:$(TARGET_COPY_OUT_SYSTEM)/etc/epdg/Certification/GCF/ca.crt \
    vendor/samsung/ims/proprietary/system/etc/epdg/Certification/VDF/ca.crt:$(TARGET_COPY_OUT_SYSTEM)/etc/epdg/Certification/VDF/ca.crt

PRODUCT_COPY_FILES += \
    vendor/samsung/ims/proprietary/system/etc/init/charon.rc:$(TARGET_COPY_OUT_SYSTEM)/etc/init/charon.rc \
    vendor/samsung/ims/proprietary/system/etc/init/imsd.rc:$(TARGET_COPY_OUT_SYSTEM)/etc/init/imsd.rc \
    vendor/samsung/ims/proprietary/system/etc/init/init.rilchip.rc:$(TARGET_COPY_OUT_SYSTEM)/etc/init/init.rilchip.rc \
    vendor/samsung/ims/proprietary/system/etc/init/init.rilcommon.rc:$(TARGET_COPY_OUT_SYSTEM)/etc/init/init.rilcommon.rc \
    vendor/samsung/ims/proprietary/system/etc/init/multiclientd.rc:$(TARGET_COPY_OUT_SYSTEM)/etc/init/multiclientd.rc

PRODUCT_COPY_FILES += \
    vendor/samsung/ims/proprietary/system/etc/permissions/privapp-permissions-com.sec.imsservice.xml:$(TARGET_COPY_OUT_SYSTEM)/etc/permissions/privapp-permissions-com.sec.imsservice.xml \
    vendor/samsung/ims/proprietary/system/etc/permissions/epdgmanager_library.xml:$(TARGET_COPY_OUT_SYSTEM)/etc/permissions/epdgmanager_library.xml \
    vendor/samsung/ims/proprietary/system/etc/permissions/imsmanager_library.xml:$(TARGET_COPY_OUT_SYSTEM)/etc/permissions/imsmanager_library.xml \
    vendor/samsung/ims/proprietary/system/etc/permissions/rcsopenapi_library.xml:$(TARGET_COPY_OUT_SYSTEM)/etc/permissions/rcsopenapi_library.xml \
    vendor/samsung/ims/proprietary/system/etc/permissions/vsimservice_library.xml:$(TARGET_COPY_OUT_SYSTEM)/etc/permissions/vsimservice_library.xml 

PRODUCT_COPY_FILES += \
    vendor/samsung/ims/proprietary/system/framework/EpdgManager.jar:$(TARGET_COPY_OUT_SYSTEM)/framework/EpdgManager.jar \
    vendor/samsung/ims/proprietary/system/framework/imsmanager.jar:$(TARGET_COPY_OUT_SYSTEM)/framework/imsmanager.jar \
    vendor/samsung/ims/proprietary/system/framework/rcsopenapi.jar:$(TARGET_COPY_OUT_SYSTEM)/framework/rcsopenapi.jar \
    vendor/samsung/ims/proprietary/system/framework/vsimmanager.jar:$(TARGET_COPY_OUT_SYSTEM)/framework/vsimmanager.jar
    
PRODUCT_COPY_FILES += \
    vendor/samsung/ims/proprietary/system/lib64/libaresdns.so:$(TARGET_COPY_OUT_SYSTEM)/lib64/libaresdns.so \
    vendor/samsung/ims/proprietary/system/lib64/libcharon.so:$(TARGET_COPY_OUT_SYSTEM)/lib64/libcharon.so \
    vendor/samsung/ims/proprietary/system/lib64/libcurl2.so:$(TARGET_COPY_OUT_SYSTEM)/lib64/libcurl2.so \
    vendor/samsung/ims/proprietary/system/lib64/libsec-ims.so:$(TARGET_COPY_OUT_SYSTEM)/lib64/libsec-ims.so \
    vendor/samsung/ims/proprietary/system/lib64/libstrongswan.so:$(TARGET_COPY_OUT_SYSTEM)/lib64/libstrongswan.so \
    vendor/samsung/ims/proprietary/system/lib64/vendor.samsung.hardware.radio.bridge@2.0.so:$(TARGET_COPY_OUT_SYSTEM)/lib64/vendor.samsung.hardware.radio.bridge@2.0.so \
    vendor/samsung/ims/proprietary/system/lib64/vendor.samsung.hardware.radio.bridge@2.1.so:$(TARGET_COPY_OUT_SYSTEM)/lib64/vendor.samsung.hardware.radio.bridge@2.1.so

PRODUCT_PACKAGES += \
    imsservice
