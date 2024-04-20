SRC_FILES=`find ./java/ | grep '\.java'`

$JAVA_HOME/bin/javac -cp "libs/*" $SRC_FILES --source 8 --target 8 -g -d out
7z a -tzip out.jar -w out/.
$SDK_HOME/build-tools/30.0.3/d8 --min-api 24 out.jar
zip -0 dexjar.jar classes.dex
$JAVA_HOME/bin/java -jar $APKTOOL_JAR_PATH d dexjar.jar
cp -r ./dexjar.jar.out/smali ./smali_out

rm -r ./dexjar.jar.out
rm ./out.jar
rm ./classes.dex
rm ./dexjar.jar
