package min.taskflow.search.service;

import lombok.RequiredArgsConstructor;
import min.taskflow.search.dto.SearchResponse;
import min.taskflow.task.service.queryService.ExternalQueryTaskService;
import min.taskflow.team.service.TeamService;
import min.taskflow.user.service.queryService.ExternalQueryUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ExternalQuerySearchService {

    private final ExternalQueryTaskService externalQueryTaskService;
    private final ExternalQueryUserService externalQueryUserService;
    private final TeamService teamService;

    @Transactional(readOnly = true)
    public SearchResponse searchAll(String query) {
        return new SearchResponse(
                externalQueryTaskService.searchTasksByQuery(query),
                externalQueryUserService.searchUsersByQuery(query),
                teamService.searchTeamsByQuery(query)
        );
    }
}

