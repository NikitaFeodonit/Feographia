
Building czmq library with CrystaX NDK in Msys2 on Windows for Android
(this is similar on the other systems)


1. Installing Android NDK Standalone Toolchain and variables, see
<Feographia_Android>/fcore/prebuild-libs/readme.txt


2. Build ZeroMQ library, see
<Feographia_Android>/fcore/prebuild-libs/zeromq/readme.txt


3. Get sources of czmq library
(All variables are set by 1st step)

cd /local32
git clone https://github.com/zeromq/czmq.git
cd czmq


4. patch sources

cp <Feographia_Android>/fcore/prebuild-libs/czmq/patch-all ./
patch -p1 <patch-all


5. Building

# set variable where installed zeromq
export OUTPUT_DIR=/local32/zeromq4-1_inst

./autogen.sh

PKG_CONFIG_PATH=$OUTPUT_DIR/lib/pkgconfig ./configure --host=arm-linux-androideabi --enable-static --disable-shared --prefix=$OUTPUT_DIR CPPFLAGS="-fPIC -I$OUTPUT_DIR/include" LDFLAGS="-L$OUTPUT_DIR/lib"

make -j8 && make install


6. Copying to project
see "Copying to project" in czmqpp/readme.txt
