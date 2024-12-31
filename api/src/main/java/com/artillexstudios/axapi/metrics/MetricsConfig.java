package com.artillexstudios.axapi.metrics;

import com.artillexstudios.axapi.config.annotation.Comment;
import com.artillexstudios.axapi.config.annotation.ConfigurationPart;

import java.util.UUID;

public class MetricsConfig implements ConfigurationPart {
    @Comment("""
            If sending anonymous data should be enabled.
            Having this enabled helps us gain insightful data
            such as the server version, the java version you are using,
            and how popular our plugins are.
            
            We'd appreciate if you kept this enabled, as this has no impact on your performance.
            """)
    public static boolean enabled = true;
    public static UUID serverUuid = UUID.randomUUID();
}
