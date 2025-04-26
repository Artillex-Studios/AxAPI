package com.artillexstudios.axapi.config.reader;

import com.artillexstudios.axapi.config.annotation.Comment;
import it.unimi.dsi.fastutil.Pair;

import java.io.InputStream;
import java.util.Map;

public interface Handler {

    Pair<Map<String, Object>, Map<String, Comment>> read(InputStream stream, Object instance);

    String write(Map<String, Object> contents, Map<String, Comment> comments);
}
