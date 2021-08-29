![GitHub](https://img.shields.io/github/license/newspicel/RxPaper) 
![GitHub issues](https://img.shields.io/github/issues/newspicel/rxpaper)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/newspicel/rxpaper)
![GitHub release (latest by date and asset)](https://img.shields.io/github/downloads/newspicel/rxpaper/latest/rxpaper-1.0-all.jar)
# RxPaper
A small Reactive API with RxJava for Minecraft Paper Plugins.

## Usage

#### RxPaper.io()
When you want to bing something back in the Minecraft Thread
````java
public void test(Plugin plugin) {
    Flowable.just("").subscribeOn(RxPaper.io(plugin)).subscribe();
}
````
#### RxPaper.event()
When you want to listen an Event
````java
public void test(Plugin plugin) {
    RxPaper.event(plugin, PlayerJoinEvent.class).map(PlayerEvent::getPlayer).subscribe(player -> player.sendMessage("Hello"));
    RxPaper.event(plugin, PlayerJoinEvent.class, new EventSettings(EventPriority.MONITOR, false)).map(PlayerEvent::getPlayer).subscribe(player -> player.sendMessage("Hello later"));
}
````
#### RxPaper.command()
When you want to listen a Command
````java
 public void test(Plugin plugin) {
    RxPaper.command(plugin, "test").subscribe(command -> command.commandSender().sendMessage("This is a command"));
}
````


## Installation

### Gradle
````kotlin
repositories {
    mavenCentral()
    maven("https://maven.pkg.github.com/newspicel/rxjava")
}    


dependencies {
    implementation("dev.newspicel.rxpaper:rxpaper:1.0")
}
````

````groovy
repositories {
    maven {
        url = 'https://maven.pkg.github.com/newspicel/rxjava'
    }
}

dependencies {
    implementation("dev.newspicel.rxpaper:rxpaper:1.0")
}
````

### Maven
````xml
<repository>
     <id>github</id>
     <url>https://maven.pkg.github.com/newspicel/rxjava</url>
</repository>

<dependency>
    <groupId>dev.newspicel.rxpaper</groupId>
    <artifactId>rxpaper</artifactId>
    <version>1.0</version>
</dependency>
````
