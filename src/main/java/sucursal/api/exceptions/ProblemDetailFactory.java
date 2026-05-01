package sucursal.api.exceptions;

import java.net.URI;
import java.time.Instant;
import java.util.Map;

import org.springframework.http.ProblemDetail;

public final class ProblemDetailFactory {
    private static final String BASE_TYPE = "https://example.com/problems/";

    private ProblemDetailFactory() {
    }

    public static ProblemDetail from(ErrorCode error, String detail, String requestURI) {
        return from(error, detail, requestURI, Map.of());
    }

    public static ProblemDetail from(ErrorCode error, String detail, String requestURI, Map<String, Object> extra) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(error.getHttpStatus(), detail);
        problemDetail.setType(URI.create(BASE_TYPE + error.getCode()));
        problemDetail.setTitle(error.getMessage());
        problemDetail.setInstance(URI.create(requestURI));
        problemDetail.setProperty("timestamp", Instant.now().toString());
        problemDetail.setProperty("code", error.getCode());

        extra.forEach(problemDetail::setProperty);

        return problemDetail;
    }
}