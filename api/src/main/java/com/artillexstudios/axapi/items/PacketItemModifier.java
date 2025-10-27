package com.artillexstudios.axapi.items;

import com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper;
import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketEvents;
import com.artillexstudios.axapi.packet.PacketListener;
import com.artillexstudios.axapi.packet.ServerboundPacketTypes;
import com.artillexstudios.axapi.packet.data.MerchantOffer;
import com.artillexstudios.axapi.packet.wrapper.clientbound.ClientboundContainerSetContentWrapper;
import com.artillexstudios.axapi.packet.wrapper.clientbound.ClientboundContainerSetSlotWrapper;
import com.artillexstudios.axapi.packet.wrapper.clientbound.ClientboundEntityMetadataWrapper;
import com.artillexstudios.axapi.packet.wrapper.clientbound.ClientboundMerchantOffersWrapper;
import com.artillexstudios.axapi.packet.wrapper.clientbound.ClientboundSetCursorItemWrapper;
import com.artillexstudios.axapi.packet.wrapper.clientbound.ClientboundSetEquipmentWrapper;
import com.artillexstudios.axapi.packet.wrapper.serverbound.ServerboundContainerClickWrapper;
import com.artillexstudios.axapi.packet.wrapper.serverbound.ServerboundSetCreativeModeSlotWrapper;
import com.artillexstudios.axapi.packetentity.meta.Metadata;
import com.artillexstudios.axapi.utils.EquipmentSlot;
import com.artillexstudios.axapi.utils.Pair;
import com.artillexstudios.axapi.utils.ReferenceCountingMap;
import com.artillexstudios.axapi.utils.Version;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.bukkit.entity.Player;

public class PacketItemModifier {
    private static boolean listening = false;
    private static final ObjectArrayList<PacketItemModifierListener> listeners = new ObjectArrayList<>();
    // If we have two items with the same hash, we can't just remove, as a different item might need the same stack
    private static final ReferenceCountingMap<HashedStack, HashedStack> stacks = new ReferenceCountingMap<>();

