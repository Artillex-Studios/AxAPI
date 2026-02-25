package com.artillexstudios.axapi.metrics;

import com.artillexstudios.axapi.config.annotation.Comment;
import com.artillexstudios.axapi.config.annotation.ConfigurationPart;

import java.util.UUID;

public final class MetricsConfig implements ConfigurationPart {
    @Comment("""
            If sending anonymous data should be enabled.
            Having this enabled helps us gain insightful data
            such as the server version, the java version you are using,
            and how popular our plugins are.
            
            Want to know more? Check out our metrics at: https://metrics.artillex-studios.com/
            
            We'd appreciate if you kept this enabled, as this has no impact on your performance.
            """)
    public static boolean enabled = true;
    public static UUID serverUuid = UUID.randomUUID();
}
