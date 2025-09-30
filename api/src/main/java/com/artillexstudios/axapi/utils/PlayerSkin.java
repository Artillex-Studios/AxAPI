package com.artillexstudios.axapi.utils;

import java.util.Optional;

public record PlayerSkin(ClientAsset.Texture body, ClientAsset.Texture cape, ClientAsset.Texture elytra,
                         PlayerModelType modelType, boolean secure) {


    public record Patch(Optional<ClientAsset.ResourceTexture> body, Optional<ClientAsset.ResourceTexture> cape,
                        Optional<ClientAsset.ResourceTexture> elytra, Optional<PlayerModelType> model) {

    }
}