    public static void registerModifierListener(PacketItemModifierListener listener) {
        if (!listening) {
            PacketEvents.INSTANCE.addListener(new PacketListener() {
                @Override
                public void onPacketSending(PacketEvent event) {
                    if (!PacketItemModifier.isListening()) {
                        return;
                    }

                    if (event.type() == ClientboundPacketTypes.CONTAINER_SET_SLOT) {
                        ClientboundContainerSetSlotWrapper wrapper = new ClientboundContainerSetSlotWrapper(event);
                        PacketItemModifier.callModify(wrapper.stack(), event.player(), PacketItemModifier.Context.SET_SLOT);
                    } else if (event.type() == ClientboundPacketTypes.CONTAINER_CONTENT) {
                        ClientboundContainerSetContentWrapper wrapper = new ClientboundContainerSetContentWrapper(event);
                        for (WrappedItemStack item : wrapper.items()) {
                            PacketItemModifier.callModify(item, event.player(), PacketItemModifier.Context.SET_CONTENTS);
                        }

                        PacketItemModifier.callModify(wrapper.carriedItem(), event.player(), PacketItemModifier.Context.SET_CONTENTS);
                    } else if (event.type() == ClientboundPacketTypes.SET_CURSOR_ITEM) {
                        ClientboundSetCursorItemWrapper wrapper = new ClientboundSetCursorItemWrapper(event);
                        ServerPlayerWrapper serverPlayer = ServerPlayerWrapper.wrap(event.player());
                        LogUtils.debug("Set cursor item!");
                        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_21_4)) {
                            HashedStack hashedStack = wrapper.getItemStack().copy().toHashedStack(serverPlayer.hashGenerator());
                            PacketItemModifier.callModify(wrapper.getItemStack(), event.player(), PacketItemModifier.Context.SET_CURSOR);
                            HashedStack changedHashed = wrapper.getItemStack().copy().toHashedStack(serverPlayer.hashGenerator());
                            stacks.put(changedHashed, hashedStack);
                            LogUtils.debug("Changed! changed: {},\n original: {},\n changedHash: {},\n originalHash: {}", changedHashed, hashedStack, changedHashed.hashCode(), hashedStack.hashCode());
                        } else {
                            PacketItemModifier.callModify(wrapper.getItemStack(), event.player(), PacketItemModifier.Context.SET_CURSOR);
                        }

                        wrapper.markDirty();
                    } else if (event.type() == ClientboundPacketTypes.SET_EQUIPMENT) {
                        ClientboundSetEquipmentWrapper wrapper = new ClientboundSetEquipmentWrapper(event);
                        for (Pair<EquipmentSlot, WrappedItemStack> item : wrapper.items()) {
                            PacketItemModifier.callModify(item.second(), event.player(), PacketItemModifier.Context.EQUIPMENT);
                        }
                        wrapper.markDirty();
                    } else if (event.type() == ClientboundPacketTypes.MERCHANT_OFFERS) {
                        ClientboundMerchantOffersWrapper wrapper = new ClientboundMerchantOffersWrapper(event);
                        for (MerchantOffer offer : wrapper.merchantOffers()) {
                            PacketItemModifier.callModify(offer.getItem1(), event.player(), PacketItemModifier.Context.MERCHANT_OFFER);
                            offer.getItem2().ifPresent(cost -> {
                                PacketItemModifier.callModify(cost, event.player(), PacketItemModifier.Context.MERCHANT_OFFER);

                            });
                            PacketItemModifier.callModify(offer.getOutput(), event.player(), PacketItemModifier.Context.MERCHANT_OFFER);
                        }
                    } else if (event.type() == ClientboundPacketTypes.SET_ENTITY_DATA) {
                        ClientboundEntityMetadataWrapper wrapper = new ClientboundEntityMetadataWrapper(event);
                        for (Metadata.DataItem<?> item : wrapper.items()) {
                            if (item.getValue() instanceof WrappedItemStack stack) {
                                PacketItemModifier.callModify(stack, event.player(), PacketItemModifier.Context.DROPPED_ITEM);
                            }
                        }
                        wrapper.markDirty();
                    }
                }

                @Override
                public void onPacketReceive(PacketEvent event) {
                    if (event.type() == ServerboundPacketTypes.SET_CREATIVE_MODE_SLOT) {
                        ServerboundSetCreativeModeSlotWrapper wrapper = new ServerboundSetCreativeModeSlotWrapper(event);
                        PacketItemModifier.restore(wrapper.stack());
                    } else if (event.type() == ServerboundPacketTypes.CONTAINER_CLICK && Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_21_4)) {
                        LogUtils.debug("Container click!");
                        ServerboundContainerClickWrapper wrapper = new ServerboundContainerClickWrapper(event);
                        LogUtils.info("Hashed stack: {}, hashCode: {}", wrapper.getCarriedItem(), wrapper.getCarriedItem().hashCode());
                        HashedStack stack = stacks.remove(wrapper.getCarriedItem());
                        if (stack != null) {
                            LogUtils.debug("Stack not null!");
                            wrapper.setCarriedItem(stack);
                        }
                    }
                }
            });
        }

        if (!listeners.contains(listener)) {
            listeners.add(listener);
            listening = true;
        }
    }

    public static void unregister(PacketItemModifierListener listener) {
        listeners.remove(listener);
        if (listeners.isEmpty()) {
            listening = false;
        }
    }

    public static boolean isListening() {
        return listening;
    }

    public static void callModify(WrappedItemStack itemStack, Player player, Context context) {
        for (PacketItemModifierListener listener : listeners) {
            listener.modifyItemStack(player, itemStack, context);
        }
    }

    public static void restore(WrappedItemStack itemStack) {
        for (PacketItemModifierListener listener : listeners) {
            listener.restore(itemStack);
        }
    }

    public enum Context {
        SET_SLOT,
        SET_CURSOR,
        SET_CONTENTS,
        EQUIPMENT,
        DROPPED_ITEM,
        MERCHANT_OFFER
    }
}
