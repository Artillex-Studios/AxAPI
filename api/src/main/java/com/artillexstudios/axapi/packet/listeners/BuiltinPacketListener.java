package com.artillexstudios.axapi.packet.listeners;

import com.artillexstudios.axapi.events.PacketEntityInteractEvent;
import com.artillexstudios.axapi.gui.SignInput;
import com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper;
import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketListener;
import com.artillexstudios.axapi.packet.ServerboundPacketTypes;
import com.artillexstudios.axapi.packet.wrapper.clientbound.ClientboundAddEntityWrapper;
import com.artillexstudios.axapi.packet.wrapper.clientbound.ClientboundBlockUpdateWrapper;
import com.artillexstudios.axapi.packet.wrapper.clientbound.ClientboundSetPassengersWrapper;
import com.artillexstudios.axapi.packet.wrapper.serverbound.ServerboundInteractWrapper;
import com.artillexstudios.axapi.packet.wrapper.serverbound.ServerboundSignUpdateWrapper;
import com.artillexstudios.axapi.packetentity.PacketEntity;
import com.artillexstudios.axapi.packetentity.tracker.EntityTracker;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import org.bukkit.Bukkit;

public final class BuiltinPacketListener extends PacketListener {
    private final EntityTracker tracker;

    public BuiltinPacketListener(EntityTracker tracker) {
        this.tracker = tracker;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (event.type() == ClientboundPacketTypes.ADD_ENTITY && FeatureFlags.LISTEN_TO_RIDE_PACKET.get()) {
            if (this.tracker == null) {
                return;
            }

            ClientboundAddEntityWrapper wrapper = new ClientboundAddEntityWrapper(event);
            int entityId = wrapper.entityId();

            PacketEntity rider = this.tracker.findRider(entityId);
            if (rider == null) {
                return;
            }

            ClientboundSetPassengersWrapper passengersWrapper = new ClientboundSetPassengersWrapper(entityId, new int[]{rider.id()});
            ServerPlayerWrapper serverPlayerWrapper = ServerPlayerWrapper.wrap(event.player());
            serverPlayerWrapper.sendPacket(passengersWrapper);
        }
    }

    @Override
    public void onPacketReceive(PacketEvent event) {
        if (event.type() == ServerboundPacketTypes.INTERACT) {
            if (this.tracker == null) {
                return;
            }

            ServerboundInteractWrapper wrapper = new ServerboundInteractWrapper(event);
            PacketEntity entity = this.tracker.getById(wrapper.entityId());
            if (entity != null) {
                PacketEntityInteractEvent interactEvent = new PacketEntityInteractEvent(event.player(), entity, wrapper.type() == ServerboundInteractWrapper.ActionType.ATTACK, wrapper.action() instanceof ServerboundInteractWrapper.InteractionAtLocationAction action ? action.location() : null, wrapper.action() instanceof ServerboundInteractWrapper.InteractionAction action ? action.hand() : wrapper.action() instanceof ServerboundInteractWrapper.InteractionAtLocationAction interaction ? interaction.hand() : null);
                Bukkit.getPluginManager().callEvent(interactEvent);
            }
        } else if (event.type() == ServerboundPacketTypes.SIGN_UPDATE) {
            ServerboundSignUpdateWrapper wrapper = new ServerboundSignUpdateWrapper(event);
            SignInput signInput = SignInput.remove(event.player());

            if (signInput == null) {
                return;
            }

            signInput.getListener().accept(event.player(), wrapper.lines());
            com.artillexstudios.axapi.scheduler.Scheduler.get().runAt(signInput.getLocation(), task -> {
                ServerPlayerWrapper playerWrapper = ServerPlayerWrapper.wrap(event.player());
                playerWrapper.sendPacket(new ClientboundBlockUpdateWrapper(signInput.getLocation(), signInput.getLocation().getBlock().getType()));
            });
        }
    }
}
