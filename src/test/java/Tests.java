import com.microsoft.auth.AuthContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Tests {
    @Test
    public void text() throws MalformedURLException, ExecutionException, InterruptedException {
        String authURL = "https://login.microsoftonline.com/common/oauth2/v2.0/authorize";
        String resource = "https://graph.microsoft.com";
        ExecutorService service = Executors.newFixedThreadPool(1);

        AuthContext context = new AuthContext(authURL, false, service);
        Future<AuthenticationResult> future = context.acquireToken(
                resource,
                "<client_id>",
                "<client_secret>",
                "<user_login>",
                "<user_password>",
                null
        );

        AuthenticationResult result = future.get();
        System.out.println("Access Token - " + result.getAccessToken());
        System.out.println("Refresh Token - " + result.getRefreshToken());
        System.out.println("ID Token - " + result.getIdToken());
        service.shutdown();
    }
}
