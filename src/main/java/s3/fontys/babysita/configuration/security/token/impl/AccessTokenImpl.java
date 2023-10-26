package s3.fontys.babysita.configuration.security.token.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import s3.fontys.babysita.configuration.security.token.AccessToken;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
public class AccessTokenImpl implements AccessToken {
    private final String subject;
    private final Integer userId;
    private final String role;
}
