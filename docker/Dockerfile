FROM openjdk:8-jre
ENV JAVA_HOME /usr/lib/jvm/java-1.8-openjdk
ENV PATH $PATH:/usr/lib/jvm/java-1.8-openjdk/jre/bin:/usr/lib/jvm/java-1.8-openjdk/bin

ENV SBT_VERSION 1.3.3

RUN echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list
RUN curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add
RUN apt-get update && apt-get -y install sbt
WORKDIR /src/main/java
COPY svc /svc
EXPOSE 9000
CMD /svc/bin/start -Dplay.evolutions.db.default.autoApply=true -Dplay.crypro.secret=QCY?tAnfk?aZ?iwrNwnxIlR6CTf:G3gf:90Latabg@5241ABIR5W:1uDFN];Ik@n
