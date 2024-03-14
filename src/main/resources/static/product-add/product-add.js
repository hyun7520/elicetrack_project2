import { checkLogin, randomId, createNavbar } from "../../useful-functions.js";

// 요소(element)들과 상수들
const titleInput = document.querySelector("#titleInput");
const categorySelectBox = document.querySelector("#categorySelectBox");
const manufacturerInput = document.querySelector("#manufacturerInput");
const shortDescriptionInput = document.querySelector("#shortDescriptionInput");
const detailDescriptionInput = document.querySelector(
  "#detailDescriptionInput"
);
const imageInput = document.querySelector("#imageInput");
const inventoryInput = document.querySelector("#inventoryInput");
const deliveryPriceInput = document.querySelector("#deliveryPriceInput");
const priceInput = document.querySelector("#priceInput");
const searchKeywordInput = document.querySelector("#searchKeywordInput");
const submitButton = document.querySelector("#submitButton");
const registerProductForm = document.querySelector("#registerProductForm");

checkLogin();
addAllElements();
addAllEvents();

// html에 요소를 추가하는 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllElements() {
  createNavbar();
  addOptionsToSelectBox();
}

// addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() {
  imageInput.addEventListener("change", uploadImage);
  submitButton.addEventListener("click", handleSubmit);
  categorySelectBox.addEventListener("change", handleCategoryChange);
  imageInput.addEventListener("change", handleImageInput);
  deliveryPriceInput.addEventListener("input", handleDeliveryPriceInput);
}

// 이미지 정보 수집
function handleImageInput() {
  const file = imageInput.files[0];
  if (file) {
    fileNameSpan.innerText = file.name; // 파일 이름을 표시
  }
}

// 제품 추가 - 사진은 지정 경로에 저장, 이후 제품 정보를 백엔드 db에 저장.
async function handleSubmit(e) {
  e.preventDefault();

  const productName = titleInput.value;
  const categoryId = categorySelectBox.value;
  const brandName = manufacturerInput.value;
  const shortDescription = shortDescriptionInput.value;
  const content = detailDescriptionInput.value;
  const image = imageInput.files[0];
  const inventory = parseInt(inventoryInput.value);
  const deliveryPrice = deliveryPriceInput.value;
  const price = parseInt(priceInput.value);

  // 입력 칸이 비어 있으면 진행 불가
  if (
    !productName ||
    !categoryId ||
    !brandName ||
    !shortDescription ||
    !content ||
    !inventory ||
    !price || !deliveryPrice
  ) {
    return alert("빈 칸 및 0이 없어야 합니다.");
  }

  if (image.size > 3e6) {
    return alert("사진은 최대 2.5MB 크기까지 가능합니다.");
  }

  try {
    const imageUrl = await uploadImage(image);

    const data = {
      productName,
      categoryId,
      brandName,
      //shortDescription,
      content,
      imageUrl,
      inventory,
      deliveryPrice,
      price,
    };

    const response = await fetch("http://34.64.249.228:8080/products", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      throw new Error("Failed to register product.");
    }

    alert(`정상적으로 ${productName} 제품이 등록되었습니다.`);

    // 폼 초기화
    registerProductForm.reset();
    fileNameSpan.innerText = "";
    categorySelectBox.style.color = "black";
    categorySelectBox.style.backgroundColor = "white";
  } catch (err) {
    console.log(err.stack);

    alert(`문제가 발생하였습니다. 확인 후 다시 시도해 주세요: ${err.message}`);
  }
}

// 이미지 업로드 함수
async function uploadImage(file) {
  try {
    const formData = new FormData();
    formData.append('file', file);

    const response = await fetch("http://34.64.249.228:8080/products/upload-image", {
      method: 'POST',
      body: formData
    });

    if (!response.ok) {
      throw new Error('이미지를 업로드하는 중에 문제가 발생했습니다.');
    }

    const imageUrl = await response.text(); // 저장된 이미지의 URL

    return imageUrl;
  } catch (error) {
    throw new Error('이미지 업로드 중에 오류가 발생했습니다.');
  }
}

// 선택할 수 있는 카테고리 종류를 api로 가져와서, 옵션 태그를 만들어 삽입함.
async function addOptionsToSelectBox() {
  try {
    const response = await fetch("http://34.64.249.228:8080/categories?parentId=1");
    if (!response.ok) {
      throw new Error("Failed to fetch categories");
    }
    const categories = await response.json();

    console.log("카테고리들:", categories);

    // 부모 카테고리가 아닌 경우에만 옵션 추가
    const childCategories = categories.filter(category => category.parentId !== null);

    console.log("카테고리들:", childCategories);

    // 옵션을 한 번에 추가하기 위해 innerHTML 사용
    categorySelectBox.innerHTML = childCategories.map(category => `
      <option value="${category.categoryId}">${category.categoryName}</option>
    `).join('');
  } catch (error) {
    console.error("카테고리를 불러오는 동안 문제가 발생했습니다:", error);
  }
}

// 카테고리 선택 시, 선택박스에 해당 카테고리 테마가 반영되게 함.
function handleCategoryChange() {
  const index = categorySelectBox.selectedIndex;

  categorySelectBox.className = categorySelectBox[index].className;
}

// 아래 함수는, 검색 키워드 추가 시, 해당 키워드로 만든 태그가 화면에 추가되도록 함.
// 아래 배열은, 나중에 api 요청 시 사용함.
let searchKeywords = [];
function handleKeywordAdd(e) {
  e.preventDefault();

  const newKeyword = searchKeywordInput.value;

  if (!newKeyword) {
    return;
  }

  if (searchKeywords.includes(newKeyword)) {
    return alert("이미 추가한 검색어입니다.");
  }

  searchKeywords.push(newKeyword);

  const random = randomId();

  keywordsContainer.insertAdjacentHTML(
    "beforeend",
    `
    <div class="control" id="a${random}">
      <div class="tags has-addons">
        <span class="tag is-link is-light">${newKeyword}</span>
        <a class="tag is-link is-light is-delete"></a>
      </div>
    </div>
  `
  );

  // x 버튼에 삭제 기능 추가.
  keywordsContainer
    .querySelector(`#a${random} .is-delete`)
    .addEventListener("click", handleKeywordDelete);

  // 초기화 및 사용성 향상
  searchKeywordInput.value = "";
  searchKeywordInput.focus();
}

function handleKeywordDelete(e) {
  // a 태그 클릭 -> 옆의 span 태그의 inenerText가 키워드임.
  const keywordToDelete = e.target.previousElementSibling.innerText;

  // 배열에서 삭제
  const index = searchKeywords.indexOf(keywordToDelete);
  searchKeywords.splice(index, 1);

  // 요소 삭제
  e.target.parentElement.parentElement.remove();
}

// 배송 가격 입력 처리
function handleDeliveryPriceInput() {
  const deliveryPrice = parseInt(deliveryPriceInput.value);
  if (deliveryPrice < 0 || isNaN(deliveryPrice)) {
    // 배송 가격이 음수이거나 숫자가 아닌 경우 경고 메시지 출력
    deliveryPriceInput.classList.add("is-danger");
    // 예를 들어, 입력 필드를 빨간색으로 강조하여 사용자에게 오류를 알릴 수 있습니다.
  } else {
    deliveryPriceInput.classList.remove("is-danger");
  }
}
