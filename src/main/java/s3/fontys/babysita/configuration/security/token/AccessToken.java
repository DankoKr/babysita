package s3.fontys.babysita.configuration.security.token;

import java.util.Set;

public interface AccessToken {
    String getSubject();

    Integer getUserId();

    String getRole();
}
