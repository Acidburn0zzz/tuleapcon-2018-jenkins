import jenkins.model.*
import hudson.model.*
import hudson.slaves.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*
import hudson.plugins.sshslaves.*;
import hudson.plugins.sshslaves.verifiers.*;
import hudson.slaves.EnvironmentVariablesNodeProperty.Entry

//// Set Credentials

global_domain = Domain.global()

def env = System.getenv()
String customJvmOpts = env['CUSTOM_JVM_OPTS']
String jdk8Home = '/usr/lib/jvm/java-1.8-openjdk'

credentials_store = Jenkins.instance.getExtensionList(
  'com.cloudbees.plugins.credentials.SystemCredentialsProvider'
)[0].getStore()

credentials = new BasicSSHUserPrivateKey(
  CredentialsScope.SYSTEM,
  "ssh-nodes-key",
  "jenkins",
  new BasicSSHUserPrivateKey.FileOnMasterPrivateKeySource(
    '/usr/share/jenkins/ref/insecure_vagrant_key'
  ),
  '',
  "SSH Key for the Agent"
)

credentials_store.addCredentials(global_domain, credentials)


/// Disable all protocol except JNLP4
//// From https://github.com/samrocketman/jenkins-bootstrap-shared/blob/master/scripts/configure-jnlp-agent-protocols.groovy
Jenkins myJenkins = Jenkins.instance

if(!myJenkins.isQuietingDown()) {
    Set<String> agentProtocolsList = ['JNLP4-connect', 'Ping']
    if(!myJenkins.getAgentProtocols().equals(agentProtocolsList)) {
        myJenkins.setAgentProtocols(agentProtocolsList)
        println "Agent Protocols have changed.  Setting: ${agentProtocolsList}"
        myJenkins.save()
    }
    else {
        println "Nothing changed.  Agent Protocols already configured: ${myJenkins.getAgentProtocols()}"
    }
}
else {
    println 'Shutdown mode enabled.  Configure Agent Protocols SKIPPED.'
}

/// Configure and start Agents on Nodes

Slave jenkinsNode = new DumbSlave(
  "jenkins-node",
  "Simple Node for running builds with docker available",
  "/home/jenkins",
  "4",
  Node.Mode.NORMAL,
  "docker java maven jdk8",
  new SSHLauncher(
    "jenkins-node", // HostName
    22,
    'ssh-nodes-key', // Credential ID
    customJvmOpts, // JVM Options
    "", // JavaPath
    "", // Prefix Start CMD
    "", // Suffix Start CMD
    15, // Launch Timeout
    3, // maxRetries
    5, // RetryWait
    new NonVerifyingKeyVerificationStrategy()
  ),
  new RetentionStrategy.Always(),
  new LinkedList()
)

List<Entry> jenkinsNodeEnv = new ArrayList<Entry>();
jenkinsNodeEnv.add(new Entry("JAVA_HOME","${jdk8Home}"))
jenkinsNodeEnv.add(new Entry("DOCKER_HOST","unix:///var/run/docker.sock"))
EnvironmentVariablesNodeProperty jenkinsNodeEnvPro = new EnvironmentVariablesNodeProperty(jenkinsNodeEnv);
jenkinsNode.getNodeProperties().add(jenkinsNodeEnvPro)

Jenkins.instance.addNode(jenkinsNode)
println("Added successfully 'jenkins-node' to Jenkins")
