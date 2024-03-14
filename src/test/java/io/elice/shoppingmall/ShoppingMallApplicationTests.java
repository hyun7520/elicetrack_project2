package io.elice.shoppingmall;

import io.elice.shoppingmall.product.dto.ProductRequestDto;
import io.elice.shoppingmall.product.entity.Product;
import io.elice.shoppingmall.product.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ShoppingMallApplicationTests {
	@Autowired
	private ProductService productService;

/*	@Test
	void testJpa() {
		// 상품 등록
		for (int i = 1; i <= 300; i++) {
			String subject = String.format("테스트 데이터입니다:[%03d]", i);
			String content = "내용무";

			// ProductRequestDto 객체 생성
			ProductRequestDto requestDto = new ProductRequestDto();
			requestDto.setProductName(subject);
			requestDto.setContent(content);

			// ProductService의 createProduct 메서드에 ProductRequestDto 전달
			this.productService.createProduct(requestDto);
		}

		// 페이징 테스트
		int page = 0; // 페이지 번호 (0부터 시작)
		int size = 10; // 페이지당 항목 수
		Page<Product> productPage = productService.getAllproductList(page, size);

		// 페이징 결과 검증
		assertNotNull(productPage);
		assertEquals(size, productPage.getSize()); // 페이지 크기 확인
		assertEquals(300, productPage.getTotalElements()); // 총 요소 수 확인
		assertEquals(30, productPage.getTotalPages()); // 총 페이지 수 확인
	}*/
}
