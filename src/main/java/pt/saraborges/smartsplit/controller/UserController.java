package pt.saraborges.smartsplit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pt.saraborges.smartsplit.dto.request.RegisterUserDto;
import pt.saraborges.smartsplit.dto.response.CreatedUserResponseDto;
import pt.saraborges.smartsplit.dto.response.GetUserResponseDto;
import pt.saraborges.smartsplit.service.UserService;

import java.util.List;

@Controller
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get all users", description = "Returns all existing users.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping("/users")
    @ResponseBody
    public ResponseEntity<List<GetUserResponseDto>> getUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Register new user", description = "Creates and registers a new user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User created."),
            @ApiResponse(responseCode = "400", description = "Validation error."),
            @ApiResponse(responseCode = "409", description = "Duplicated user."),
            @ApiResponse(responseCode = "500", description = "Internal error.")
    })
    @PostMapping("/users")
    @ResponseBody
    public ResponseEntity<CreatedUserResponseDto> registerUser(@RequestBody RegisterUserDto dto){
        var createdUser = userService.registerUser(dto);
        return ResponseEntity.ok(createdUser);
    }
}

