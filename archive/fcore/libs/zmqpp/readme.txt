
Preparing zmqpp library with CrystaX NDK in Msys2 on Windows for Android
(this is similar on the other systems)


1. Installing Android NDK Standalone Toolchain and variables, see
<Feographia_Android>/fcore/prebuild-libs/readme.txt


2. Build ZeroMQ library, see
<Feographia_Android>/fcore/prebuild-libs/zeromq/readme.txt


3. Get sources of zmqpp library
(All variables are set by 1st step)

cd /local32
git clone https://github.com/zeromq/zmqpp.git


6. Copying to project

cp zmqpp/src/zmqpp <Feographia_Android>/fcore/libs/zmqpp/src
