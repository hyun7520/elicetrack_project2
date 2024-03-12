// home.js
import { navigate, createNavbar } from "../useful-functions.js";

addAllElements();
addAllEvents();

// html에 요소를 추가하는 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
async function addAllElements() {
  createNavbar();
  await addImageCardsToBlocks();
}

// 여러 개의 addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() {}

// api에서 카테고리 정보 및 사진 가져와서 블록으로 사용
async function addImageCardsToBlocks() {
  try {
    const response = await fetch("http://localhost:8080/categories");
    if (!response.ok) {
      throw new Error("Failed to fetch categories");
    }
    const categories = await response.json();
    console.log(categories);

    let blockHTML = ''; // 블록의 HTML을 저장할 변수

    for (let i = 0; i < categories.length; i++) {
      const category = categories[i];
      // 객체 destructuring
      const { categoryId, categoryName, content, imageUrl } = category;

      // 각 카테고리에 대한 블록 HTML 생성
      if (i % 3 === 0) {
        blockHTML += '<div class="columns">';
      }
      blockHTML += `
        <div class="column is-one-third">
          <div class="card" data-categoryId="${categoryId}">
            <div class="notification">
              <p class="title is-3 is-spaced">${categoryName}</p>
              <p class="subtitle is-6">${content}</p>
            </div>
            <div class="card-image">
              <figure class="image is-3by2">
                <img src="http://localhost:8080${imageUrl}" alt="카테고리 이미지" />
              </figure>
            </div>
          </div>
        </div>
      `;
      if ((i + 1) % 3 === 0 || i === categories.length - 1) {
        blockHTML += '</div>';
      }
    }

    // 블록에 카드를 추가하기 위해 HTML을 블록에 직접 삽입
    document.getElementById('categoryBlocks').innerHTML = blockHTML;

    // 카드 클릭 시 해당 카테고리의 상품 목록 페이지로 이동
    const cards = document.querySelectorAll('.card');
    cards.forEach(card => {
      card.addEventListener('click', () => {
        const categoryId = card.getAttribute('data-categoryId');
        window.location.href = (`/product-list/product-list.html?categoryId=${categoryId}`);
      });
    });
  } catch (error) {
    console.error("Error fetching categories:", error);
  }
}
