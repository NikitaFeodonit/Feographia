#
# Project:  Feographia
# Purpose:  Mobile application to work with the biblical text
# Authors:  NikitaFeodonit, nfeodonit@yandex.com
# ****************************************************************************
# Copyright (C) 2015 NikitaFeodonit
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)


LOCAL_MODULE    := zmqpp
#LOCAL_MODULE_FILENAME := libfcore


LOCAL_SRC_FILES := src/zmqpp/actor.cpp
LOCAL_SRC_FILES += src/zmqpp/auth.cpp
LOCAL_SRC_FILES += src/zmqpp/context.cpp
LOCAL_SRC_FILES += src/zmqpp/curve.cpp
LOCAL_SRC_FILES += src/zmqpp/frame.cpp
LOCAL_SRC_FILES += src/zmqpp/message.cpp
LOCAL_SRC_FILES += src/zmqpp/poller.cpp
LOCAL_SRC_FILES += src/zmqpp/reactor.cpp
LOCAL_SRC_FILES += src/zmqpp/signal.cpp
LOCAL_SRC_FILES += src/zmqpp/socket.cpp
LOCAL_SRC_FILES += src/zmqpp/z85.cpp
LOCAL_SRC_FILES += src/zmqpp/zap_request.cpp
LOCAL_SRC_FILES += src/zmqpp/zmqpp.cpp


LOCAL_C_INCLUDES := $(LOCAL_PATH)/src
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/src


TARGET_PLATFORM := android-9

LOCAL_CPPFLAGS += -fPIC

LOCAL_STATIC_LIBRARIES := zmq-prebuilt


include $(BUILD_STATIC_LIBRARY)

$(call import-add-path,$(LOCAL_PATH)/../..)
$(call import-module,prebuild-libs/zeromq)
