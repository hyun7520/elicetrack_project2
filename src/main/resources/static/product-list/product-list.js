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

// addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() {}

// URL에서 categoryId 가져오기
function getCategoryParams() {
  const { categoryId } = getUrlParams();
  return categoryId;
}

async function addProductItemsToContainer(categoryId, page = 1) {
  productItemContainer.innerHTML = "";

  try {
    const response = await fetch(`http://localhost:8080/products/category/${categoryId}?page=${page}&size=10`);
    if (!response.ok) {
      throw new Error("Failed to fetch products");
    }
    const products = await response.json();
    console.log(products);

    for (const product of products.content) {
      const random = randomId();
      const imageUrl = `http://localhost:8080${product.productImageUrl}`;

      productItemContainer.insertAdjacentHTML(
          "beforeend",
          `
        <div class="message media product-item" id="a${random}" data-productId="${product.productId}">
            <div class="media-left">
                <figure class="image">
                    <img src="${imageUrl}" alt="제품 이미지"/>
                </figure>
            </div>
            <div class="media-content">
                <div class="content">
                    <p class="title">${product.productName}</p>
                    <p class="description">${product.content}</p>
                    <p class="price">${addCommas(product.price)}원</p>
                </div>
            </div>
             <button class="cart-button" data-productId="${product.productId}">장바구니</button>
        </div>
        `
      );
      const productItem = document.querySelector(`#a${random}`);
      const cartButton = productItem.querySelector('.cart-button');
      cartButton.addEventListener(
          "click",
          () => {
            //const productId = productItem.getAttribute('data-productId');
            window.location.href = `/cart`; // 장바구니 페이지로 이동
          }
      );

      // 장바구니 버튼을 제외한 영역에 대한 클릭 이벤트
      productItem.addEventListener(
          "click",
          (event) => {
            if (!event.target.classList.contains('cart-button')) {
              const productId = productItem.getAttribute('data-productId');
              window.location.href = `/product-detail/product-detail.html?productId=${productId}`;
            }
          }
      );
    }
  } catch (error) {
    console.error('데이터를 불러오는 중 오류가 발생했습니다:', error.message);
  }
}

async function addAllElements() {
  createNavbar();

  // URL 파라미터에서 categoryId를 추출합니다. (여기부분 수정!!!!!! restcontroller문제)
  const categoryId = getCategoryParams();

  if (categoryId) {
    const params = new URLSearchParams(window.location.search);
    const page = params.get("page") || 1;
    await addProductItemsToContainer(categoryId, page);
    await addPagination(categoryId);
  } else {
    console.error("카테고리 ID가 제공되지 않았습니다.");
  }
}

async function addPagination(categoryId) {
  // 페이지네이션 컨테이너를 찾을 때 사용되는 쿼리 셀렉터 수정
  const paginationContainer = document.querySelector(".pagination-list");


  try {
    const response = await fetch(`http://localhost:8080/products/category/${categoryId}?page=1&size=10`);
    if (!response.ok) {
      throw new Error("Failed to fetch pagination data");
    }
    const data = await response.json();
    const totalPages = data.totalPages;

    for (let i = 0; i < totalPages; i++) {
      const pageLink = document.createElement("a");
      pageLink.textContent = i + 1;
      pageLink.href = `javascript:void(0);`;
      pageLink.addEventListener("click", () => addProductItemsToContainer(categoryId, i + 1));

      paginationContainer.appendChild(pageLink);
    }
  } catch (error) {
    console.error('페이지네이션을 생성하는 중 오류가 발생했습니다:', error.message);
  }
}
