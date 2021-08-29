package dev.newspicel.rxpaper;

import dev.newspicel.rxpaper.command.BukkitCommandExecutor;
import dev.newspicel.rxpaper.command.Command;
import dev.newspicel.rxpaper.event.BukkitEventExecutor;
import dev.newspicel.rxpaper.event.EventSettings;
import dev.newspicel.rxpaper.scheduler.SchedulerWorker;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.Objects;

public class RxPaper {

    private static final EventSettings DEFAULT_EVENT_SETTINGS = new EventSettings(EventPriority.NORMAL, false);

    public static Scheduler io(Plugin plugin) {
        return new Scheduler() {
            @Override
            public @NonNull Worker createWorker() {
                return new SchedulerWorker(plugin);
            }
        };
    }

    public static <T extends Event> Observable<T> event(Plugin plugin, Class<T> clazz, EventSettings eventSettings) {
        return Observable.create(emitter -> {
            BukkitEventExecutor<T> eventExecutor = new BukkitEventExecutor<T>(emitter);
            Listener listener = new Listener() {};
            plugin.getServer().getPluginManager().registerEvent(clazz, listener, eventSettings.eventPriority(), eventExecutor, plugin, eventSettings.ignoreCancelled());

            emitter.setCancellable(() -> HandlerList.unregisterAll(listener));
        });
    }

    public static Observable<Command> command(Plugin plugin, String command){
        return Observable.create(emitter -> {
            BukkitCommandExecutor bukkitCommandExecutor = new BukkitCommandExecutor(emitter);
            PluginCommand pluginCommand = Objects.requireNonNull(plugin.getServer().getPluginCommand(command));
            pluginCommand.setExecutor(bukkitCommandExecutor);

            emitter.setCancellable(() -> pluginCommand.setExecutor(null));
        });
    }

    public static <T extends Event> Observable<T> event(Plugin plugin, Class<T> clazz) {
        return event(plugin, clazz, DEFAULT_EVENT_SETTINGS);
    }


}
