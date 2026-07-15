package pt.saraborges.smartsplit.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pt.saraborges.smartsplit.user.dto.request.RegisterUserDto;
import pt.saraborges.smartsplit.user.service.UserService;

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
    public void getUsers(){

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
    public ResponseEntity registerUser(@RequestBody RegisterUserDto dto){
        userService.registerUser(dto);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
