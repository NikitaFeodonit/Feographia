
Building jzmq library with CrystaX NDK in Msys2 on Windows for Android
(this is similar on the other systems)


1. Installing Android NDK Standalone Toolchain and variables, see
<Feographia_Android>/fcore/prebuild-libs/readme.txt


2. Build ZeroMQ library, see
<Feographia_Android>/fcore/prebuild-libs/zeromq/readme.txt


3. Get sources of jzmq library
(All variables are set by 1st step)

cd /local32
git clone https://github.com/zeromq/jzmq.git
cd jzmq


4. patch sources

cp <Feographia_Android>/fcore/prebuild-libs/jzmq/patch-all ./
patch -p1 <patch-all


5. Building

# set variable where installed zeromq
export OUTPUT_DIR=/local32/zeromq4-1_inst

./autogen.sh

./configure --host=arm-linux-androideabi --enable-static --disable-shared --prefix=/local32/jzmq_inst --with-zeromq=$OUTPUT_DIR CPPFLAGS="-fPIC -I$OUTPUT_DIR/include" LDFLAGS="-L$OUTPUT_DIR/lib" --disable-version

make && make install


6. Copying to project

cp /local32/jzmq_inst/lib <Feographia_Android>/fcore/prebuild-libs/zeromq/lib

# must be like so:
# <Feographia_Android>/fcore/prebuild-libs/zeromq/lib
#     /pkgconfig
#     libjzmq.a
#     libjzmq.la
#     libzmq.a
#     libzmq.la



cp /local32/jzmq/src/main/java <Feographia_Android>/jzmq/src/main

# must be like so:
# <Feographia_Android>/jzmq/src/main
#     /java
#     res
#     AndroidManifest.xml
