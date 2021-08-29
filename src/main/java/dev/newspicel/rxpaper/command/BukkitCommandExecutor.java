package dev.newspicel.rxpaper.command;

import io.reactivex.rxjava3.core.ObservableEmitter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public record BukkitCommandExecutor(ObservableEmitter<Command> observableEmitter) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {
            observableEmitter.onNext(new Command(command, sender, label, Arrays.asList(args)));
        } catch (Exception ex) {
            observableEmitter.onError(ex);
        }
        return !observableEmitter.isDisposed();
    }
}
