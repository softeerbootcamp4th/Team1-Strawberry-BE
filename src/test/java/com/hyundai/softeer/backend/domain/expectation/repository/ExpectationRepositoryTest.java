package com.hyundai.softeer.backend.domain.expectation.repository;

import com.hyundai.softeer.backend.domain.expectation.constant.ExpectationPage;
import com.hyundai.softeer.backend.domain.expectation.dto.ExpectationContentDto;
import com.hyundai.softeer.backend.domain.expectation.entity.Expectation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE, connection = EmbeddedDatabaseConnection.H2)
@Sql(scripts = "classpath:sql/integration-h2.sql")
class ExpectationRepositoryTest {

    @Autowired
    ExpectationRepository expectationRepository;

    @Test
    @DisplayName("페이지네이션 테스트")
    void pagenationTest() {
        // given
        Sort sort = Sort.by("createdAt").ascending();

        Pageable pageable = PageRequest.of(
                1,
                ExpectationPage.PAGE_SIZE,
                sort
        );

        // when
        Page<Expectation> expectations = expectationRepository.findByEventId(1L, pageable);
        List<ExpectationContentDto> expectationContent = expectations.get()
                .map(expectation -> {
                    String expectationComment = expectation.getExpectationComment();
                    String name = expectation.getUser().getName();
                    return new ExpectationContentDto(name, expectationComment);
                })
                .toList();
        // then

        assertThat(expectations.getTotalElements()).isEqualTo(13L);
        assertThat(expectations.getTotalPages()).isEqualTo(2);
        assertThat(expectationContent).containsExactly(
                new ExpectationContentDto("김승준", "우우우9"),
                new ExpectationContentDto("한사랑", "우우우10")
        );
    }

    @Test
    @DisplayName("페이지네이션 테스트: 해당 페이지의 정보가 없는 경우")
    void pagenationNotExistTest() {
        // given
        Sort sort = Sort.by("createdAt").ascending();

        Pageable pageable = PageRequest.of(
                100,
                ExpectationPage.PAGE_SIZE,
                sort
        );

        // when
        Page<Expectation> expectations = expectationRepository.findByEventId(1L, pageable);
        List<Expectation> list = expectations.get().toList();

        // then
        assertThat(list.size()).isEqualTo(0);
    }
}