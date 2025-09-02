package min.taskflow.common.response;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public record ApiPageResponse<T>(int page,
                                 int size,
                                 int totalPages,
                                 long totalElements,
                                 List<T> data) {

    public static <T> ApiPageResponse<T> from(Page<T> pagedData) {
        return new ApiPageResponse<>(
                pagedData.getNumber(),
                pagedData.getSize(),
                pagedData.getTotalPages(),
                pagedData.getTotalElements(),
                pagedData.getContent()
        );
    }

    /**
     * 성공적인 요청에 대한 페이징 응답을 반환하는 메서드
     * 주어진 데이터를 포함하여 HTTP 200 OK 상태 코드와 함께 응답을 반환
     *
     * @param pagedData 요청 성공 시 반환할 페이징 데이터
     * @return HTTP 200 OK 응답과 함께 ApiResponse<ApiPageResponse<T>>
     */
    public static <T> ResponseEntity<ApiResponse<ApiPageResponse<T>>> success(Page<T> pagedData, String message) {
        return ResponseEntity.ok(
                ApiResponse.<ApiPageResponse<T>>builder()
                        .success(true)
                        .message(message)
                        .data(ApiPageResponse.from(pagedData))
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
