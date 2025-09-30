package com.artillexstudios.axapi.utils.featureflags.type;

import com.artillexstudios.axapi.utils.featureflags.FeatureFlag;

import java.util.ArrayList;
import java.util.List;

public final class ListFlag<Z> extends FeatureFlag<List<Z>> {
    private final FeatureFlag<Z> flag;

    public ListFlag(FeatureFlag<Z> flag, List<Z> def) {
        super(def);
        this.flag = flag;
    }

    public boolean contains(Z element) {
        List<Z> list = this.get();
        return list != null && list.contains(element);
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
