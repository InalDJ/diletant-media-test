package com.diletant.diletantmediatest.util;

import javax.persistence.AttributeConverter;
import java.util.*;

public class StringListConverter implements AttributeConverter<Set<String>, String> {
    @Override
    public String convertToDatabaseColumn(Set<String> list) {
        return String.join(",", list);
    }

    @Override
    public Set<String> convertToEntityAttribute(String joined) {
        return new HashSet<>(Arrays.asList(joined.split(",")));
    }
}
