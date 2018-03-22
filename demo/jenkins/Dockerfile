FROM jenkins/jenkins:2.107.1-alpine

# Used to provide Custom JVM to all elements (master and agents)
ARG CUSTOM_JVM_OPTS='-XshowSettings:vm -Djenkins.install.runSetupWizard=false'
ENV CUSTOM_JVM_OPTS=${CUSTOM_JVM_OPTS}

# Install Plugins Before starting Jenkins
COPY plugins.txt /tmp/plugins.txt
RUN /usr/local/bin/install-plugins.sh $(cat /tmp/plugins.txt)

COPY ./ref /usr/share/jenkins/ref

# We need SSH Keys so let's use the Vagrant insecure one
RUN curl -L -o /usr/share/jenkins/ref/insecure_vagrant_key \
  https://raw.githubusercontent.com/mitchellh/vagrant/master/keys/vagrant

ENV JAVA_OPTS="${CUSTOM_JVM_OPTS}"

# Provides knowledge of our service status for Docker
# The webserver inside Jetty is enough for now
HEALTHCHECK --start-period=5s --interval=5s --retries=3 --timeout=2s \
  CMD curl -f http://localhost:8080 || exit 1
