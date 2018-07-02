import auth.AuthContext;
import com.microsoft.aad.adal4j.AuthenticationResult;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) {
        try {
            new Main().run();
        } catch (ExecutionException | InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }

    private void run() throws ExecutionException, InterruptedException, IOException {
        String authURL = "https://login.microsoftonline.com/common/oauth2/v2.0/authorize";
        String resource = "https://graph.microsoft.com";
        ExecutorService service = Executors.newFixedThreadPool(1);
        AuthenticationResult result = null;
        Future<AuthenticationResult> future = null;
//------------------------------------------------------------------------------------------------------------------------
        AuthContext contextt = new AuthContext(authURL, false, service);
        future = contextt.acquireToken(
                resource,
                "",
                "",
                "",
                "",
                null
        );
//------------------------------------------------------------------------------------------------------------------------
        result = future.get();
        System.out.println("Access Token - " + result.getAccessToken());
        System.out.println("Refresh Token - " + result.getRefreshToken());
        System.out.println("ID Token - " + result.getIdToken());
        service.shutdown();
    }

}
