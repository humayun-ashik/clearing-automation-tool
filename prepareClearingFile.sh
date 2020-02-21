#!/bin/bash

set -x

## ------------------- DECLARING NECESSARY VARIABLES -----------------##
toDate=`date '+%Y%m%d'`
fromDate=`date '+%Y%m%d'`
timeStr=`date -d "1 hour ago" '+%Y-%m-%d-%H'`
ias_logFile="/home/ias/clr/*.log"
ussd_logFile="/home/ussdgw/clr/*.log"
ias_arc_logFile="/home/ias/clr/archive/CLR-ias-app1.kpp.com-INST_1-${timeStr}*.log"
ussd_arc_logFile="/home/ias/clr/archive/CLR-ussdgw-app1.kpp.com-INST_1-$timeStr*.log"
ias_clr_destPath="/home/clr/DFS-CLR/test_clr/ias_clr/"
ussd_clr_destPath="/home/clr/DFS-CLR/test_clr/ussd_gw/"
clr_scriptFile="/home/clr/DFS-CLR/script/run_clearing.sh"

## ------------------- REMOVING OLD LOG FILE -------------------------##
printf "Removing old clearing log...\n"
rm -f ${ias_clr_destPath}*.log
rm -f ${ussd_clr_destPath}*.log

## ------------------- PREPARE IAS LOG FILE --------------------------##
printf "Preparing ias log file for clearing...\n"
cp ${ias_logFile} ${ias_clr_destPath}

if [ -f ${ias_arc_logFile} ]
then
    cp ${ias_arc_logFile} ${ias_logFile}
fi

printf "IAS LOG FILE COPY DONE\n"

## ------------------- PREPARE USSD LOG FILE ------------------------##
printf "Preparing ussd log file for clearing...\n"
cp ${ussd_logFile} ${ussd_clr_destPath}

if [ -f ${ussd_arc_logFile} ]
then
    cp ${ussd_arc_logFile} ${ussd_clr_destPath}
fi

printf "USSD LOG FILE COPY DONE\n"

## ------------------ RUNNING CLEARING JOB --------------------------##
printf "Start running clearing job...\n"
printf "From-date : ${fromDate}\n"
printf "To-date : ${toDate}\n"

sudo -u clr ${clr_scriptFile} ${fromDate} ${toDate}

printf "Clearing job running done!\n"
