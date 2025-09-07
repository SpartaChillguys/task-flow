package min.taskflow.search.controller;

import lombok.RequiredArgsConstructor;
import min.taskflow.common.response.ApiResponse;
import min.taskflow.search.dto.SearchResponse;
import min.taskflow.search.service.ExternalQuerySearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final ExternalQuerySearchService externalQuerySearchService;

    @GetMapping
    public ResponseEntity<ApiResponse<SearchResponse>> searchAll(@RequestParam("q") String query) {

        SearchResponse response = externalQuerySearchService.searchAll(query);

        return ApiResponse.success(response, "검색 완료");
    }
}

