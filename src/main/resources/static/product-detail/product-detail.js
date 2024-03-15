
import {
  getUrlParams,
  addCommas,
  checkUrlParams,
  createNavbar,
} from "../../useful-functions.js";
import { addToDb, putToDb } from "../../indexed-db.js";

// 요소(element), input 혹은 상수
const productImageTag = document.querySelector("#productImageTag");
const manufacturerTag = document.querySelector("#manufacturerTag");
const titleTag = document.querySelector("#titleTag");
const detailDescriptionTag = document.querySelector("#detailDescriptionTag");
const addToCartButton = document.querySelector("#addToCartButton");
const purchaseButton = document.querySelector("#purchaseButton");

checkUrlParams("productId");
addAllElements();
addAllEvents();

// 고상현 추가: 현재 로그인한 유저의 아이디를 가져옵니다.
const sessionUser = sessionStorage.getItem("id");

// html에 요소를 추가하는 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
async function addAllElements() {
  createNavbar();

  // URL 파라미터에서 categoryId를 추출합니다.
  const { productId } = getUrlParams();

  if (productId) {
    await insertProductData(productId);
  } else {
    console.error("카테고리 ID가 제공되지 않았습니다.");
  }
}

// addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() { }

async function insertProductData(productId) {
  try {
    const response = await fetch(`http://34.64.249.228:8080/products/${productId}`);
    if (!response.ok) {
      throw new Error("Failed to fetch product details");
    }
    const product = await response.json();
    console.log(product);

    // 객체 destructuring
    const {
      productName,
      content,
      brandName,
      isRecommended,
      price,
      category: { imageUrl }
    } = product;

    // 이미지 파일 이름 추출
    const filename = imageUrl.substring(imageUrl.lastIndexOf('\\') + 1);

    // 이미지 URL 생성
    const imageURL = `http://34.64.249.228:8080/${filename}`;

    productImageTag.src = imageURL;
    titleTag.innerText = productName;
    detailDescriptionTag.innerText = content;
    manufacturerTag.innerText = brandName;
    priceTag.innerText = `${addCommas(price)}원`;

    if (isRecommended) {
      titleTag.insertAdjacentHTML(
        "beforeend",
        '<span class="tag is-success is-rounded">추천</span>'
      );
    }

    addToCartButton.addEventListener("click", async () => {

      const result = await fetch(`http://34.64.249.228:8080/carts/user/${sessionUser}/items?product=${productId}`, {
        method: "POST",
        headers: { 'Content-Type': 'application/json' }
      });

      // Key already exists 에러면 아래와 같이 alert함
      if (result.status === 400) {
        alert("이미 장바구니에 추가되어 있습니다.");
      } else {
        alert("장바구니에 추가되었습니다.");
      }
    });

    purchaseButton.addEventListener("click", async () => {
      try {
        await insertDb(product);

        window.location.href = "/order";
      } catch (err) {
        console.log(err);

        //insertDb가 에러가 되는 경우는 이미 제품이 장바구니에 있던 경우임
        //따라서 다시 추가 안 하고 바로 order 페이지로 이동함
        window.location.href = "/order";
      }
    });
  } catch (error) {
    console.error('데이터를 불러오는 중 오류가 발생했습니다:', error.message);
  }
}

async function insertDb(product) {
  // 객체 destructuring
  const { id: id, price } = product;

  // 장바구니 추가 시, indexedDB에 제품 데이터 및
  // 주문수량 (기본값 1)을 저장함.
  await addToDb("cart", { ...product, quantity: 1 }, id);

  // 장바구니 요약(=전체 총합)을 업데이트함.
  await putToDb("order", "summary", (data) => {
    // 기존 데이터를 가져옴
    const count = data.productsCount;
    const total = data.productsTotal;
    const ids = data.ids;
    const selectedIds = data.selectedIds;

    // 기존 데이터가 있다면 1을 추가하고, 없다면 초기값 1을 줌
    data.productsCount = count ? count + 1 : 1;

    // 기존 데이터가 있다면 가격만큼 추가하고, 없다면 초기값으로 해당 가격을 줌
    data.productsTotal = total ? total + price : price;

    // 기존 데이터(배열)가 있다면 id만 추가하고, 없다면 배열 새로 만듦
    data.ids = ids ? [...ids, id] : [id];

    // 위와 마찬가지 방식
    data.selectedIds = selectedIds ? [...selectedIds, id] : [id];
  });
}

//리뷰 데이터 가져오는 api 호출
async function fetchReviews(productId) {
  try {
    const response = await fetch(`http://34.64.249.228:8080/reviews/product/${productId}`);
    if (!response.ok) {
      throw new Error("Failed to fetch reviews");
    }
    const reviews = await response.json();
    return reviews.content;
  } catch (error) {
    console.error("Error fetching reviews:", error);
    return [];
  }
}
