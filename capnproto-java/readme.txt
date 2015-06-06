git clone https://github.com/dwrensha/capnproto-java.git

cp capnproto-java/runtime/src/main/java <Feographia_Android>/capnproto-java/src/main
cp capnproto-java/compiler/src/main/schema/capnp/java.capnp <Feographia_Android>/fcore/prebuild-libs/capnproto-cpp/include/capnp
cp capnpc-java.exe.zip/*.exe <Feographia_Android>/fcore/prebuild-libs/capnproto-cpp/tools
