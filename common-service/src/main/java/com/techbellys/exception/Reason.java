package com.techbellys.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reason {

    private String code;
    private String message;

    private List<Reason> nested = new ArrayList<>();

    public static Reason from(final ApplicationException e) {
        final Reason reason = toReason(e);
        e.getNestedExceptions().stream().forEach(n -> reason.getNested().add(toReason(n)));
        return reason;
    }

    private static Reason toReason(ApplicationException exp) {
        Reason reason = new Reason();
        reason.setCode(exp.getApplicationCode().code());
        reason.setMessage(Objects.isNull(exp.getDescription()) ? exp.getApplicationCode().message() :
                exp.getDescription());
        return reason;
    }
}
