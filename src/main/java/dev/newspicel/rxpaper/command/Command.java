package dev.newspicel.rxpaper.command;

import org.bukkit.command.CommandSender;

import java.util.List;

public record Command(org.bukkit.command.Command command, CommandSender commandSender, String label, List<String> args) {}
