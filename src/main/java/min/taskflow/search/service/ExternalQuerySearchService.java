package min.taskflow.search.service;

import lombok.RequiredArgsConstructor;
import min.taskflow.search.dto.SearchResponse;
import min.taskflow.task.service.query.InternalQueryTaskService;
import min.taskflow.team.service.query.InternalQueryTeamService;
import min.taskflow.user.service.query.InternalQueryUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ExternalQuerySearchService {

    private final InternalQueryTaskService internalQueryTaskService;
    private final InternalQueryUserService internalQueryUserService;
    private final InternalQueryTeamService internalQueryTeamService;

    @Transactional(readOnly = true)
    public SearchResponse searchAll(String query) {
        return new SearchResponse(
                internalQueryTaskService.searchTasksByQuery(query),
                internalQueryUserService.searchUsersByQuery(query),
                internalQueryTeamService.searchTeamByQuery(query)
        );
    }
}

