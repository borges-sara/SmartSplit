package pt.saraborges.smartsplit.entity.user.valueobject;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EmailConverter implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(Email email) {
        return email == null ? null : email.getEmail();
    }

    @Override
    public Email convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Email.fromExisting(dbData);
    }
}
