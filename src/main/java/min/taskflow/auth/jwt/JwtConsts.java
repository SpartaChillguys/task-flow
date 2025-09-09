package min.taskflow.auth.jwt;

public class JwtConsts {

    public static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15;
    public static final long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 7;
    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String BEARER_PREFIX = "Bearer ";
}
