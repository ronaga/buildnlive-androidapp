package buildnlive.com.buildlive.Server;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class AuthenticationInterceptor implements Interceptor {

    private String authToken;
    private String uuid;

    AuthenticationInterceptor(String token,String uuid) {
        this.authToken = token;
        this.uuid = uuid;
    }

    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder()
                .header("Bearer", authToken)
                .header("uuid",uuid);

        Request request = builder.build();
        return chain.proceed(request);
    }
}