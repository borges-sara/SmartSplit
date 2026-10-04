package pt.saraborges.smartsplit.mapper.user;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import pt.saraborges.smartsplit.entity.user.valueobject.Password;

@Converter(autoApply = true)
public class PasswordMapper implements AttributeConverter<Password, String> {

    @Override
    public String convertToDatabaseColumn(Password password) {
        return password == null ? null : password.getHash();
    }

    @Override
    public Password convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Password.fromHash(dbData);
    }
}
