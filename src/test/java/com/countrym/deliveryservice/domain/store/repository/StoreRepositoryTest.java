package com.countrym.deliveryservice.domain.store.repository;

import com.countrym.deliveryservice.common.config.spring.JPAConfig;
import com.countrym.deliveryservice.domain.store.dto.response.GetStoreListResponseDto;
import com.countrym.deliveryservice.domain.store.entity.Store;
import com.countrym.deliveryservice.domain.store.enums.StoreSortType;
import com.countrym.deliveryservice.domain.store.enums.StoreType;
import com.countrym.deliveryservice.domain.user.entity.User;
import com.countrym.deliveryservice.domain.user.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.countrym.deliveryservice.domain.data.store.StoreMockData.getRegisterStoreRequestDto;
import static com.countrym.deliveryservice.domain.data.user.UserMockData.getOwnerSignUpRequestDto;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import(JPAConfig.class)
@ExtendWith(SpringExtension.class)
class StoreRepositoryTest {
    private static final Logger log = LoggerFactory.getLogger(StoreRepositoryTest.class);

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Nested
    @DisplayName("가게 목록 조회")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class findStoreList {
        private final int listSize = 40;
        private List<Store> storeList;

        @BeforeEach
        public void setup() {
            User owner = User.from(getOwnerSignUpRequestDto(), "owner123");
            userRepository.save(owner);

            storeList = new ArrayList<>();

            for (int i = 1; i <= listSize; i++) {
                Store store = Store.from(owner, getRegisterStoreRequestDto());
                ReflectionTestUtils.setField(store, "name", "store number " + i);
                ReflectionTestUtils.setField(store, "type", StoreType.values()[i % StoreType.values().length]);
                ReflectionTestUtils.setField(store, "averageRating", new Random().nextInt(0, 11) * 0.5);
                ReflectionTestUtils.setField(store, "reviewAmount", new Random().nextInt(0, 101));
                storeList.add(store);
            }

            storeRepository.saveAll(storeList);
        }

        @Test
        @DisplayName("가게명으로 가게 목록 조회 성공")
        public void findStoreListWithName() {
            // given
            String name1 = "2";
            String name2 = "store";

            PageRequest pageRequest = PageRequest.of(0, listSize, Sort.by(Sort.Direction.ASC, "id"));

            // when
            List<GetStoreListResponseDto> name1FilteredList = storeRepository.findAllByTypeAndNameByPaging(null, name1, pageRequest);
            List<GetStoreListResponseDto> name2FilteredList = storeRepository.findAllByTypeAndNameByPaging(null, name2, pageRequest);

            // then
            long name1FilteredCount = storeList.stream().filter(store -> store.getName().contains(name1)).count();
            long name2FilteredCount = storeList.stream().filter(store -> store.getName().contains(name2)).count();

            assertEquals(name1FilteredCount, name1FilteredList.size());
            assertEquals(name2FilteredCount, name2FilteredList.size());
        }

        @Test
        @DisplayName("가게 분류로 가게 목록 조회 성공")
        public void findStoreListWithStoreType() {
            // given
            StoreType storeType = StoreType.CHICKEN;

            PageRequest pageRequest = PageRequest.of(0, listSize, Sort.by(Sort.Direction.ASC, "id"));

            // when
            List<GetStoreListResponseDto> typeFilteredList = storeRepository.findAllByTypeAndNameByPaging(storeType, null, pageRequest);

            // then
            List<Long> filteredList = storeList.stream().filter(store -> store.getType().equals(storeType)).map(Store::getId).toList();

            assertEquals(filteredList.size(), typeFilteredList.size());
            assertEquals(filteredList, typeFilteredList.stream().map(GetStoreListResponseDto::getStoreId).toList());
        }

        @Test
        @DisplayName("별점 높은 순으로 정렬 성공")
        public void findStoreListWithSorting() {
            // given
            storeList.sort((o1, o2) -> {
                if (o1.getAverageRating() == o2.getAverageRating()) {
                    return o2.getReviewAmount() - o1.getReviewAmount();
                }

                int result;
                if (o1.getAverageRating() > o2.getAverageRating()) result = -1;
                else result = 1;

                return result;
            });

            PageRequest pageRequest = PageRequest.of(0, listSize, Sort.by(StoreSortType.HIGH_AVERAGE_RATING.name()));

            // when
            List<GetStoreListResponseDto> sortedList = storeRepository.findAllByTypeAndNameByPaging(null, null, pageRequest);

            // then
            assertEquals(sortedList.stream().map(GetStoreListResponseDto::getStoreId).toList(), storeList.stream().map(Store::getId).toList());
        }
    }
}