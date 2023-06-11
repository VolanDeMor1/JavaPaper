<div align="center">

<a href="#">
  <img src="https://raw.githubusercontent.com/VolanDeMor1/JavaPaper/master/images/banner.png" alt="JavaPaperAPI" draggable="false">
</a>
<br/><br/>
Simple Kotlin/Java Paper API implementation
<br/><br/>

[![JitPack](https://jitpack.io/v/VolanDeMor1/JavaPaper.svg)](https://jitpack.io/#VolanDeMor1/JavaPaper)
![Kotlin](https://img.shields.io/badge/kotlin-%23f5336d.svg?style=flat&logo=kotlin&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=flat&logo=Gradle&logoColor=white)
</div>

<a href="#">
<img src="https://raw.githubusercontent.com/VolanDeMor1/JavaPaper/master/images/installation.png" alt="Installation" draggable="false">
</a>
<br/>
<p> </p>

<a href="#">
<img src="https://raw.githubusercontent.com/VolanDeMor1/JavaPaper/master/images/maven.png" alt="Maven" draggable="false">
</a><br/>
<p> </p>

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
```xml
<dependency>
    <groupId>com.github.VolanDeMor1</groupId>
    <artifactId>JavaPaper</artifactId>
    <version>VERSION</version>
</dependency>
```

<br/>
<a href="#">
<img src="https://raw.githubusercontent.com/VolanDeMor1/JavaPaper/master/images/gradle_kts.png" alt="Gradle Kotlin" draggable="false">
</a><br/>
<p> </p>

```kotlin
maven("https://jitpack.io")
```
```kotlin
implementation("com.github.VolanDeMor1:JavaPaper:VERSION")
```

<a href="#">
<br/>
<img src="https://raw.githubusercontent.com/VolanDeMor1/JavaPaper/master/images/gradle_grv.png" alt="Gradle Groovy" draggable="false">
</a><br/>
<p> </p>

```groovy
maven { url 'https://jitpack.io' }
```
```groovy
implementation 'com.github.VolanDeMor1:JavaPaper:VERSION'
```

<br/><br/>
<a href="#">
<img src="https://raw.githubusercontent.com/VolanDeMor1/JavaPaper/master/images/usage.png" alt="Usage" draggable="false">
</a><br/>

<a href="#">
<img src="https://raw.githubusercontent.com/VolanDeMor1/JavaPaper/master/images/kotlin.png" alt="Kotlin" draggable="false">
</a><br/>
<p> </p>

```kotlin
val javaPaper = JavaPaper()

runBlocking {
    // Getting information about all PaperMC projects
    for (projectType in javaPaper.projects().projects) {
        val project = javaPaper.project(projectType)
        println(project.projectType) // OUTPUT: PAPER
        println(project.projectName) // OUTPUT: Paper
        println(project.versionGroups.joinToString()) // OUTPUT: 1.8, ... 1.19, 1.20
        println(project.versions.joinToString()) // OUTPUT: 1.8.8, ... 1.19.3, 1.19.4, 1.20
    }
    
    // Getting all Paper builds for Minecraft 1.20
    println(javaPaper.project(ProjectType.PAPER)
        .version("1.20")
        .builds.joinToString()
    ) // OUTPUT: 1, 2, 3, 4 ....
    
    // Download latest Paper build for Minecraft 1.20 in downloads folder
    javaPaper.builds(ProjectType.PAPER, "1.20")
        .builds.last().downloads.application
        .downloadAutoName(Path.of("downloads"))
        .percentage { percent, speed -> // OPTIONAL
            println("Downloaded $percent%... (Speed: $speed mb/s)")
        }
        .after { // OPTIONAL
            println("Downloaded successfully!")
        }
        .start() // Starts download
}
```

<br/>