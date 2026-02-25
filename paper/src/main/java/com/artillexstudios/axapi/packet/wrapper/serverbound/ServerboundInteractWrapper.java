package com.artillexstudios.axapi.packet.wrapper.serverbound;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.ServerboundPacketTypes;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;
import com.artillexstudios.axapi.utils.Vector3f;

import java.util.function.Function;

public final class ServerboundInteractWrapper extends PacketWrapper {
    private int entityId;
    private ActionType type;
    private Action action;
    private boolean usingSecondaryAction;

    public ServerboundInteractWrapper(PacketEvent event) {
        super(event);
    }

    public int entityId() {
        return this.entityId;
    }

    public void entityId(int entityId) {
        this.entityId = entityId;
    }

    public ActionType type() {
        return this.type;
    }

    public void type(ActionType type) {
        this.type = type;
    }

    public Action action() {
        return this.action;
    }

    public void action(Action action) {
        this.action = action;
    }

    public boolean usingSecondaryAction() {
        return this.usingSecondaryAction;
    }

    public void usingSecondaryAction(boolean usingSecondaryAction) {
        this.usingSecondaryAction = usingSecondaryAction;
    }

    @Override
    public void write(FriendlyByteBuf out) {
        out.writeVarInt(this.entityId);
        out.writeEnum(this.type);
        this.action.write(out);
        out.writeBoolean(this.usingSecondaryAction);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.type = buf.readEnum(ActionType.class);
        this.action = this.type.mapper.apply(buf);
        this.usingSecondaryAction = buf.readBoolean();
    }

    @Override
    public PacketType packetType() {
        return ServerboundPacketTypes.INTERACT;
    }

    public enum ActionType {
        INTERACT(InteractionAction::new),
        ATTACK(buf -> new AttackAction()),
        INTERACT_AT(InteractionAtLocationAction::new);

        private Function<FriendlyByteBuf, Action> mapper;

        ActionType(Function<FriendlyByteBuf, Action> mapper) {
            this.mapper = mapper;
        }
    }

    public interface Action {

        void write(FriendlyByteBuf buf);
    }

    public static class InteractionAction implements Action {
        private final InteractionHand hand;

        public InteractionAction(FriendlyByteBuf buf) {
            this.hand = buf.readEnum(InteractionHand.class);
        }

        public InteractionHand hand() {
            return this.hand;
        }

        @Override
        public void write(FriendlyByteBuf buf) {
            buf.writeEnum(this.hand);
        }
    }

    public static class InteractionAtLocationAction implements Action {
        private final InteractionHand hand;
        private final Vector3f location;

        public InteractionAtLocationAction(FriendlyByteBuf buf) {
            this.location = buf.readVector3f();
            this.hand = buf.readEnum(InteractionHand.class);
        }

        public InteractionHand hand() {
            return this.hand;
        }

        public Vector3f location() {
            return this.location;
        }

        @Override
        public void write(FriendlyByteBuf buf) {
            buf.writeVector3f(this.location);
            buf.writeEnum(this.hand);
        }
    }

    public static class AttackAction implements Action {

        @Override
        public void write(FriendlyByteBuf buf) {

        }
    }

    public enum InteractionHand {
        MAIN_HAND,
        OFF_HAND;
    }
}
