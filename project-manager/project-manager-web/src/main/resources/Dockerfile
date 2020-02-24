#Version: 0.0.1
FROM 192.168.250.18/hgt/centos7-jdk8:v1 

MAINTAINER xavier
ADD project-manager-web-1.0-SNAPSHOT.jar /root/project-manager.jar
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
RUN  mkdir /home/logs
RUN  mkdir /home/logs/project-manager
RUN chmod 777 -R /home/logs/project-manager
ENV LANG en_US.UTF-8    
ENV LANGUAGE en_US:en    
ENV LC_ALL en_US.UTF-8
ENV LANG C.UTF-8

EXPOSE 13600
ENTRYPOINT ["java", "-jar", "-Xms500m", "-Xmx500m", "/root/project-manager.jar"]
