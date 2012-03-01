#!/bin/sh

# wrapper script to invoke netbeans in a cluster host

MAUI=/vol/maltcms/maui/1.1/bin/maui

if [ -z "$MAUI" ]; then
    /vol/x11/bin/xmessage "Maui executable $MAUI not found, aborting."
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
                DISP=`/vol/X11/bin/display | cut -d: -f2`
                DISPLAY=${HOSTNAME}:${DISP}
        fi
        export DISPLAY
        exec qrsh -nostdin -l arch=sol-amd64 -cwd -now y -V $MAUI "$@"
else
        # we already run on a cluster host, execute maui locally
        exec $MAUI
fi