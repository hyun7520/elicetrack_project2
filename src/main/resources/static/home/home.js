<<<<<<< HEAD
import * as Api from "../api.js";
import { getImageUrl } from "../aws-s3.js";
import { navigate, createNavbar } from "../useful-functions.js";
// import {attach} from "bulma-carousel/src/js";


// 요소(element), input 혹은 상수
const sliderDiv = document.querySelector("#slider");
const sliderArrowLeft = document.querySelector("#sliderArrowLeft");
const sliderArrowRight = document.querySelector("#sliderArrowRight");
=======
// home.js
import { navigate, createNavbar } from "../useful-functions.js";
>>>>>>> 1d45952b32fc70e0fbce5ff0ca1933958d18d60d

addAllElements();
addAllEvents();

// html에 요소를 추가하는 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
async function addAllElements() {
  createNavbar();
<<<<<<< HEAD
  await addImageCardsToSlider();
  attachSlider();
=======
  await addImageCardsToBlocks();
>>>>>>> 1d45952b32fc70e0fbce5ff0ca1933958d18d60d
}

// 여러 개의 addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() {}

<<<<<<< HEAD
// api에서 카테고리 정보 및 사진 가져와서 슬라이드 카드로 사용
async function addImageCardsToSlider() {
  const categories = await Api.get("/categories");
  console.log(categories)

  for (const category of categories) {
    // 객체 destructuring
    const { id, title, description, themeClass, imageKey } = category;

    sliderDiv.insertAdjacentHTML(
      "beforeend",
      `
      <div class="card" id="category-${id}">
        <div class="notification ${themeClass}">
          <p class="title is-3 is-spaced">${title}</p>
          <p class="subtitle is-6">${description}</p>
        </div>
        <div class="card-image">
          <figure class="image is-3by2">
            <img
              src="${imageKey}"
              alt="카테고리 이미지"
            />
          </figure>
        </div>
      </div>
    `
    );

    const card = document.querySelector(`#category-${id}`);

    card.addEventListener("click", navigate(`/product/list?category=${title}`));
  }
}

function attachSlider() {
  // 페이지 로드 완료 후 bulmaCarousel 라이브러리의 attach 함수를 사용합니다.
  document.addEventListener('DOMContentLoaded', () => {
    const imageSlider = bulmaCarousel.attach("#slider", {
      autoplay: true,
      autoplaySpeed: 6000,
      infinite: true,
      duration: 500,
      pauseOnHover: false,
      navigation: false,
    });

    sliderArrowLeft.addEventListener("click", () => {
      imageSlider[0].previous();
    });

    sliderArrowRight.addEventListener("click", () => {
      imageSlider[0].next();
    });
  });
}

=======
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
        window.location.href = `http://localhost:8080/products/category/${categoryId}`;
      });
    });
  } catch (error) {
    console.error("Error fetching categories:", error);
  }
}
>>>>>>> 1d45952b32fc70e0fbce5ff0ca1933958d18d60d
