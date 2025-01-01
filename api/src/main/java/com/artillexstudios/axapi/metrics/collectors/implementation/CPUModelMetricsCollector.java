package com.artillexstudios.axapi.metrics.collectors.implementation;

import com.artillexstudios.axapi.metrics.collectors.MetricsCollector;
import com.artillexstudios.axapi.utils.LogUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.lang.reflect.Method;

public class CPUModelMetricsCollector implements MetricsCollector {
    private String cpuModel;

    public CPUModelMetricsCollector(Object systemInfo) {
        try {
            Method hardwareInfoGetter = Class.forName("oshi.SystemInfo").getDeclaredMethod("getHardware");
            Object hardwareInfo = hardwareInfoGetter.invoke(systemInfo);
            Method processorGetter = Class.forName("oshi.hardware.HardwareAbstractionLayer").getDeclaredMethod("getProcessor");
            Object centralProcessingUnit = processorGetter.invoke(hardwareInfo);
            Method processorIdentifierGetter = Class.forName("oshi.hardware.CentralProcessor").getDeclaredMethod("getProcessorIdentifier");
            Object processorIdentifier = processorIdentifierGetter.invoke(centralProcessingUnit);
            Method processorNameGetter = Class.forName("oshi.hardware.CentralProcessor$ProcessorIdentifier").getDeclaredMethod("getName");
            this.cpuModel = (String) processorNameGetter.invoke(processorIdentifier);
        } catch (Exception exception) {
            LogUtils.error("Failed to load CPU model!");
        }
    }

    @Override
    public void collect(JsonArray data) {
        if (this.cpuModel == null) {
            return;
        }

        JsonObject object = new JsonObject();
        object.addProperty("@type", "cpu-model");
        object.addProperty("cpu-model", this.cpuModel);
        data.add(object);
    }
}