FROM docker:stable-dind
ARG CUSTOM_JVM_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XshowSettings:vm"
ENV DEFAULT_USER_HOME=/home/jenkins \
  DEFAULT_USER=jenkins \
  DEFAULT_USER_UID=1000 \
  LANG=C.UTF-8 \
  JAVA_HOME=/usr/lib/jvm/java-1.8-openjdk \
  PATH=${PATH}:/usr/lib/jvm/java-1.8-openjdk/jre/bin:/usr/lib/jvm/java-1.8-openjdk/bin \
  DOCKER_IMAGE_CACHE_DIR=/docker-cache \
  START_DOCKER="true" \
  DOCKER_STORAGE_DRIVER="overlay2" \
  CUSTOM_JVM_OPTS=${CUSTOM_JVM_OPTS}
# Install packages
RUN apk add --no-cache \
    bash \
    curl \
    git \
    maven \
    openjdk8 \
    openssh \
    perl \
    py-pip \
    ttf-dejavu \
    unzip \
    tar \
    tini \
  # Create default user and groups
  # Default Group and Default User are the same, with same ID
  && addgroup -g "${DEFAULT_USER_UID}" "${DEFAULT_USER}" \
    && adduser -h "${DEFAULT_USER_HOME}" -u "${DEFAULT_USER_UID}" \
      -G "${DEFAULT_USER}" -s /bin/bash -D "${DEFAULT_USER}" \
  && echo "${DEFAULT_USER}:${DEFAULT_USER}" | chpasswd \
  # Add default user to docker groups + tune PATH
  && addgroup docker \
  && addgroup "${DEFAULT_USER}" docker \
  && addgroup "${DEFAULT_USER}" dockremap \
  && ln -s /usr/local/bin/docker* /usr/bin/ \
  # Install latest Docker-compose
  && pip install --no-cache-dir "docker-compose" \
  # Tune SSH Server for Jenkins Usage + sending logs on stdout
  && sed -i 's/#PermitRootLogin.*/PermitRootLogin no/' /etc/ssh/sshd_config \
  && sed -i 's/#RSAAuthentication.*/RSAAuthentication yes/' /etc/ssh/sshd_config \
  && sed -i 's/#PasswordAuthentication.*/PasswordAuthentication yes/' /etc/ssh/sshd_config \
  && sed -i 's/#SyslogFacility.*/SyslogFacility AUTH/' /etc/ssh/sshd_config \
  && sed -i 's/#LogLevel.*/LogLevel INFO/' /etc/ssh/sshd_config \
  && mkdir /var/run/sshd \
  && rm -f /var/log/auth.log \
  && ln -s /dev/stdout /var/log/auth.log

COPY daemon.json /etc/docker/daemon.json
COPY dind-entrypoint.sh /usr/local/bin/dind-entrypoint.sh
COPY jenkins-agent-entrypoint.sh /usr/local/bin/entrypoint.sh

VOLUME ${DEFAULT_USER_HOME} /docker-cache /var/run /run /etc/docker

HEALTHCHECK --start-period=3s --interval=10s --retries=3 --timeout=2s \
  CMD curl -f http://localhost:2375/_ping || exit 1

EXPOSE 22

ENTRYPOINT ["/sbin/tini","-g","--","bash"]
CMD ["/usr/local/bin/entrypoint.sh"]
