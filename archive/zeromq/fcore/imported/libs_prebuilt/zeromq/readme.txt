
Building ZeroMQ library with CrystaX NDK in Msys2 on Windows for Android
(this is similar on the other systems)


1. Installing Android NDK Standalone Toolchain and variables, see
<Feographia_Android>/fcore/prebuild-libs/readme.txt


2. Build ZeroMQ library

mkdir /local32
mkdir /local32/crystax-ndk-10.1.0
mount e:/Dev/Tools/Android/crystax-ndk-10.1.0 /local32/crystax-ndk-10.1.0
export ANDROID_NDK_ROOT=/local32/crystax-ndk-10.1.0

# if use window 7/8 64bit add additional options to make-standalone-toolchain.sh:
# --system=windowx86_64
$ANDROID_NDK_ROOT/build/tools/make-standalone-toolchain.sh --platform=android-9 --toolchain=arm-linux-androideabi-4.9 --stl=gnustl --system=windows-x86_64 --install-dir=$ANDROID_NDK_ROOT/standalone_toolchain/android-9/arm-linux-androideabi-4.9

export NDK_STANDALONE_TOOLCHAIN_ROOT=$ANDROID_NDK_ROOT/standalone_toolchain/android-9/arm-linux-androideabi-4.9
export PATH=$PATH:$NDK_STANDALONE_TOOLCHAIN_ROOT/bin:$NDK_STANDALONE_TOOLCHAIN_ROOT/arm-linux-androideabi/bin

cd /local32
git clone https://github.com/zeromq/zeromq4-1.git
cd zeromq4-1

export OUTPUT_DIR=/local32/zeromq4-1_inst

# see issues below for trubles with autogen.sh
./autogen.sh

# additional options:
# --without-documentation
./configure --host=arm-linux-androideabi --enable-static --disable-shared --prefix=$OUTPUT_DIR LDFLAGS="-L$OUTPUT_DIR/lib" CPPFLAGS="-fPIC -I$OUTPUT_DIR/include" LIBS="-lgcc" --with-libsodium=no

make -j8 && make install


3. Copying to project

cp $OUTPUT_DIR/include <Feographia_Android>/fcore/prebuild-libs/zeromq
cp $OUTPUT_DIR/lib <Feographia_Android>/fcore/prebuild-libs/zeromq

# must be like so:
# <Feographia_Android>/fcore/prebuild-libs/zeromq
#     /include
#     /lib
#     Android.mk
#     zeromq.pri
#     readme.txt



4. Issue for autogen.sh

$ ./autogen.sh -W all

==output:
autoreconf: Entering directory `.'
autoreconf: configure.ac: not using Gettext
autoreconf: running: aclocal -I config --force -I config
' is already registered with AC_CONFIG_FILES.
../../lib/autoconf/status.m4:288: AC_CONFIG_FILES is expanded from...
configure.ac:534: the top level
autom4te: /usr/bin/m4 failed with exit status: 1
aclocal-1.15: error: echo failed with exit status: 1
autoreconf: aclocal failed with exit status: 1
autogen.sh: error: autoreconf exited with status 0

==solution:
make conversion CRLF -> LF
