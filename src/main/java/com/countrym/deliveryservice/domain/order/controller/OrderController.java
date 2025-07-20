package com.countrym.deliveryservice.domain.order.controller;

import com.countrym.deliveryservice.common.config.security.UserInfo;
import com.countrym.deliveryservice.common.response.SuccessResponse;
import com.countrym.deliveryservice.domain.order.dto.request.ModifyOrderStatusRequestDto;
import com.countrym.deliveryservice.domain.order.dto.request.OrderRequestDto;
import com.countrym.deliveryservice.domain.order.dto.response.OrderListResponseDto;
import com.countrym.deliveryservice.domain.order.dto.response.OrderResponseDto;
import com.countrym.deliveryservice.domain.order.dto.response.StoreOrderListResponseDto;
import com.countrym.deliveryservice.domain.order.service.OrderService;
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

@Tag(name = "주문 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<SuccessResponse<Void>> order(
            @AuthenticationPrincipal UserInfo userInfo,
            @Valid @RequestBody OrderRequestDto orderRequestDto
    ) {
        orderService.order(userInfo, orderRequestDto);
        return ResponseEntity.ok(SuccessResponse.of());
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<SuccessResponse<OrderResponseDto>> getOrder(
            @AuthenticationPrincipal UserInfo userInfo,
            @PathVariable("orderId") long orderId
    ) {
        OrderResponseDto orderResponseDto = orderService.getOrder(userInfo, orderId);
        return ResponseEntity.ok(SuccessResponse.of(orderResponseDto));
    }

    @GetMapping("/orders")
    public ResponseEntity<SuccessResponse<List<OrderListResponseDto>>> getOrderList(
            @AuthenticationPrincipal UserInfo userInfo,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 20, sort = "ordered_at", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        List<OrderListResponseDto> orderListResponseDto = orderService.getOrderListByPaging(userInfo, status, pageable);
        return ResponseEntity.ok(SuccessResponse.of(orderListResponseDto));
    }

    @GetMapping("/stores/{storeId}/orders")
    public ResponseEntity<SuccessResponse<List<StoreOrderListResponseDto>>> getOrderListWithStoreId(
            @AuthenticationPrincipal UserInfo userInfo,
            @PathVariable("storeId") long storeId,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 20, sort = "ordered_at", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        List<StoreOrderListResponseDto> storeOrderResponseDtoList = orderService.getOrderListWithStore(userInfo, storeId, status, pageable);
        return ResponseEntity.ok(SuccessResponse.of(storeOrderResponseDtoList));
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<SuccessResponse<Void>> cancelOrder(
            @AuthenticationPrincipal UserInfo userInfo,
            @PathVariable("orderId") long orderId
    ) {
        orderService.cancelOrder(userInfo, orderId);
        return ResponseEntity.ok(SuccessResponse.of());
    }

    @PatchMapping("/orders/{orderId}")
    public ResponseEntity<SuccessResponse<Void>> modifyOrderStatus(
            @AuthenticationPrincipal UserInfo userInfo,
            @PathVariable("orderId") long orderId,
            @Valid @RequestBody ModifyOrderStatusRequestDto modifyOrderStatusRequestDto
    ) {
        orderService.modifyOrderStatus(userInfo, orderId, modifyOrderStatusRequestDto);
        return ResponseEntity.ok(SuccessResponse.of());
    }
}
