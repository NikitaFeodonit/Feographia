#
# Project:  Feographia
# Purpose:  Mobile application to work with the biblical text
# Author:   NikitaFeodonit, nfeodonit@yandex.com
# ****************************************************************************
# Copyright (C) 2015-2016 NikitaFeodonit
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

TEMPLATE = app
CONFIG += console
CONFIG -= app_bundle
CONFIG -= qt

INCLUDEPATH += \
    $$PWD/src \

HEADERS += \
    $$PWD/src/zmqpp/actor.hpp \
    $$PWD/src/zmqpp/auth.hpp \
    $$PWD/src/zmqpp/compatibility.hpp \
    $$PWD/src/zmqpp/context.hpp \
    $$PWD/src/zmqpp/context_options.hpp \
    $$PWD/src/zmqpp/curve.hpp \
    $$PWD/src/zmqpp/exception.hpp \
    $$PWD/src/zmqpp/frame.hpp \
    $$PWD/src/zmqpp/inet.hpp \
    $$PWD/src/zmqpp/message.hpp \
    $$PWD/src/zmqpp/poller.hpp \
    $$PWD/src/zmqpp/reactor.hpp \
    $$PWD/src/zmqpp/signal.hpp \
    $$PWD/src/zmqpp/socket.hpp \
    $$PWD/src/zmqpp/socket_mechanisms.hpp \
    $$PWD/src/zmqpp/socket_options.hpp \
    $$PWD/src/zmqpp/socket_types.hpp \
    $$PWD/src/zmqpp/z85.hpp \
    $$PWD/src/zmqpp/zap_request.hpp \
    $$PWD/src/zmqpp/zmqpp.hpp \

SOURCES += \
    $$PWD/src/zmqpp/actor.cpp \
    $$PWD/src/zmqpp/auth.cpp \
    $$PWD/src/zmqpp/context.cpp \
    $$PWD/src/zmqpp/curve.cpp \
    $$PWD/src/zmqpp/frame.cpp \
    $$PWD/src/zmqpp/message.cpp \
    $$PWD/src/zmqpp/poller.cpp \
    $$PWD/src/zmqpp/reactor.cpp \
    $$PWD/src/zmqpp/signal.cpp \
    $$PWD/src/zmqpp/socket.cpp \
    $$PWD/src/zmqpp/z85.cpp \
    $$PWD/src/zmqpp/zap_request.cpp \
    $$PWD/src/zmqpp/zmqpp.cpp \

OTHER_FILES += \
    $$PWD/Android.mk
