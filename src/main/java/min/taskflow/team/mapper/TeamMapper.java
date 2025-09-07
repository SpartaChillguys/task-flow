package min.taskflow.team.mapper;

import min.taskflow.team.dto.*;
import min.taskflow.team.entity.Team;
import min.taskflow.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TeamMapper {

    public Team toEntity(TeamCreateRequest request) {

        return Team.builder()
                .name(request.name())
                .description(request.description())
                .build();
    }

    public void updateEntity(Team team, TeamUpdateRequest request) {

        team.updateTeam(request.name(), request.description());
    }

    public TeamResponse toTeamResponse(Team team) {

        return new TeamResponse(
                team.getTeamId(),
                team.getName(),
                team.getDescription(),
                team.getCreatedAt(),
                team.getMembers().stream()
                        .map(this::toMemberResponse)
                        .toList()
        );
    }

    public TeamSearchResponse toTeamSearchResponse(Team team) {

        return TeamSearchResponse.builder()
                .teamId(team.getTeamId())
                .name(team.getName())
                .description(team.getDescription())
                .build();
    }

    public MemberResponse toMemberResponse(User user) {

        return new MemberResponse(
                user.getUserId(),
                user.getUserName(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.getCreatedAt()
        );
    }


    public List<MemberResponse> toMemberResponseList(List<User> users) {

        return users.stream()
                .map(this::toMemberResponse)
                .toList();
    }

    public List<Long> toMemberIdList(List<User> members) {

        return members.stream()
                .map(User::getUserId)
                .toList();
    }
}

