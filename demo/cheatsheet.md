
# Cheat Sheet

## Tuleap initial setup

* Start Stack
* Get admin password

```
docker-compose exec tuleap cat /data/root/.tuleap_passwd | grep Site | awk '{print $5}'| pbcopy
```

* Install Jenkins & Jenkins/Git plugins + enable
* Create new project named jenkins
* Validate pending project
* Go to project -> services and add **Git**
* Create Git repositories `basic-pipeline`, `jenkins-libraries` and `tracker-pipeline`
* Go to project -> services and add **Continous Integration**


## Basic Pipeline

* Open Jenkins, log in, go to BlueOcean WOUHAOUH
* New Pipeline Wizard
* URL: `ssh://gitolite@tuleap/jenkins-demo/application-backend.git`
* SSH Key stuff
* Create Pipeline with "Build"
* Show the result
* Iterate with the "Test"
* Show the result
* Spend time on BlueOcean elements
* Show the Legacy UI


## WebHooks

* Create a webhook on the Git repository with the URL http://jenkins:8080/jenkins/git/notifyCommit?url=ssh://gitolite@tuleap/jenkins/basic-pipeline.git
* `export GIT_SSL_NO_VERIFY=true` and clone in HTTP
* Push a change: magica

## Tracker

* Master branch Pipeline:te
* Create new tracker `apptrack`
* Go to administration -> workflow -> from (New) TO NEW -> New action "CI"
* Add the master's branch job URL
* Pipelien snippet:

## TODO

* Dépôt basic-pipeline: remplir avec du code
* Prévoir une version locale avec branch "serious-pipeline" qui contient
approval, parallel, etc.
