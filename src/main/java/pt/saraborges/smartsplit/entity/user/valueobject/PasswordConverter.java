package pt.saraborges.smartsplit.entity.user.valueobject;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PasswordConverter implements AttributeConverter<Password, String> {

    @Override
    public String convertToDatabaseColumn(Password password) {
        return password == null ? null : password.getHash();
    }

    @Override
    public Password convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Password.fromHash(dbData);
    }
}
