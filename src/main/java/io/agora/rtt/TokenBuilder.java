package io.agora.rtt;
import io.agora.media.RtcTokenBuilder;
import io.agora.media.RtcTokenBuilder.Role;

public class TokenBuilder {

    public static String getToken(String appId, String appCertificate , String channelName, int uid,  int expirationTimeInSeconds ) {
        // Generates a channel token for the given credentials using Agora RtcTokenBuilder class
        RtcTokenBuilder token = new RtcTokenBuilder();
        int timestamp = (int)(System.currentTimeMillis() / 1000 + expirationTimeInSeconds);
        
        String result = token.buildTokenWithUid(appId, appCertificate,  
       		 channelName, uid, Role.Role_Publisher, timestamp);
        return result;
    }
}
