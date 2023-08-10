package nade.empty.horse.listeners;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import nade.empty.horse.mechanics.HorseObject;

public class Handlers {
    public static final Map<UUID, HorseObject> mounts = Maps.newLinkedHashMap();
    public static final Map<UUID, Date> MOUNT_COOLDOWN = Maps.newLinkedHashMap();
}
