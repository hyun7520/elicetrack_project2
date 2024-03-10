//import { getImageUrl } from "../../aws-s3.js";
import * as Api from "../../api.js";
import {
  randomId,
  getUrlParams,
  addCommas,
  navigate,
  createNavbar,
  checkUrlParams
} from "../../useful-functions.js";

const productItemContainer = document.querySelector("#productItemContainer");

// 카테고리 ID 확인
checkUrlParams("categoryId");

// 모든 요소 추가 및 이벤트 처리
async function initialize() {
  createNavbar();
  const { categoryId } = getUrlParams();

  if (!categoryId) {
    console.error("카테고리 ID가 제공되지 않았습니다.");
    // 카테고리 ID가 없을 때 사용자를 다른 페이지로 리디렉션하거나 적절한 조치를 취하세요.
    return;
  }

  try {
    const products = await Api.get(`/products/${categoryId}?page=0&size=10`);
    const totalProducts = products.totalElements;
    await addProductItems(products.content);
    await addPagination(categoryId, totalProducts);
  } catch (error) {
    console.error("오류가 발생했습니다:", error);
  }
}

// 상품 추가
async function addProductItems(products) {
  try {
    for (const product of products) {
      const random = randomId();
      const imageUrl = await getImageUrl(product.productImageUrl);

      const productItem = document.createElement("div");
      productItem.classList.add("message", "media", "product-item");
      productItem.id = `a${random}`;
      productItem.innerHTML = `
        <div class="media-left">
          <figure class="image">
            <img src="${imageUrl}" alt="제품 이미지" />
          </figure>
        </div>
        <div class="media-content">
          <div class="content">
            <p class="title">${product.productName}</p>
            <p class="description">${product.content}</p>
            <p class="price">${addCommas(product.price)}원</p>
          </div>
        </div>
      `;

      productItem.addEventListener("click", () => navigate(`/product/detail?id=${product.productId}`));

      productItemContainer.appendChild(productItem);
    }
  } catch (error) {
    console.error("상품을 추가하는 도중 오류가 발생했습니다:", error);
    throw error;
  }
}

// 페이지네이션 추가
async function addPagination(categoryId, totalProducts) {
  try {
    const pageSize = 10;
    const totalPages = Math.ceil(totalProducts / pageSize);
    const paginationList = document.getElementById("paginationList");

    for (let i = 1; i <= totalPages; i++) {
      const listItem = document.createElement("li");
      listItem.innerHTML = `<a class="pagination-link">${i}</a>`;

      listItem.addEventListener("click", async () => {
        await loadProductsByPage(categoryId, i - 1);
      });

      paginationList.appendChild(listItem);
    }
  } catch (error) {
    console.error("페이지네이션을 추가하는 도중 오류가 발생했습니다:", error);
    throw error;
  }
}

// 페이지별 상품 로드
async function loadProductsByPage(categoryId, page) {
  try {
    productItemContainer.innerHTML = "";
    const products = await Api.get(`/products/${categoryId}?page=${page}&size=10`);

    for (const product of products.content) {
      const random = randomId();
      const imageUrl = await getImageUrl(product.productImageUrl);

      const productItem = document.createElement("div");
      productItem.classList.add("message", "media", "product-item");
      productItem.id = `a${random}`;
      productItem.innerHTML = `
        <div class="media-left">
          <figure class="image">
            <img src="${imageUrl}" alt="제품 이미지" />
          </figure>
        </div>
        <div class="media-content">
          <div class="content">
            <p class="title">${product.productName}</p>
            <p class="description">${product.content}</p>
            <p class="price">${addCommas(product.price)}원</p>
          </div>
        </div>
      `;

      productItem.addEventListener("click", () => navigate(`/product/detail?id=${product.productId}`));

      productItemContainer.appendChild(productItem);
    }
  } catch (error) {
    console.error("페이지별 상품을 로드하는 도중 오류가 발생했습니다:", error);
    throw error; // 예외를 다시 throw하여 initialize 함수에서도 잡을 수 있도록 함
  }
}

// 초기화 함수 호출
initialize();