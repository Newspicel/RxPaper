package dev.newspicel.rxpaper.scheduler;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.concurrent.TimeUnit;

public class SchedulerWorker extends Scheduler.Worker{

    private final Plugin plugin;
    private static final BukkitScheduler scheduler = Bukkit.getScheduler();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();


    public SchedulerWorker(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NonNull Disposable schedule(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
        if (compositeDisposable.isDisposed()) {
            return Disposable.disposed();
        }
        final int taskID = scheduler.scheduleSyncDelayedTask(plugin, run, convertToTicks(delay, unit));
        if (taskID < 0) {
            return Disposable.disposed();
        }
        final Disposable disposable = new Disposable() {
            @Override
            public void dispose() {
                scheduler.cancelTask(taskID);
            }

            @Override
            public boolean isDisposed() {
                return !(scheduler.isQueued(taskID) || scheduler.isCurrentlyRunning(taskID));
            }
        };
        compositeDisposable.add(disposable);
        return disposable;
    }

    @Override
    public void dispose() {
        compositeDisposable.dispose();
    }

    @Override
    public boolean isDisposed() {
        return compositeDisposable.isDisposed();
    }

    private long convertToTicks(final long delay, final TimeUnit unit) {
        return Math.round(unit.toMillis(delay) * 0.02);
    }
}
