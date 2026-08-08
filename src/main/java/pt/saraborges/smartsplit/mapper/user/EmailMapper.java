package pt.saraborges.smartsplit.mapper.user;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import pt.saraborges.smartsplit.entity.user.valueobject.Email;

@Converter(autoApply = true)
public class EmailMapper implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(Email email) {
        return email == null ? null : email.getEmail();
    }

    @Override
    public Email convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Email.fromExisting(dbData);
    }
}
