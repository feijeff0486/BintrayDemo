# BintrayDemo
A sample for bintray config.


# 发布个人项目到Maven、JCenter、JitPack

## 一、发布个人项目到Maven
[gradle-bintray-plugin官方文档](https://github.com/bintray/gradle-bintray-plugin/blob/master/README.md)

Step 1. Add plugins in your root build.gradle:

```
buildscript {
    
    repositories {
        maven {
            url "https://oss.sonatype.org/content/repositories/snapshots/"
        }
        maven {
            url 'http://dl.bintray.com/jetbrains/intellij-plugin-service'
        }
        mavenCentral()
        
        google()
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.2.0'

        classpath 'com.jfrog.bintray.gradle:gradle-bintray-plugin:1.2'
        classpath 'com.github.dcendents:android-maven-gradle-plugin:2.1'
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}
...
```
Step 2. Add script in your project root path.

Script 1. bintray.gradle
```
apply plugin: 'com.jfrog.bintray'

version = libraryVersion

//if (project.hasProperty("android")) { // Android libraries
//    task sourcesJar(type: Jar) {
//        classifier = 'sources'
//        from android.sourceSets.main.java.srcDirs
//    }
//
//    task javadoc(type: Javadoc) {
//        source = android.sourceSets.main.java.srcDirs
//        classpath += project.files(android.getBootClasspath().join(File.pathSeparator))
//    }
//} else { // Java libraries
//    task sourcesJar(type: Jar, dependsOn: classes) {
//        classifier = 'sources'
//        from sourceSets.main.allSource
//    }
//}

//task javadocJar(type: Jar, dependsOn: javadoc) {
//    classifier = 'javadoc'
//    from javadoc.destinationDir
//}

//artifacts {
////    archives javadocJar
//    archives sourcesJar
//}

// Bintray
Properties properties = new Properties()
properties.load(project.rootProject.file('local.properties').newDataInputStream())

bintray {
    //从local.properties获取
    user = properties.getProperty("bintray.user")
    key = properties.getProperty("bintray.apikey")
    //从系统环境变量中获取
//    user = System.getenv('BINTRAY_USER')
//    key = System.getenv('BINTRAY_API_KEY')

    configurations = ['archives']
    pkg {
        repo = bintrayRepo
        name = bintrayName
        userOrg = properties.getProperty("bintray.user")
        desc = libraryDescription
        websiteUrl = siteUrl
        vcsUrl = gitUrl

        licenses = ['Apache-2.0']
        publish = true
        publicDownloadNumbers = true
        version {
            name = libraryVersion
            desc = libraryDescription
            gpg {
//                sign = true //Determines whether to GPG sign the files. The default is false
//                passphrase = properties.getProperty("bintray.gpg.password")
                //Optional. The passphrase for GPG signing'
            }
        }
    }
}
```
Script 2. install.gradle

```
apply plugin: 'com.github.dcendents.android-maven'

group = publishedGroupId

install {
    repositories.mavenInstaller {
        // This generates POM.xml with proper parameters
        pom {
            project {
                packaging 'aar'
                groupId publishedGroupId
                artifactId artifact
                version libraryVersion

                // Add your description here
                name libraryName
                description libraryDescription
                url siteUrl

                // Set your license
                licenses {
                    license {
                        name 'The Apache Software License, Version 2.0'
                        url 'http://www.apache.org/licenses/LICENSE-2.0.txt'
                    }
                }
                developers {
                    developer {
                        id developerId
                        name developerName
                        email developerEmail
                    }
                }
                scm {
                    connection gitUrl
                    developerConnection gitUrl
                    url siteUrl
                }
            }
        }
    }
}
```
Step 3. 在要上传module的build.gradle添加配置

```
apply plugin: 'com.android.library'

apply from: '../install.gradle'
apply from: '../bintray.gradle'

ext {
    //bintrayName尽量和module名称保持一致
    bintrayName = 'bintray-core'
    artifact = bintrayName
    libraryName = 'Bintray library'
    libraryDescription = 'A bintrary library for android'
    libraryVersion = bintray_core_version
}
...
```

Step 4. gradle.properties的一些配置
```
bintray_tool_version=1.0.0
bintray_core_version=1.0.0

bintrayRepo=maven
publishedGroupId=com.jeff.bintray
siteUrl=https://github.com/feijeff0486/BintrayDemo
gitUrl=https://github.com/feijeff0486/BintrayDemo.git
developerId=afei
developerName=fei.zhang
developerEmail=feijeff0486@gmail.com
```

Step 5. 在local.properties配置bintray账号密码等私密信息

```
bintray.user=xxx
bintray.apikey=xxx
```


Step 6. 执行上传命令

选中执行AndroidStudio右侧的module下的/Tasks/publishing/bintrayUpload，亦或执行下面命令：
```
gradle build bintrayUpload -PbintrayUser=<my user> -PbintrayApiKey=<my key>
```

## 二、发布个人项目到[JCenter](https://bintray.com)
Step 1.登录[JCenter](https://bintray.com)打开个人中心  打开上传的包详情.

Step 2. 点击 “add to JCenter” button (如果没有显示则点击头部的 “go to old look” 按钮).

## 三、发布个人项目到[JitPack](https://jitpack.io/)
Step 1. Upload your code to github.

Step 2. Build new version for your library.

Step 3. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 4. Add the dependency

	dependencies {
	        implementation 'com.github.feijeff0486:JeffTools:Tag'
	}
