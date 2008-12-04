## Process this file with automake to produce Makefile.in

IDLCXX=omniidl -bcxx -Wba -I$(OMNI_ROOT)/idl
IDLDIR = ../../idl
OMNI_ROOT = /opt/omni

INCLUDES = \
	-c -I$(OMNI_ROOT)/include \
	-D__x86__ -D__linux__ -D__OSVERSION__=2 -D__OMNIORB4__

noinst_LIBRARIES = librctcorba.a

librctcorba_a_SOURCES = \
	AuthenticationServer.hh AuthenticationServerSK.cc AuthenticationServerDynSK.cc \
	Server.hh ServerSK.cc ServerDynSK.cc \
	ClassServer.hh ClassServerSK.cc ClassServerDynSK.cc \
	CourseContentServer.hh CourseContentServerSK.cc CourseContentServerDynSK.cc \
	GroupServer.hh GroupServerSK.cc GroupServerDynSK.cc \
	DatabaseServer.hh DatabaseServerSK.cc DatabaseServerDynSK.cc \
	SessionServer.hh SessionServerSK.cc SessionServerDynSK.cc \
	TeamServer.hh TeamServerSK.cc TeamServerDynSK.cc \
	UserServer.hh UserServerSK.cc UserServerDynSK.cc \
	Message.hh MessageSK.cc MessageDynSK.cc \
	BCMessage.hh BCMessageSK.cc BCMessageDynSK.cc \
	PermissionObj.hh PermissionObjSK.cc PermissionObjDynSK.cc \
	PermissionPerson.hh PermissionPersonSK.cc PermissionPersonDynSK.cc \
	ChatServer.hh ChatServerSK.cc ChatServerDynSK.cc \
	ChatMessage.hh ChatMessageSK.cc ChatMessageDynSK.cc \
	FileServer.hh FileServerSK.cc FileServerDynSK.cc \
	PingServer.hh PingServerSK.cc PingServerDynSK.cc \
	ControlServer.hh ControlServerSK.cc ControlServerDynSK.cc \
	ControlMessage.hh ControlMessageSK.cc ControlMessageDynSK.cc \
	SoundServer.hh SoundServerSK.cc SoundServerDynSK.cc \
	SoundMessage.hh SoundMessageSK.cc SoundMessageDynSK.cc \
	TextpadServer.hh TextpadServerSK.cc TextpadServerDynSK.cc \
	TextpadMessage.hh TextpadMessageSK.cc TextpadMessageDynSK.cc \
	UrlServer.hh UrlServerSK.cc UrlServerDynSK.cc \
	UrlMessage.hh UrlMessageSK.cc UrlMessageDynSK.cc \
	FtpServer.hh FtpServerSK.cc FtpServerDynSK.cc \
	FtpMessage.hh FtpMessageSK.cc FtpMessageDynSK.cc \
	AdminServer.hh AdminServerSK.cc AdminServerDynSK.cc \
	TDServer.hh TDServerSK.cc TDServerDynSK.cc \
	TDMessage.hh TDMessageSK.cc TDMessageDynSK.cc

$(librctcorba_a_SOURCES):
	$(IDLCXX) $(IDLDIR)/AuthenticationServer.idl
	$(IDLCXX) $(IDLDIR)/Server.idl
	$(IDLCXX) $(IDLDIR)/ClassServer.idl
	$(IDLCXX) $(IDLDIR)/CourseContentServer.idl
	$(IDLCXX) $(IDLDIR)/DatabaseServer.idl
	$(IDLCXX) $(IDLDIR)/SessionServer.idl
	$(IDLCXX) $(IDLDIR)/UserServer.idl
	$(IDLCXX) $(IDLDIR)/GroupServer.idl
	$(IDLCXX) $(IDLDIR)/TeamServer.idl
	$(IDLCXX) $(IDLDIR)/Message.idl
	$(IDLCXX) $(IDLDIR)/BCMessage.idl
	$(IDLCXX) $(IDLDIR)/PermissionObj.idl
	$(IDLCXX) $(IDLDIR)/PermissionPerson.idl
	$(IDLCXX) $(IDLDIR)/ChatServer.idl
	$(IDLCXX) $(IDLDIR)/ChatMessage.idl
	$(IDLCXX) $(IDLDIR)/FileServer.idl
	$(IDLCXX) $(IDLDIR)/PingServer.idl
	$(IDLCXX) $(IDLDIR)/ControlServer.idl
	$(IDLCXX) $(IDLDIR)/ControlMessage.idl
	$(IDLCXX) $(IDLDIR)/SoundServer.idl
	$(IDLCXX) $(IDLDIR)/SoundMessage.idl
	$(IDLCXX) $(IDLDIR)/TextpadServer.idl
	$(IDLCXX) $(IDLDIR)/TextpadMessage.idl
	$(IDLCXX) $(IDLDIR)/UrlServer.idl
	$(IDLCXX) $(IDLDIR)/UrlMessage.idl
	$(IDLCXX) $(IDLDIR)/FtpServer.idl
	$(IDLCXX) $(IDLDIR)/FtpMessage.idl
	$(IDLCXX) $(IDLDIR)/AdminServer.idl
	$(IDLCXX) $(IDLDIR)/TDServer.idl
	$(IDLCXX) $(IDLDIR)/TDMessage.idl

dist-hook:
	rm -f $(librctcorba_a_SOURCES)
