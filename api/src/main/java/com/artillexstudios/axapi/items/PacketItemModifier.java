package com.artillexstudios.axapi.items;

import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketEvents;
import com.artillexstudios.axapi.packet.PacketListener;
import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.utils.MerchantOffer;
import com.artillexstudios.axapi.packet.wrapper.ClientboundContainerSetContentWrapper;
import com.artillexstudios.axapi.packet.wrapper.ClientboundContainerSetSlotWrapper;
import com.artillexstudios.axapi.packet.wrapper.ClientboundEntityMetadataWrapper;
import com.artillexstudios.axapi.packet.wrapper.ClientboundMerchantOffersWrapper;
import com.artillexstudios.axapi.packet.wrapper.ClientboundSetEquipmentWrapper;
import com.artillexstudios.axapi.packetentity.meta.Metadata;
import com.artillexstudios.axapi.utils.EquipmentSlot;
import com.artillexstudios.axapi.utils.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.bukkit.entity.Player;

public class PacketItemModifier {
    private static boolean listening = false;
    private static final ObjectArrayList<PacketItemModifierListener> listeners = new ObjectArrayList<>();

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
                    } else if (event.type() == ClientboundPacketTypes.SET_EQUIPMENT) {
                        ClientboundSetEquipmentWrapper wrapper = new ClientboundSetEquipmentWrapper(event);
                        for (Pair<EquipmentSlot, WrappedItemStack> item : wrapper.items()) {
                            PacketItemModifier.callModify(item.second(), event.player(), PacketItemModifier.Context.EQUIPMENT);
                        }
                    } else if (event.type() == ClientboundPacketTypes.MERCHANT_OFFERS) {
                        ClientboundMerchantOffersWrapper wrapper = new ClientboundMerchantOffersWrapper(event);
                        for (MerchantOffer offer : wrapper.merchantOffers()) {
                            PacketItemModifier.callModify(offer.item1(), event.player(), PacketItemModifier.Context.MERCHANT_OFFER);
                            offer.item2().ifPresent(cost -> {
                                PacketItemModifier.callModify(cost, event.player(), PacketItemModifier.Context.MERCHANT_OFFER);

                            });
                            PacketItemModifier.callModify(offer.output(), event.player(), PacketItemModifier.Context.MERCHANT_OFFER);
                        }
                    } else if (event.type() == ClientboundPacketTypes.SET_ENTITY_DATA) {
                        ClientboundEntityMetadataWrapper wrapper = new ClientboundEntityMetadataWrapper(event);
                        for (Metadata.DataItem<?> item : wrapper.items()) {
                            if (item.getValue() instanceof WrappedItemStack stack) {
                                PacketItemModifier.callModify(stack, event.player(), PacketItemModifier.Context.DROPPED_ITEM);
                            }
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
        SET_CONTENTS,
        EQUIPMENT,
        DROPPED_ITEM,
        MERCHANT_OFFER
    }
}
