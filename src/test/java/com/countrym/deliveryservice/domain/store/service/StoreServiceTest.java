package com.countrym.deliveryservice.domain.store.service;

import com.countrym.deliveryservice.common.config.security.UserInfo;
import com.countrym.deliveryservice.common.exception.DuplicateElementException;
import com.countrym.deliveryservice.common.exception.InvalidParameterException;
import com.countrym.deliveryservice.domain.store.dto.projection.StoreMenuListDto;
import com.countrym.deliveryservice.domain.store.dto.request.ModifyStoreRequestDto;
import com.countrym.deliveryservice.domain.store.dto.request.RegisterStoreRequestDto;
import com.countrym.deliveryservice.domain.store.dto.response.GetStoreResponseDto;
import com.countrym.deliveryservice.domain.store.dto.response.ModifyStoreResponseDto;
import com.countrym.deliveryservice.domain.store.dto.response.RegisterStoreResponseDto;
import com.countrym.deliveryservice.domain.store.entity.Store;
import com.countrym.deliveryservice.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.countrym.deliveryservice.domain.data.store.StoreMockData.*;
import static com.countrym.deliveryservice.domain.data.user.UserMockData.getOwnerInfo;
import static com.countrym.deliveryservice.domain.data.user.UserMockData.getUserInfo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {
    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreService storeService;

    @Nested
    @DisplayName("상점 등록")
    class registerStore {
        @Test
        @DisplayName("이미 존재하는 상점명일 경우 상점 등록에 실패")
        public void registerStore_duplicateStoreName_failure() {
            // given
            UserInfo userInfo = getOwnerInfo();
            RegisterStoreRequestDto registerStoreRequestDto = getRegisterStoreRequestDto();

            given(storeRepository.existsByName(anyString())).willReturn(true);

            // when, then
            assertThrows(DuplicateElementException.class,
                    () -> storeService.registerStore(userInfo, registerStoreRequestDto));
        }

        @Test
        @DisplayName("성공")
        public void registerStore_success() {
            // given
            UserInfo userInfo = getOwnerInfo();
            RegisterStoreRequestDto registerStoreRequestDto = getRegisterStoreRequestDto();

            Store store = getStore(userInfo, registerStoreRequestDto);

            given(storeRepository.existsByName(anyString())).willReturn(false);
            given(storeRepository.save(any(Store.class))).willReturn(store);

            // when
            RegisterStoreResponseDto registerStoreResponseDto = storeService.registerStore(userInfo, registerStoreRequestDto);

            assertEquals(registerStoreResponseDto.getStoreId(), store.getId());
            assertEquals(registerStoreResponseDto.getName(), registerStoreRequestDto.getName());
        }
    }

    @Nested
    @DisplayName("상점 조회")
    class getStore {
        @Test
        @DisplayName("상점 조회 시, 메뉴가 없을 경우 메뉴가 빈 리스트로 표시")
        public void getStore_menuListIsEmpty() {
            // given
            long storeId = 1L;
            StoreMenuListDto storeMenuListDto = getStoreMenuListDto(0);

            given(storeRepository.findByStoreIdWithMenuList(storeId)).willReturn(storeMenuListDto);

            // when
            GetStoreResponseDto getStoreResponseDto = storeService.getStore(storeId);

            // then
            assertNotNull(getStoreResponseDto);
            assertTrue(getStoreResponseDto.getMenuList().isEmpty());
        }

        @Test
        @DisplayName("성공")
        public void getStore_success() {
            // given
            long storeId = 1L;
            StoreMenuListDto storeMenuListDto = getStoreMenuListDto(5);

            given(storeRepository.findByStoreIdWithMenuList(storeId)).willReturn(storeMenuListDto);

            // when
            GetStoreResponseDto getStoreResponseDto = storeService.getStore(storeId);

            // then
            assertNotNull(getStoreResponseDto);
            assertFalse(getStoreResponseDto.getMenuList().isEmpty());
        }
    }

    @Nested
    @DisplayName("상점 수정")
    class modifyStore {
        @Test
        @DisplayName("유저가 해당 가게의 점주가 아닐 경우 상점 수정에 실패")
        public void modifyStore_storeNotFound_failure() {
            // given
            long storeId = 1L;
            UserInfo userInfo = getOwnerInfo();
            ModifyStoreRequestDto modifyStoreRequestDto = getModifyStoreRequestDto();

            Store store = getStore(getUserInfo());

            given(storeRepository.findByStoreId(anyLong())).willReturn(store);

            // when, then
            assertThrows(InvalidParameterException.class,
                    () -> storeService.modifyStore(storeId, userInfo, modifyStoreRequestDto));
        }

        @Test
        @DisplayName("가게명 변경 시 이미 존재하는 가게명인 경우 상점 수정에 실패")
        public void modifyStore_duplicateStoreName_failure() {
            // given
            long storeId = 1L;
            UserInfo userInfo = getOwnerInfo();
            ModifyStoreRequestDto modifyStoreRequestDto = getModifyStoreRequestDto();

            Store store = getStore(getOwnerInfo());

            given(storeRepository.findByStoreId(anyLong())).willReturn(store);
            given(storeRepository.existsByName(anyString())).willReturn(true);

            // when, then
            assertThrows(DuplicateElementException.class,
                    () -> storeService.modifyStore(storeId, userInfo, modifyStoreRequestDto));
        }

        @Test
        @DisplayName("성공")
        public void modifyStore_success() {
            // given
            long storeId = 1L;
            UserInfo userInfo = getOwnerInfo();
            ModifyStoreRequestDto modifyStoreRequestDto = getModifyStoreRequestDto();

            Store store = getStore(getOwnerInfo());
            String prvStoreName = store.getName();

            given(storeRepository.findByStoreId(anyLong())).willReturn(store);
            given(storeRepository.existsByName(anyString())).willReturn(false);

            // when
            ModifyStoreResponseDto modifyStoreResponseDto = storeService.modifyStore(storeId, userInfo, modifyStoreRequestDto);

            // then
            assertEquals(modifyStoreResponseDto.getStoreId(), store.getId());
            assertEquals(modifyStoreRequestDto.getName(), modifyStoreResponseDto.getName());
            assertNotEquals(modifyStoreResponseDto.getName(), prvStoreName);
        }
    }
}