package min.taskflow.team.mapper;

import min.taskflow.team.dto.MemberResponse;
import min.taskflow.team.dto.TeamCreateRequest;
import min.taskflow.team.dto.TeamResponse;
import min.taskflow.team.dto.TeamUpdateRequest;
import min.taskflow.team.entity.Team;

import java.util.Collections;

public class TeamMapper {

    public static Team toEntity(TeamCreateRequest request) {

        return Team.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public static void updateEntity(Team team, TeamUpdateRequest request) {

        team.updateTeam(request.getName(), request.getDescription());
    }

    public static TeamResponse toResponse(Team team) {

        return TeamResponse.builder()
                .id(team.getTeamId())
                .name(team.getName())
                .description(team.getDescription())
                .createdAt(team.getCreatedAt())
                .members(team.getMembers() == null ? Collections.emptyList() :
                        team.getMembers().stream()
                                .map(user -> new MemberResponse(
                                        user.getUserId(),
                                        user.getUserName(),
                                        user.getName(),
                                        user.getEmail(),
                                        user.getRole().name(),
                                        user.getCreatedAt()
                                ))
                                .toList())
                .build();
    }
}
