package pt.saraborges.smartsplit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pt.saraborges.smartsplit.dto.request.group.GroupMembersDto;
import pt.saraborges.smartsplit.dto.request.group.NewGroupDto;
import pt.saraborges.smartsplit.dto.response.group.GroupResponseDto;
import pt.saraborges.smartsplit.service.GroupService;
import java.util.List;

@Controller
@AllArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @Operation(summary = "Gets all registered groups.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping("/groups")
    @ResponseBody
    public ResponseEntity<List<GroupResponseDto>> getAllGroups(){
        return ResponseEntity.ok(groupService.getAllGroups());
    }

    @Operation(summary = "Creates new group.")
    @ApiResponses({
           @ApiResponse(responseCode = "201", description = "Created."),
            @ApiResponse(responseCode = "400", description = "Validation error."),
            @ApiResponse(responseCode = "409", description = "Duplicated group."),
            @ApiResponse(responseCode = "500", description = "Internal error.")
    })
    @PostMapping("/groups")
    @ResponseBody
    public ResponseEntity<GroupResponseDto> createGroup(@RequestBody NewGroupDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.createGroup(dto));
    }

    @Operation(summary = "Updates group's member list.")
    @ApiResponses ({
            @ApiResponse(responseCode = "200", description = "Updated."),
            @ApiResponse(responseCode = "400", description = "Validation error."),
            @ApiResponse(responseCode = "409", description = "Duplicated group."),
            @ApiResponse(responseCode = "500", description = "Internal error.")
    })
    @PutMapping("/groups/members")
    @ResponseBody
    public ResponseEntity<GroupResponseDto> addMembers(@RequestBody GroupMembersDto dto){
        return ResponseEntity.ok(groupService.addUsersToGroup(dto));
    }

    @Operation(summary = "Updates group's member list.")
    @ApiResponses ({
            @ApiResponse(responseCode = "200", description = "Updated."),
            @ApiResponse(responseCode = "400", description = "Validation error."),
            @ApiResponse(responseCode = "409", description = "Duplicated group."),
            @ApiResponse(responseCode = "500", description = "Internal error.")
    })
    @DeleteMapping("/groups/members")
    @ResponseBody
    public ResponseEntity<GroupResponseDto> removeMembers(@RequestBody GroupMembersDto dto){
        return ResponseEntity.ok(groupService.removeGroupMembers(dto));
    }
}
