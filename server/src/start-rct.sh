#!/bin/sh

# $Id: start-rct.sh,v 1.8 2003/05/15 22:00:51 thomas Exp $

clear

echo -n "Starting omniNames: ..."
omniNames > /dev/null 2>&1 &
echo -e "\tOK"

echo -n "Starting notifd: ..."
notifd > /dev/null 2>&1 &
echo -e "\tOK"

echo -n "Starting rctd: ..."
rctd $1 $2&
echo -e "\tOK"

exit 0
