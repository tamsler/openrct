#!/bin/sh

# $Id: kill-rct.sh,v 1.3 2002/06/20 18:55:19 thomas Exp $

clear

echo -n "Kill omniNames: ..."
killall omniNames > /dev/null 2>&1
echo -e "\tOK"

echo -n "Kill notifd: ..."
killall notifd > /dev/null 2>&1
echo -e "\tOK"

echo -n "Kill rctd: ..."
killall rctd > /dev/null 2>&1
echo -e "\t\tOK"

exit 0
