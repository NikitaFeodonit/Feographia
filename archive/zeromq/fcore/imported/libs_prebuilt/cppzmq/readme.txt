
Preparing cppzmq library with CrystaX NDK in Msys2 on Windows for Android
(this is similar on the other systems)


1. Installing Android NDK Standalone Toolchain and variables, see
<Feographia_Android>/fcore/prebuild-libs/readme.txt


2. Build ZeroMQ library, see
<Feographia_Android>/fcore/prebuild-libs/zeromq/readme.txt


3. Get cppzmq sources

git clone https://github.com/zeromq/cppzmq.git


4. Copying to project

cp cppzmq/zmq.hpp <Feographia_Android>/fcore/prebuild-libs/zeromq/include
