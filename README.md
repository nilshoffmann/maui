# Getting Started

## Requirements

-[JAVA SE Development Kit 7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)
-[Apache ANT](http://ant.apache.org)
-[Git](http://git-scm.com/)

An IDE, preferrably [NetBeans IDE 8](http://www.netbeans.org).

## Checking out (Read-Only)

    git clone git://git.code.sf.net/p/maltcmsui/code maltcmsui-code

## Checking out (Read/Write)

    git clone ssh://YOUR_SF_USERNAME@git.code.sf.net/p/maltcmsui/code maltcmsui-code
or
    git clone https://YOUR_SF_USERNAME@git.code.sf.net/p/maltcmsui/code maltcmsui-code

## Initialize the Proteus submodule
    
    git submodule init
    git submodule update

# Building
Maui requires Apache Ant to compile and package the application.

## Building from the base directory

    ant build-all

## Building Maui
Within the 'maui/' directory, call

    ant build

to compile Maui and the dependent project omics-base. If you want 
to build the complete distribution with installers and documentation, call

    ant prepare-release

## Building Maui-Ext
Within the 'maui-ext/' directory, call

    ant build

to compile Maui-Ext and the dependent projects omics-base and maui. If you want 
to build the complete distribution with installers and documentation, call

    ant prepare-release

## Building Proteus
Within the 'proteus/' directory, call

    ant build

to compile Proteus and the dependent project omics-base. If you want to build 
the complete distribution with installers and documentation, call

    ant prepare-release
