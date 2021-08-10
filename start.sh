#!/bin/sh
xterm -hold -e "cd ~/AcademicPerformance/frontend && yarn start" &
xterm -hold -e "cd ~/AcademicPerformance/backend && mvn spring-boot:run" &
