package dev.newspicel.rxpaper.event;

import io.reactivex.rxjava3.core.ObservableEmitter;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public record BukkitEventExecutor<T extends Event>(ObservableEmitter<T> observableEmitter) implements EventExecutor {

    @Override
    @SuppressWarnings("unchecked")
    public void execute(@NotNull Listener listener, @NotNull Event event) {
        try {
            observableEmitter.onNext(((T) event));
        } catch (Exception ex) {
            observableEmitter.onError(ex);
        }
    }
}
