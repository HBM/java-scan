# $${\color{red}MOVED}$$
# https://github.com/hbkworld/java-scan

# A Java implementation of the HBM device discovery protocol.

[![Build Status](https://travis-ci.org/HBM/java-scan.svg?branch=master)](https://travis-ci.org/HBM/java-scan)
[![Coverity](https://scan.coverity.com/projects/5097/badge.svg)](https://scan.coverity.com/projects/5097)
[![Coverage Status](https://coveralls.io/repos/github/HBM/java-scan/badge.svg?branch=master)](https://coveralls.io/github/HBM/java-scan?branch=master)
[![codebeat badge](https://codebeat.co/badges/ed7f8cda-76cb-401a-bdd9-66189420753f)](https://codebeat.co/projects/github-com-gatzka-java-scan-master)
[![Download](https://api.bintray.com/packages/hbm/java/devscan/images/download.svg) ](https://bintray.com/hbm/java/devscan/_latestVersion)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.hbm/devscan/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.hbm/devscan)

[![Stories in Backlog](https://badge.waffle.io/HBM/java-scan.png?label=backlog&title=Backlog)](https://waffle.io/HBM/java-scan)
[![Stories in Ready](https://badge.waffle.io/HBM/java-scan.png?label=ready&title=Ready)](https://waffle.io/HBM/java-scan)
[![Stories in progress](https://badge.waffle.io/HBM/java-scan.png?label=in%20progress&title=In%20Progress)](https://waffle.io/HBM/java-scan)

[![Open Hub](https://img.shields.io/badge/Open-Hub-0185CA.svg)](https://www.openhub.net/p/java-scan)

## About
This library implements the HBM device discovery protocol in  Java. The
library is 100 % pure Java and fully compatible with Android.

Both scan and configuration are implemented.

## License

Copyright (c) 2014 Hottinger Baldwin Messtechnik GmbH. See the
[LICENSE](LICENSE) file for license rights and limitations (MIT).

## Build

The preferred method to build the library is gradle. Just run the gradle wrapper
```bash
./gradlew build
```
and you will find the generated devscan.jar file in the folder
/tmp/devscan/devscan/libs.

The gradlew script has now the ability to deploy jar files and 
pom files to [bintray](https://bintray.com/) and
[Maven Central](http://search.maven.org/). Run
```bash
./gradlew uploadArchives -Prelease -P<repository> -Pgpg_id=<gpg-id> -Pgpg_secring=<path/to/secring.gpg> -Pgpg_passphrase=<gpg-passphrase> -PrepositoryUsername=<name> -PrepositoryPassword=<passwd
```
with either "mavencentral" or "bintray" for `<repository>`.

## Documentation

The generated javadoc documentation can be found
[here](http://hbm.github.io/java-scan/javadoc/).
