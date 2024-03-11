import { getImageUrl } from "../../aws-s3.js";
import * as Api from "../../api.js";
import {
  randomId,
  getUrlParams,
  addCommas,
  navigate,
  checkUrlParams,
  createNavbar,
} from "../../useful-functions.js";

// 요소(element), input 혹은 상수
const productItemContainer = document.querySelector("#producItemContainer");

checkUrlParams("categoryId");
addAllElements();
addAllEvents();

async function addAllElements() {
  createNavbar();

  const { categoryId } = getUrlParams();

  if (categoryId) {
    await addProductItemsToContainer(categoryId);
    await addPagination(categoryId); // 페이지네이션을 추가합니다.
  } else {
    console.error("카테고리 ID가 제공되지 않았습니다.");
  }
}


// addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() {}

// URL에서 categoryId 가져오기
function getCategoryParams() {
  const { categoryId } = getUrlParams();
  return categoryId;
}

async function addProductItemsToContainer(categoryId) {
  try {
    const response = await fetch(`http://localhost/products/category/${categoryId}`);
    if (!response.ok) {
      throw new Error('네트워크 오류: ' + response.status);
    }
    const products = await response.json();

    for (const product of products.content) {
      const random = randomId();
      const imageUrl = await getImageUrl(product.productImageUrl);

      productItemContainer.insertAdjacentHTML(
          "beforeend",
          `
        <div class="message media product-item" id="a${random}">
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
        </div>
        `
      );

      const productItem = document.querySelector(`#a${random}`);
      productItem.addEventListener(
          "click",
          () => navigate(`/product/detail?id=${product.productId}`)
      );
    }
  } catch (error) {
    console.error('데이터를 불러오는 중 오류가 발생했습니다:', error.message);
  }
}


async function loadProductsByPage(categoryId, page) {
  productItemContainer.innerHTML = "";

  const response = await fetch(`http://localhost/products/category/${categoryId}?page=${page}&size=10`);
  const products = await response.json();

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
}