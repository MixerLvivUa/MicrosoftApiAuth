package com.microsoft.auth;

import com.nimbusds.oauth2.sdk.SerializeException;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientAuthenticationMethod;
import com.nimbusds.oauth2.sdk.http.CommonContentTypes;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.util.URLUtils;

import javax.mail.internet.ContentType;
import java.util.HashMap;
import java.util.Map;

class ClientAuthenticationPost extends ClientAuthentication {
    ClientAuthenticationPost(ClientAuthenticationMethod method, ClientID clientID) {
        super(method, clientID);
    }

    Map<String, String> toParameters() {
        Map<String, String> params = new HashMap<>();
        params.put("client_id", this.getClientID().getValue());
        return params;
    }

    public void applyTo(HTTPRequest httpRequest) throws SerializeException {
        if (httpRequest.getMethod() != HTTPRequest.Method.POST) {
            throw new SerializeException("The HTTP request method must be POST");
        } else {
            ContentType ct = httpRequest.getContentType();
            if (ct == null) {
                throw new SerializeException("Missing HTTP Content-Type header");
            } else if (!ct.match(CommonContentTypes.APPLICATION_URLENCODED)) {
                throw new SerializeException("The HTTP Content-Type header must be " + CommonContentTypes.APPLICATION_URLENCODED);
            } else {
                Map<String, String> params = httpRequest.getQueryParameters();
                params.putAll(this.toParameters());
                String queryString = URLUtils.serializeParameters(params);
                httpRequest.setQuery(queryString);
            }
        }
    }
}
