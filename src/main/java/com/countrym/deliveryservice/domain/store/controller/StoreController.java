package com.countrym.deliveryservice.domain.store.controller;

import com.countrym.deliveryservice.common.config.security.UserInfo;
import com.countrym.deliveryservice.common.response.SuccessResponse;
import com.countrym.deliveryservice.domain.store.dto.request.ModifyStoreRequestDto;
import com.countrym.deliveryservice.domain.store.dto.request.RegisterStoreRequestDto;
import com.countrym.deliveryservice.domain.store.dto.response.GetStoreListResponseDto;
import com.countrym.deliveryservice.domain.store.dto.response.GetStoreResponseDto;
import com.countrym.deliveryservice.domain.store.dto.response.ModifyStoreResponseDto;
import com.countrym.deliveryservice.domain.store.dto.response.RegisterStoreResponseDto;
import com.countrym.deliveryservice.domain.store.service.StoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "가게 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {
    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<SuccessResponse<RegisterStoreResponseDto>> registerStore(
            @AuthenticationPrincipal UserInfo userInfo,
            @Valid @RequestBody RegisterStoreRequestDto registerStoreRequestDto
    ) {
        RegisterStoreResponseDto registerStoreResponseDto = storeService.registerStore(userInfo, registerStoreRequestDto);
        return ResponseEntity.ok(SuccessResponse.of(registerStoreResponseDto));
    }

    @GetMapping("{storeId}")
    public ResponseEntity<SuccessResponse<GetStoreResponseDto>> getStore(
            @PathVariable("storeId") long storeId
    ) {
        GetStoreResponseDto getStoreResponseDto = storeService.getStore(storeId);
        return ResponseEntity.ok(SuccessResponse.of(getStoreResponseDto));
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<GetStoreListResponseDto>>> getStoreList(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String name,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(SuccessResponse.of(storeService.getStoreList(type, name, pageable)));
    }

    @PatchMapping("/{storeId}")
    public ResponseEntity<SuccessResponse<ModifyStoreResponseDto>> modifyStore(
            @PathVariable("storeId") long storeId,
            @AuthenticationPrincipal UserInfo userInfo,
            @Valid @RequestBody ModifyStoreRequestDto modifyStoreRequestDto
    ) {
        ModifyStoreResponseDto modifyStoreResponseDto = storeService.modifyStore(storeId, userInfo, modifyStoreRequestDto);
        return ResponseEntity.ok(SuccessResponse.of(modifyStoreResponseDto));
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<SuccessResponse<Void>> closureStore(
            @PathVariable("storeId") long storeId,
            @AuthenticationPrincipal UserInfo userInfo
    ) {
        storeService.closureStore(storeId, userInfo);
        return ResponseEntity.ok(SuccessResponse.of());
    }
}
