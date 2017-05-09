
安装 alsa

设置环境变量:
export LANG=C
export ALT_BOOTDIR=/usr/java/jdk1.8.0_60
export ALLOW_DOWNLOADS=true

export HOTSPOT_BUILD_JOBS=2
export ALT_PARALLEL_COMPILE_JOBS=2

export SKIP_COMPARE_IMAGES=true

export USE_PRECOMPILED_HEADER=true

export BUILD_LANGTOOLS=true

export BUILD_HOTSPOT=true

export BUILD_JDK=true

BUILD_DEPLOY=false

BUILD_INSTALL=false

export ALT_OUTPUTDIR=/home/dev-env/tools/result

unset JAVA_HOME
unset CLASSPATH





CLASSPATH=.:/usr/java/jdk1.8.0_60/lib/dt.jar:/usr/java/jdk1.8.0_60/lib/tools.jar:/usr/java/jdk1.8.0_60/jre/lib
JAVA_HOME=/usr/java/jdk1.8.0_60