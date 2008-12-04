#!/bin/sh

/usr/local/pgsql/bin/psql rctdb < rct-drop-all.sql

/usr/local/pgsql/bin/psql rctdb < rct-test.sql

