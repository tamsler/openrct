## Process this file with automake to produce Makefile.in

SUBDIRS = skeletons

OMNI_ROOT = /opt/omni

INCLUDES = \
	-I$(OMNI_ROOT)/include \
	-I$(SUBDIRS) \
	-I$(OMNI_ROOT)/src/services/omniNotify/include \
	-I$(OMNI_ROOT)/include/COS \
	-I/usr/local/pgsql/include \
	-D__x86__ -D__linux__ -D__OSVERSION__=2 -D_REENTRANT -D__OMNIORB4__ \
	@DEBUG_FLAGS@

bin_PROGRAMS = rctd

rctd_LDADD = \
        $(SUBDIRS)/librctcorba.a \
        -L$(OMNI_ROOT)/lib \
        -L$(OMNI_ROOT)/src/services/omniNotify/lib \
        -L/usr/local/pgsql/lib \
        -lCOS4 -lCOSDynamic4 -lomniORB4 -lomniDynamic4 -lomnithread -lpthread \
        -lpq

rctd_SOURCES = \
	AdminServerImpl.cc AdminServerImpl.h \
	AuthenticationServerImpl.cc AuthenticationServerImpl.h \
	ChatServerImpl.cc ChatServerImpl.h \
	ClassServerImpl.cc ClassServerImpl.h \
	ControlServerImpl.cc ControlServerImpl.h \
	CourseContentServerImpl.cc CourseContentServerImpl.h CourseContentServerConst.h \
	DatabaseServerImpl.cc DatabaseServerImpl.h \
	FileServerImpl.cc FileServerImpl.h FileServerConst.h \
	FtpServerImpl.cc FtpServerImpl.h FtpServerConst.h \
	GroupServerImpl.cc GroupServerImpl.h GroupServerConst.h \
	NameServerUtil.cc NameServerUtil.h \
	NotifyUtil.cc NotifyUtil.h \
	PingServerImpl.cc PingServerImpl.h PingServerConst.h \
	PushSupplierImpl.cc PushSupplierImpl.h \
	RctMutex.h RctMutex.cc \
	ServerImpl.cc ServerImpl.h \
	SessionServerImpl.cc SessionServerImpl.h SessionServerConst.h \
	SoundServerImpl.cc SoundServerImpl.h SoundServerConst.h \
	TDServerImpl.cc TDServerImpl.h TDServerConst.h \
	TeamServerImpl.cc TeamServerImpl.h TeamServerConst.h \
	TextpadServerImpl.cc TextpadServerImpl.h TextpadServerConst.h \
	UrlServerImpl.cc UrlServerImpl.h \
	UserServerImpl.cc UserServerImpl.h \
	Util.cc Util.h \
	main.cc

