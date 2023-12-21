package s3.fontys.babysita.configuration.security.token;


public interface AccessToken {
    String getSubject();

    Integer getUserId();

    String getRole();
}
