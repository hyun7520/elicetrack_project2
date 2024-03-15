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
    const response = await fetch("http://34.64.249.228:8080/categories");
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

      // 이미지 파일 이름 추출
      const filename = imageUrl.substring(imageUrl.lastIndexOf('\\') + 1);

      // 이미지 URL 생성
      const imageURL = `http://34.64.249.228.228:8080/${filename}`;

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
                <img src="${imageURL}" alt="카테고리 이미지" />
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
      card.addEventListener('click', async () => {
        const categoryId = card.getAttribute('data-categoryId');
        try {
          const response = await fetch(`http://34.64.249.228:8080/categories/${categoryId}/subcategories`);
          if (!response.ok) {
            throw new Error("Failed to fetch subcategories");
          }
          const subcategories = await response.json();

          // 하위 카테고리가 있는 경우 팝업 창 표시
          if (subcategories.length > 0) {
            const selectedSubcategory = await selectSubcategory(subcategories);
            if (selectedSubcategory) {
              window.location.href = `/product-list/product-list.html?categoryId=${selectedSubcategory.categoryId}`;
            }
          } else {
            // 하위 카테고리가 없는 경우 알림 표시
            showNoSubcategoriesAlert();
          }
        } catch (error) {
          console.error("Error fetching subcategories:", error);
        }
      });
    });
  } catch (error) {
    console.error("Error fetching categories:", error);
  }
}

// 하위 카테고리 선택 팝업 창 표시 함수
async function selectSubcategory(subcategories) {
  return new Promise((resolve) => {
    const modal = document.createElement('div');
    modal.classList.add('modal', 'is-active');
    modal.innerHTML = `
      <div class="modal-background"></div>
      <div class="modal-content">
        <div class="box">
          <p>하위 카테고리를 선택하세요:</p>
          <div class="content">
            ${subcategories.map(subcategory => `
              <a href="#" class="subcategory" data-categoryId="${subcategory.categoryId}">${subcategory.categoryName}</a>
            `).join('')}
          </div>
        </div>
        <button class="modal-close is-large" aria-label="close"></button>
      </div>
    `;
    document.body.appendChild(modal);

    // 하위 카테고리 링크 클릭 시 해당 카테고리의 categoryId를 resolve하여 반환
    modal.querySelectorAll('.subcategory').forEach(subcategoryLink => {
      subcategoryLink.addEventListener('click', (event) => {
        event.preventDefault();
        const selectedCategoryId = event.target.getAttribute('data-categoryId');
        modal.remove();
        resolve({ categoryId: selectedCategoryId });
      });
    });

    // 모달 닫기 버튼 클릭 시 모달 닫기
    modal.querySelector('.modal-close').addEventListener('click', () => {
      modal.remove();
      navigate('/home.html');
    });

    // 모달 배경 클릭 시 모달 닫기
    modal.querySelector('.modal-background').addEventListener('click', () => {
      modal.remove();
      navigate('/home.html');
    });
  });
}

// 하위 카테고리가 없는 경우 알림 표시
function showNoSubcategoriesAlert() {
  const modal = document.createElement('div');
  modal.classList.add('modal', 'is-active');
  modal.innerHTML = `
    <div class="modal-background"></div>
    <div class="modal-content">
      <div class="box">
        <p>소속된 카테고리가 없습니다.</p>
      </div>
      <button class="modal-close is-large" aria-label="close"></button>
    </div>
  `;
  document.body.appendChild(modal);

  // 닫기 버튼 클릭 시 모달 닫기
  modal.querySelector('.modal-close').addEventListener('click', () => {
    modal.remove();
    navigate('/home.html');
  });

  // 모달 배경 클릭 시 모달 닫기
  modal.querySelector('.modal-background').addEventListener('click', () => {
    modal.remove();
    navigate('/home.html');
  });
}
document.addEventListener('DOMContentLoaded', function() {
  // 세션 스토리지에서 isAdmin 값을 가져옵니다.
  const isAdmin = sessionStorage.getItem('isAdmin');

  // isAdmin이 "true" 문자열로 저장되어 있다면 버튼을 표시합니다.
  if (isAdmin === "true") {
    const adminBtn = document.getElementById('adminPageBtn');
    adminBtn.style.display = 'block'; // 버튼을 보이게 합니다.

    // 버튼 클릭 이벤트를 추가합니다.
    adminBtn.addEventListener('click', function() {
      window.location.href = "/admin"; // "/admin" 페이지로 이동합니다.
    });
  }
});
