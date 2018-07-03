import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.auth.AuthContext;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Tests {
    @Test
    public void getOneToken() throws MalformedURLException {
        String authURL = "https://login.microsoftonline.com/common/oauth2/v2.0/authorize";
        String resource = "https://graph.microsoft.com";
        ExecutorService service = Executors.newFixedThreadPool(1);

        AuthContext context = new AuthContext(authURL, false, service);
        AuthenticationResult result = context.acquireToken(
                resource,
                "<client_id>",
                "<client_secret>",
                "<user_login>",
                "<user_password>",
                null
        );

        System.out.println("Access Token - " + result.getAccessToken());
        System.out.println("Refresh Token - " + result.getRefreshToken());
        System.out.println("ID Token - " + result.getIdToken());
        service.shutdown();
    }

    @Test
    public void getTokenHandler() throws MalformedURLException, ExecutionException, InterruptedException {
        AuthContext context = new AuthContext(
                "<client_id>",
                "<client_secret>",
                "<user_login>",
                "<user_password>",
                null);
        AuthenticationResult result = context.acquireToken();
        System.out.println("Access Token - " + result.getAccessToken());
        System.out.println("Refresh Token - " + result.getRefreshToken());
        System.out.println("ID Token - " + result.getIdToken());

        result = context.refreshToken();
        System.out.println("Access Token - " + result.getAccessToken());
        System.out.println("Refresh Token - " + result.getRefreshToken());
        System.out.println("ID Token - " + result.getIdToken());
        context.shutdownService();
    }
}
