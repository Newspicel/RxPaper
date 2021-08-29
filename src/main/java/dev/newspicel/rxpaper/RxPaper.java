package dev.newspicel.rxpaper;

import dev.newspicel.rxpaper.command.BukkitCommandExecutor;
import dev.newspicel.rxpaper.command.Command;
import dev.newspicel.rxpaper.event.BukkitEventExecutor;
import dev.newspicel.rxpaper.event.EventSettings;
import dev.newspicel.rxpaper.scheduler.SchedulerWorker;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Class RxPaper has only Static Methods
 * Class the make the work with Minecraft and RxJava easier
 *
 * @author Newspicel | Julian
 */
@SuppressWarnings("unused")
public class RxPaper {

    private RxPaper() {
    }

    private static final EventSettings DEFAULT_EVENT_SETTINGS = new EventSettings(EventPriority.NORMAL, false);

    /**
     * Return a RxJava Scheduler, all Tasks run over the main Thread from the Minecraft Server.
     *
     * @param plugin the current using minecraft plugin instance
     * @return a RxJava Scheduler,
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Scheduler io(Plugin plugin) {
        return new Scheduler() {
            @Override
            public @NonNull Worker createWorker() {
                return new SchedulerWorker(plugin);
            }
        };
    }

    /**
     * Event observable. Reactive API for Minecraft Events
     *
     * @param <T>    the Event type
     * @param plugin the current using minecraft plugin instance
     * @param clazz  the Event class
     * @return the observable
     */
    public static <T extends Event> @NotNull Observable<T> event(Plugin plugin, Class<T> clazz) {
        return event(plugin, clazz, DEFAULT_EVENT_SETTINGS);
    }

    /**
     * Event observable. Reactive API for Minecraft Events
     *
     * @param <T>           the Event type
     * @param plugin        the current using minecraft plugin instance
     * @param clazz         the Event class
     * @param eventSettings the event settings. Priority and ignoreCancelled
     * @return the observable
     */
    public static <T extends Event> @NotNull Observable<T> event(Plugin plugin, Class<T> clazz, EventSettings eventSettings) {
        return Observable.create(emitter -> {
            BukkitEventExecutor<T> eventExecutor = new BukkitEventExecutor<>(emitter);
            Listener listener = new Listener() {
            };
            plugin.getServer().getPluginManager().registerEvent(clazz, listener, eventSettings.eventPriority(), eventExecutor, plugin, eventSettings.ignoreCancelled());

            emitter.setCancellable(() -> HandlerList.unregisterAll(listener));
        });
    }

    /**
     * Command observable. Reactive API for Minecraft Commands
     *
     * @param plugin  the current using minecraft plugin instance
     * @param command the command string / label
     * @return the observable
     */
    public static @NotNull Observable<Command> command(Plugin plugin, String command) {
        return Observable.create(emitter -> {
            BukkitCommandExecutor bukkitCommandExecutor = new BukkitCommandExecutor(emitter);
            PluginCommand pluginCommand = Objects.requireNonNull(plugin.getServer().getPluginCommand(command));
            pluginCommand.setExecutor(bukkitCommandExecutor);

            emitter.setCancellable(() -> pluginCommand.setExecutor(null));
        });
    }
}
