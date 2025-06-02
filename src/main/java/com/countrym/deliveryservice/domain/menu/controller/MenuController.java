package com.countrym.deliveryservice.domain.menu.controller;

import com.countrym.deliveryservice.common.config.security.UserInfo;
import com.countrym.deliveryservice.common.response.SuccessResponse;
import com.countrym.deliveryservice.domain.menu.dto.request.ModifyMenuRequestDto;
import com.countrym.deliveryservice.domain.menu.dto.request.RegisterMenuRequestDto;
import com.countrym.deliveryservice.domain.menu.dto.response.GetMenuResponseDto;
import com.countrym.deliveryservice.domain.menu.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "메뉴 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class MenuController {
    private final MenuService menuService;

    @PostMapping("/{storeId}/menu")
    @Operation(summary = "메뉴 등록 API")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    schema = @Schema(implementation = RegisterMenuRequestDto.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "정상 처리되었습니다."
    )
    public ResponseEntity<SuccessResponse<GetMenuResponseDto>> registerMenu(
            @PathVariable("storeId") long storeId,
            @AuthenticationPrincipal UserInfo userInfo,
            @Valid @RequestBody RegisterMenuRequestDto registerMenuRequestDto
    ) {
        return ResponseEntity.ok(SuccessResponse.of(menuService.registerMenu(storeId, userInfo, registerMenuRequestDto)));
    }

    @PatchMapping("/{storeId}/menu/{menuId}")
    @Operation(summary = "메뉴 수정 API")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    schema = @Schema(implementation = ModifyMenuRequestDto.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "정상 처리되었습니다."
    )
    public ResponseEntity<SuccessResponse<GetMenuResponseDto>> modifyMenu(
            @PathVariable("storeId") long storeId,
            @PathVariable("menuId") long menuId,
            @AuthenticationPrincipal UserInfo userInfo,
            @Valid @RequestBody ModifyMenuRequestDto modifyMenuRequestDto
    ) {
        return ResponseEntity.ok(SuccessResponse.of(menuService.modifyMenu(storeId, menuId, userInfo, modifyMenuRequestDto)));
    }

    @DeleteMapping("/{storeId}/menu/{menuId}")
    @Operation(summary = "메뉴 삭제 API")
    @ApiResponse(
            responseCode = "200",
            description = "정상 처리되었습니다."
    )
    public ResponseEntity<SuccessResponse<Void>> deleteMenu(
            @PathVariable("storeId") long storeId,
            @PathVariable("menuId") long menuId,
            @AuthenticationPrincipal UserInfo userInfo
    ) {
        menuService.deleteMenu(storeId, menuId, userInfo);
        return ResponseEntity.ok(SuccessResponse.of(null));
    }
}
