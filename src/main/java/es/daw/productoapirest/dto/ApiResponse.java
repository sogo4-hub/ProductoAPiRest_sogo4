package es.daw.productoapirest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class ApiResponse {
    private boolean success;
    private String message;
    private Map<String, Object> details= new HashMap<>();

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public void addDetail(String key, Object value) {
        details.put(key, value);
    }
}
