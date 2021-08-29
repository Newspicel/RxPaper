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
````groovy

````

### Maven
````xml

````
