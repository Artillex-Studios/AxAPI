package com.artillexstudios.axapi.command;

import com.artillexstudios.axapi.BuildConstants;
import com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper;
import com.artillexstudios.axapi.nms.wrapper.ServerWrapper;
import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.PacketSide;
import com.artillexstudios.axapi.packet.ServerboundPacketTypes;
import com.artillexstudios.axapi.reflection.FieldAccessor;
import com.artillexstudios.axapi.utils.ItemBuilder;
import com.artillexstudios.axapi.utils.MessageUtils;
import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axapi.utils.Version;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.http.Requests;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Command("axapi")
@CommandPermission("axapi.command")
public final class AxAPICommand {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String PREFIX = "<#00CCFF><b>AxAPI</b> <gray>» ";
    private static final Gson gson = new Gson();

    @Subcommand("dump")
    @CommandPermission("axapi.command.dump")
    public void dumpPlugins(CommandSender sender) {
        String dump = """
                AxAPI DUMP @ %time%
                Server software: %server-software%
                Server version: %server-version%
                Minecraft version: %minecraft-version%
                Bukkit version: %bukkit-version%
                AxAPI NMS version: %version-axapi%
                AxAPI protocol version: %protocol-version%
                AxAPI build: %axapi-build%
                
                Ax plugins:
                %axplugins%
                
                Other plugins:
                %otherplugins%
                """;

        StringBuilder axPlugins = new StringBuilder();
        StringBuilder otherPlugins = new StringBuilder();
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            String axPackage = this.findAxPackage(plugin);
            if (axPackage == null) {
                String otherPlugin = """
                        ----%name%----
                        Version: %version%
                        Enabled: %enabled%
                        """.replace("%name%", plugin.getName())
                        .replace("%version%", plugin.getDescription().getVersion())
                        .replace("%enabled%", String.valueOf(plugin.isEnabled()));
                otherPlugins.append(otherPlugin).append('\n');

                continue;
            }

            FieldAccessor accessor = FieldAccessor.builder()
                    .withClass(axPackage.replace("/", ".") + "BuildConstants")
                    .withField("VERSION")
                    .build();
            if (FeatureFlags.DEBUG.get()) {
                LogUtils.debug("Getting accessor for class: {}", axPackage.replace("/", ".") + "BuildConstants");
            }

            String axPlugin = """
                    ----%name%----
                    Version: %version%
                    AxAPI version: %axapi-version%
                    Enabled: %enabled%
                    """.replace("%name%", plugin.getName())
                    .replace("%version%", plugin.getDescription().getVersion())
                    .replace("%enabled%", String.valueOf(plugin.isEnabled()))
                    .replace("%axapi-version%", accessor == null ? "UNKNOWN VERSION" : accessor.get(null, String.class));
            axPlugins.append(axPlugin).append('\n');
        }

