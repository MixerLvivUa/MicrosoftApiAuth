package com.microsoft.auth;

import com.microsoft.aad.adal4j.AdalAuthorizationGrant;
import com.microsoft.aad.adal4j.AuthenticationCallback;
import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.ResourceOwnerPasswordCredentialsGrant;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientAuthenticationMethod;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.id.ClientID;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


public class AuthContext {
    private AuthenticationContext authenticationContext;

    public AuthContext(String authority, boolean validateAuthority, ExecutorService service) throws MalformedURLException {
        authenticationContext = new AuthenticationContext(authority, false, service);

    }

    public Future<AuthenticationResult> acquireToken(String resource, String clientId, String clientSecret, String username, String password, AuthenticationCallback callback) {
        Object result = null;
        try {
            Map<String, String> params = new HashMap<>();
            params.put("resource", resource);
            params.put("client_secret", clientSecret);
            Class<?> clazz = Class.forName("com.microsoft.aad.adal4j.AdalOAuthAuthorizationGrant");
            Constructor<?> constructor = clazz.getDeclaredConstructor(AuthorizationGrant.class, Map.class);
            constructor.setAccessible(true);

            Method method = AuthenticationContext.class.getDeclaredMethod("acquireToken", AdalAuthorizationGrant.class, ClientAuthentication.class, AuthenticationCallback.class);
            method.setAccessible(true);
            result = method.invoke(authenticationContext,
                    constructor.newInstance(new ResourceOwnerPasswordCredentialsGrant(username, new Secret(password)), params),
                    new ClientAuthenticationPost(ClientAuthenticationMethod.NONE, new ClientID(clientId)),
                    callback);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }
        return (Future<AuthenticationResult>) result;
    }
}
