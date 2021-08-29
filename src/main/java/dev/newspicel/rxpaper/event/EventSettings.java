package dev.newspicel.rxpaper.event;

import org.bukkit.event.EventPriority;

public record EventSettings(EventPriority eventPriority, boolean ignoreCancelled) {}