        this.upload(dump.replace("%time%", formatter.format(LocalDateTime.now()))
                .replace("%server-software%", Bukkit.getName())
                .replace("%server-version%", Bukkit.getVersion())
                .replace("%minecraft-version%", String.join("/", Version.getServerVersion().getVersions()))
                .replace("%bukkit-version%", Bukkit.getBukkitVersion())
                .replace("%version-axapi%", Version.getServerVersion().getNMSVersion())
                .replace("%protocol-version%", String.valueOf(Version.getServerVersion().getProtocolId()))
                .replace("%axapi-build%", BuildConstants.VERSION)
                .replace("%axplugins%", axPlugins)
                .replace("%otherplugins%", otherPlugins)
        ).thenAccept(response -> {
            String body = response.body();
            if (body == null || body.isBlank()) {
                MessageUtils.sendMessage(sender, PREFIX, "<red>Failed to upload data!");
                return;
            }

            JsonObject object = gson.fromJson(body, JsonObject.class);
            String key = object.get("key").getAsString();
            if (!(sender instanceof Player player)) {
                MessageUtils.sendMessage(sender, PREFIX, "<green>Successfully uploaded data to our paste service! URL: <click:open_url:'https://paste.artillex-studios.com/%key%'>https://paste.artillex-studios.com/%key%</click>".replace("%key%", key));
            } else {
                Component message = MiniMessage.miniMessage().deserialize(PREFIX + "<green>Successfully uploaded data to our paste service! URL: <click:open_url:'https://paste.artillex-studios.com/%key%'>https://paste.artillex-studios.com/%key%</click>".replace("%key%", key));
                ServerPlayerWrapper wrapper = ServerPlayerWrapper.wrap(player);
                wrapper.message(message);
            }
        });
    }

    @Subcommand("print")
    @CommandPermission("axapi.command.print")
    public void printPlugins(CommandSender sender) {
        String dump = """
                AxAPI DUMP @ %time%
                Server software: %server-software%
                Server version: %server-version%
                Minecraft version: %minecraft-version%
                Bukkit version: %bukkit-version%
                AxAPI NMS version: %version-axapi%
                AxAPI protocol version: %protocol-version%
                AxAPI build: %axapi-build%
                
                Ax plugins:
                %axplugins%
                
                Packets:
                %packets%
                """;

        StringBuilder axPlugins = new StringBuilder();
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            String axPackage = this.findAxPackage(plugin);
            if (axPackage == null) {
                continue;
            }

            FieldAccessor accessor = FieldAccessor.builder()
                    .withClass(axPackage.replace("/", ".") + "BuildConstants")
                    .withField("VERSION")
                    .build();
            if (FeatureFlags.DEBUG.get()) {
                LogUtils.debug("Getting accessor for class: {}", axPackage.replace("/", ".") + "BuildConstants");
            }

            String axPlugin = """
                    ----%name%----
                    Version: %version%
                    AxAPI version: %axapi-version%
                    Enabled: %enabled%
                    """.replace("%name%", plugin.getName())
                    .replace("%version%", plugin.getDescription().getVersion())
                    .replace("%enabled%", String.valueOf(plugin.isEnabled()))
                    .replace("%axapi-version%", accessor == null ? "UNKNOWN VERSION" : accessor.get(null, String.class));
            axPlugins.append(axPlugin).append('\n');
        }

        String packets = """
                AXAPI Registered:
                Clientbound:
                %clientbound%
                
                Serverbound:
                %serverbound%
                
                Server registered:
                Clientbound:
                %server-clientbound%
                
                Serverbound:
                %server-serverbound%
                """.replace("%clientbound%", ClientboundPacketTypes.dump())
                .replace("%serverbound%", ServerboundPacketTypes.dump())
                .replace("%server-clientbound%", ServerWrapper.INSTANCE.listPackets(PacketSide.CLIENT_BOUND))
                .replace("%server-serverbound%", ServerWrapper.INSTANCE.listPackets(PacketSide.SERVER_BOUND));

        this.upload(dump.replace("%time%", formatter.format(LocalDateTime.now()))
                .replace("%server-software%", Bukkit.getName())
                .replace("%server-version%", Bukkit.getVersion())
                .replace("%minecraft-version%", String.join("/", Version.getServerVersion().getVersions()))
                .replace("%bukkit-version%", Bukkit.getBukkitVersion())
                .replace("%version-axapi%", Version.getServerVersion().getNMSVersion())
                .replace("%protocol-version%", String.valueOf(Version.getServerVersion().getProtocolId()))
                .replace("%axapi-build%", BuildConstants.VERSION)
                .replace("%axplugins%", axPlugins)
                .replace("%packets%", packets.toString())
        ).thenAccept(response -> {
            String body = response.body();
            if (body == null || body.isBlank()) {
                MessageUtils.sendMessage(sender, PREFIX, "<red>Failed to upload data!");
                return;
            }

            JsonObject object = gson.fromJson(body, JsonObject.class);
            String key = object.get("key").getAsString();
            if (!(sender instanceof Player player)) {
                MessageUtils.sendMessage(sender, PREFIX, "<green>Successfully uploaded data to our paste service! URL: <click:open_url:'https://paste.artillex-studios.com/%key%'>https://paste.artillex-studios.com/%key%</click>".replace("%key%", key));
            } else {
                Component message = MiniMessage.miniMessage().deserialize(PREFIX + "<green>Successfully uploaded data to our paste service! URL: <click:open_url:'https://paste.artillex-studios.com/%key%'>https://paste.artillex-studios.com/%key%</click>".replace("%key%", key));
                ServerPlayerWrapper wrapper = ServerPlayerWrapper.wrap(player);
                wrapper.message(message);
            }
        });
    }

    private String findAxPackage(Plugin plugin) {
        try (ZipFile file = new ZipFile(Paths.get(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).toFile())) {
            Iterator<? extends ZipEntry> iterator = file.entries().asIterator();
            while (iterator.hasNext()) {
                ZipEntry entry = iterator.next();
                if (entry.getName().contains("AxPlugin.class")) {
                    return entry.getName().replace("AxPlugin.class", "");
                }
            }
        } catch (URISyntaxException | IOException exception) {
            LogUtils.error("Failed to get plugins!", exception);
        }

        return null;
    }

    private CompletableFuture<HttpResponse<String>> upload(String data) {
        return Requests.postAsyncString("https://paste.artillex-studios.com/api/post", Map.of("User-Agent", "axapi", "Bytebin-Api-Key", "axplugin", "Content-Type", "text/plain"), () -> data);
    }

    @Subcommand("snbt")
    @CommandPermission("axapi.command.snbt")
    public void dumpSNBT(Player player) {
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack == null || stack.getType().isAir()) {
            MessageUtils.sendMessage(player, PREFIX, "<red>You need to be holding an item in your main hand!");
            return;
        }
        ItemBuilder builder = ItemBuilder.create(stack);

        String serialized = (String) builder.serialize(true).get("snbt");
        String escaped = MiniMessage.miniMessage().escapeTags(serialized);
        Component message = StringUtils.format(PREFIX).append(Component.text("Dumped data! Click to copy to clipboard: " + escaped)
                .color(NamedTextColor.GREEN)
                .clickEvent(ClickEvent.copyToClipboard(escaped))
        );

        ServerPlayerWrapper wrapper = ServerPlayerWrapper.wrap(player);
        wrapper.message(message);
    }
}
