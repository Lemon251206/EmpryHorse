package nade.empty.horse.commands;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Horse.Color;

import nade.empty.horse.EmptyHorse;
import nade.empty.horse.listeners.Handlers;
import nade.empty.horse.mechanics.Database;
import nade.empty.horse.mechanics.HorseObject;
import nade.empty.horse.utils.CustomConfig;
import nade.empty.horse.utils.strings.Colors;
import nade.empty.horse.utils.strings.DateFormat;

public class Commands implements CommandExecutor{
    private EmptyHorse plugin;

    private Configuration config = CustomConfig.get("config.yml");

    public Commands(EmptyHorse plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!isPlayer(sender)) {
            sender.sendMessage(Colors.vanilla("&cChỉ người chơi mới được sử dụng lệnh này!!!"));
            return false;
        }
        if (args.length == 0) {
            return onInfo(sender);
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("doiten")) {
            return onChangeDisplayname(sender, label, args);
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("doimau")) {
            return onChangeColor(sender, label, args);
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("lenngua")) {
            return onMount(sender);
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("thuengua")) {
            return onRent(sender);
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("muangua")) {
            return onBuy(sender);
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("giahan")) {
            return onExtension(sender);
        }
        sender.sendMessage(Colors.vanilla("&cLệnh không tồn tại!"));
        return false;
    }

    private boolean onInfo(CommandSender sender) {
        Player player = (Player) sender;

        if (!Database.contains(player.getUniqueId())) {
            player.sendMessage(Colors.vanilla("&cBạn không sở hữu bất kỳ con ngựa nào!"));
            return false;
        }

        HorseObject horse = Database.get(player.getUniqueId());

        sender.sendMessage(Colors.vanilla("&8>&7>&f>  &3EmptyHorse  &f<&7<&8<"));
        sender.sendMessage(Colors.vanilla("&7Tên: &r{displayname}".replace("{displayname}", Objects.isNull(horse.getDisplayname()) ? "&ckhông có" : horse.getDisplayname())));
        sender.sendMessage(Colors.vanilla("&7Màu: &f{color}".replace("{color}", Objects.isNull(horse.getColor()) ? "&cngẫu nhiên" : horse.getColor().name())));
        if (Objects.isNull(horse.getDuration())) {
            sender.sendMessage(Colors.vanilla("&7Thời hạn: &avĩnh viễn"));
        }else {
            sender.sendMessage(Colors.vanilla("&7Thời hạn: &f{duration}".replace("{duration}", DateFormat.format(horse.getDuration()).equalsIgnoreCase("0 giây") ? "&chết hạn" : DateFormat.format(horse.getDuration()))));
        }
        return true;
    }

    private boolean onChangeDisplayname(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;

        if (args.length != 2) {
            player.sendMessage(Colors.vanilla("&cVui lòng sử dụng &7/{label} doiten <tên mới> &cđể đổi tên cho ngựa của bạn.").replace("{label}", label));
            return false;
        }

        if (!Database.contains(player.getUniqueId())) {
            player.sendMessage(Colors.vanilla("&cBạn không sở hữu bất kỳ con ngựa nào!"));
            return false;
        }

        HorseObject horse = Database.get(player.getUniqueId());
        if (!Objects.isNull(horse.getDuration())) {
            if (new Date().getTime() > horse.getDuration().getTime()) {
                Database.remove(player.getUniqueId());
                player.sendMessage(Colors.vanilla("&cNgựa của bạn đã hết thời hạn thuê, vui lòng gia hạn hoặc thuê một con ngựa khác!!"));
                return false;
            }
        }

        if (Handlers.mounts.containsKey(player.getUniqueId())) {
            Handlers.mounts.get(player.getUniqueId()).dismount();
        }

        horse.setDisplayname(args[1]);
        Database.update(player.getUniqueId(), horse);
        player.sendMessage("&7Bạn đã đổi tên của ngựa thành: &r" + args[1]);

        return true;
    }

    private boolean onChangeColor(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;

        if (args.length != 2) {
            player.sendMessage(Colors.vanilla("&cVui lòng sử dụng &7/{label} doimau <màu> &cđể đổi tên cho ngựa của bạn.").replace("{label}", label));
            player.sendMessage(Colors.vanilla("&7Danh sách màu:"));
            for (Color color : Color.values()) {
                player.sendMessage(" " + color.name());
            }
            return false;
        }

        if (!Database.contains(player.getUniqueId())) {
            player.sendMessage(Colors.vanilla("&cBạn không sở hữu bất kỳ con ngựa nào!"));
            return false;
        }

        HorseObject horse = Database.get(player.getUniqueId());
        if (!Objects.isNull(horse.getDuration())) {
            if (new Date().getTime() > horse.getDuration().getTime()) {
                Database.remove(player.getUniqueId());
                player.sendMessage(Colors.vanilla("&cNgựa của bạn đã hết thời hạn thuê, vui lòng gia hạn hoặc thuê một con ngựa khác!!"));
                return false;
            }
        }

        Color color = null;
        try {
            color = Color.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(Colors.vanilla("&cMàu &4{color} &ckhông tồn tại, vui lòng thử lại!!").replace("{color}", args[1]));
            return false;
        }

        if (Handlers.mounts.containsKey(player.getUniqueId())) {
            Handlers.mounts.get(player.getUniqueId()).dismount();
        }

        horse.setColor(color);
        Database.update(player.getUniqueId(), horse);
        player.sendMessage(Colors.vanilla("&7Bạn đã đổi màu của ngựa thành: &f" + args[1]));

        return true;
    }

    private boolean onMount(CommandSender sender) {
        Player player = (Player) sender;
        if (!Database.contains(player.getUniqueId())) {
            player.sendMessage(Colors.vanilla("&cBạn không sở hữu bất kỳ con ngựa nào!"));
            return false;
        }
        HorseObject horse = Database.get(player.getUniqueId());
        if (!Objects.isNull(horse.getDuration())) {
            if (new Date().getTime() > horse.getDuration().getTime()) {
                Database.remove(player.getUniqueId());
                player.sendMessage(Colors.vanilla("&cNgựa của bạn đã hết thời hạn thuê, vui lòng gia hạn hoặc thuê một con ngựa khác!!"));
                return false;
            }
        }
        if (Handlers.mounts.containsKey(player.getUniqueId())) {
            player.sendMessage(Colors.vanilla("&cBạn đã ở trên ngựa rồi!"));
            return false;
        }
        if (Handlers.MOUNT_COOLDOWN.containsKey(player.getUniqueId())) {
            long current = new Date().getTime();
            long cooldown = Handlers.MOUNT_COOLDOWN.get(player.getUniqueId()).getTime();
            if (cooldown > current) {
                int time = (int) ((cooldown - current)/1000);
                player.sendMessage(Colors.vanilla("&4" + time + " &cgiây nữa bạn mới có thể lên ngựa!"));
                return false;
            }
        }
        horse.mount();
        return true;
    }

    private boolean onRent(CommandSender sender) {
        Player player = (Player) sender;

        if (Database.contains(player.getUniqueId())) {
            HorseObject horse = Database.get(player.getUniqueId());

            long current = new Date().getTime();
            long duration = horse.getDuration().getTime();

            if (duration > current) {
                player.sendMessage(Colors.vanilla("&cVui lòng đợi hết thời hạn để thuê một con ngựa khác!"));
                return false;
            }
        }

        if (plugin.getEconomy().getBalance(player) < config.getDouble("rent-cost")) {
            player.sendMessage(Colors.vanilla("&cBạn cần &4%cost%&c%money-format% &cđể thuê!")
                                     .replace("%cost%", new DecimalFormat("#,###").format(config.getDouble("rent-cost")))
                                     .replace("%money-format%", config.getString("money-format")));
            return false;
        }

        plugin.getEconomy().withdrawPlayer(player, config.getDouble("rent-cost"));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, config.getInt("rent-time"));

        Database.remove(player.getUniqueId());

        player.sendMessage(Colors.vanilla("&7Bạn đã thuê một con ngựa trong &f" + DateFormat.format(config.getInt("rent-time"))));
        HorseObject horse = Database.register(player.getUniqueId(), calendar.getTime());
        horse.mount();
        return true;
    }

    private boolean onBuy(CommandSender sender) {
        Player player = (Player) sender;

        if (plugin.getEconomy().getBalance(player) < config.getDouble("rent-cost")) {
            player.sendMessage(Colors.vanilla("&cBạn cần &4{cost}&c{money-format} &cđể mua một con ngựa!")
                                     .replace("{cost}", new DecimalFormat("#,###").format(config.getDouble("buy-cost")))
                                     .replace("{money-format}", config.getString("money-format")));
            return false;
        }

        plugin.getEconomy().withdrawPlayer(player, config.getDouble("buy-cost"));
        HorseObject horse = Database.register(player.getUniqueId(), null);
        horse.mount();
        return true;
    }

    private boolean onExtension(CommandSender sender) {
        Player player = (Player) sender;

        if (!Database.contains(player.getUniqueId())) {
            player.sendMessage(Colors.vanilla("&cBạn không sở hữu bất kỳ con ngựa nào!"));
            return false;
        }

        if (plugin.getEconomy().getBalance(player) < config.getDouble("rent-cost")) {
            player.sendMessage(Colors.vanilla("&cBạn cần &4%cost%&c%money-format% &cđể gia hạn ngựa của bạn!")
                                     .replace("%cost%", new DecimalFormat("#,###").format(config.getDouble("rent-cost")))
                                     .replace("%money-format%", config.getString("money-format")));
            return false;
        }

        plugin.getEconomy().withdrawPlayer(player, config.getDouble("rent-cost"));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, config.getInt("rent-time"));

        HorseObject horse = Database.get(player.getUniqueId());
        horse.setDuration(calendar.getTime());
        player.sendMessage(Colors.vanilla("&7Bạn đã gia hạn ngựa thêm &f" + DateFormat.format(config.getInt("rent-time"))));
        Database.update(player.getUniqueId(), horse);
        horse.mount();
        return true;
    }

    private boolean isPlayer(CommandSender sender) {
        try {
            return Objects.isNull(Player.class.cast(sender));
        } catch (Exception e) {
            return false;
        }
    }
}