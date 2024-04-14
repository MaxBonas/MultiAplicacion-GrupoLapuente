package MultiAplicacion.ENUMs;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import MultiAplicacion.ENUMs.Cargo;

@Component
public class StringToEnumConverter implements Converter<String, Cargo> {
    @Override
    public Cargo convert(String source) {
        String upperCaseSource = source.replace(" ", "_").toUpperCase();
        return Cargo.valueOf(upperCaseSource);
    }
}
