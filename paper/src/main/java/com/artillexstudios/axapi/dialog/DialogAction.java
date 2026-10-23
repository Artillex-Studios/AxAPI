package com.artillexstudios.axapi.dialog;

public enum DialogAction {
    CLOSE("close"),
    NONE("none"),
    WAIT_FOR_RESPONSE("wait_for_response");

    private final String actionName;

    DialogAction(String actionName) {
        this.actionName = actionName;
    }

    public String getActionName() {
        return this.actionName;
    }
}
