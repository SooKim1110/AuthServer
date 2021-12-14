package sookim.authServer.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class RoleAttributeConverter implements AttributeConverter<String, Integer> {
    @Override
    public Integer convertToDatabaseColumn(String attribute) {
        if ("ROLE_USER".equals(attribute)) return 1;
        else if ("ROLE_ADMIN".equals(attribute)) return 2;
        return null;
    }

    @Override
    public String convertToEntityAttribute(Integer dbData) {
        if (1 == dbData) return "ROLE_USER";
        else if (2 == dbData) return "ROLE_ADMIN";
        return null;
    }
}
