package com.artillexstudios.axapi.utils.featureflags;

import java.util.ArrayList;
import java.util.List;

public final class ListFlag<Z> extends FeatureFlag<List<Z>> {
    private final FeatureFlag<Z> flag;

    public ListFlag(String property, FeatureFlag<Z> flag, List<Z> def) {
        super(property, def);
        this.flag = flag;
    }

    @Override
    public List<Z> transform(String string) {
        if (string == null) {
            return null;
        }

        List<Z> list = new ArrayList<>();
        for (String s : string.split(",")) {
            list.add(this.flag.transform(s));
        }

        return list;
    }
}
