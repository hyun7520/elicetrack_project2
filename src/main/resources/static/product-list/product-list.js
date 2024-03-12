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
  const products = await Api.get(`/products/category/${categoryId}?page=0&size=10`);

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
}

async function addPagination(categoryId) {
  const products = await Api.get(`/products/category/${categoryId}?page=0&size=10`);
  const totalProducts = products.totalElements; // 총 상품 수
  const pageSize = 10; // 페이지당 보여줄 상품 수
  const totalPages = Math.ceil(totalProducts / pageSize); // 페이지 수 계산

  const paginationList = document.getElementById("paginationList"); // 페이지네이션을 추가할 요소

  for (let i = 1; i <= totalPages; i++) {
    const listItem = document.createElement("li");
    listItem.innerHTML = `<a class="pagination-link">${i}</a>`;

    listItem.addEventListener("click", async () => {
      await loadProductsByPage(categoryId, i - 1);
    });

    paginationList.appendChild(listItem);
  }
}

async function loadProductsByPage(categoryId, page) {
  productItemContainer.innerHTML = ""; // 이전에 표시된 상품을 지웁니다.

  const products = await Api.get(`/products/category/${categoryId}?page=${page}&size=10`);

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