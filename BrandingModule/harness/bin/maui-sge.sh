#!/bin/sh
#######################################################
# Wrapper script to run Maui on a cluster host
# Requires working installation of OpenGrid engine or 
# drmaa compatible grid submission systems.
#
# This script requires that X11 binaries are on the 
# executing user's path!
#######################################################
# set maui bin location

if [ -z "$MAUI_HOME" ]; then
	echo "Maui installation not found, aborting. Please set MAUI_HOME to point to the base directory of your Maui installation!"
	xmessage "Maui installation not found, aborting. Please set MAUI_HOME to point to the base directory of your Maui installation!"
	exit 1;
fi

MAUI="$MAUI_HOME/bin/maui"

if [ -z "$MAUI" ]; then
	echo "Maui executable $MAUI not found, aborting."
    xmessage "Maui executable $MAUI not found, aborting."
    exit 1;
fi

if [ -z "$SGE_ARCH" ]; then
        # start netbeans on a cluster host
        #
        #  if $DISPLAY is of the form host:[0-9]* or unix:0.0
        #  take it as is, otherwise (SunRay displays!) we need to
        #  build the correct one...
        #
        NWORDS=`echo $DISPLAY | tr : ' ' | wc -w` 
        if [ "$NWORDS" -ne 2 -o "$DISPLAY" = "unix:0.0" ]; then
                HOSTNAME=`uname -n` 
                DISP=`display | cut -d: -f2`
                DISPLAY=${HOSTNAME}:${DISP}
        fi
        export DISPLAY
        exec qrsh -nostdin -l arch=sol-amd64 -cwd -now y -V $MAUI "$@"
else
        # we already run on a cluster host, execute maui locally
        exec $MAUI
fi