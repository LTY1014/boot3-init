#!/bin/bash
# 说明：第一次手动拉代码,脚本放置在pom文件同级

# 设置变量
jarName=boot3-init.jar
logFile=1.out

# 拉取代码 构建项目
git pull
mvn package
         
# 停止旧进程
PID=`ps -ef|grep $jarName |grep -v grep|awk '{print $2}' `
if [ -n "$PID" ]; then
    kill -9 $PID
    echo "结束$PID进程"
fi

# 启动新进程
cd target
nohup java -jar $jarName > $logFile 2>&1 &

tail -f ./$logFile
