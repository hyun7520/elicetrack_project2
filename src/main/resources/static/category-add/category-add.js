import { checkLogin, createNavbar } from "../../useful-functions.js";

// 요소(element), input 혹은 상수
const titleInput = document.querySelector("#titleInput");
const descriptionInput = document.querySelector("#descriptionInput");
const imageInput = document.querySelector("#imageInput");
const fileNameSpan = document.querySelector("#fileNameSpan");
const submitButton = document.querySelector("#addCategoryButton");
const registerCategoryForm = document.querySelector("#registerCategoryForm");
const parentCategoryId = document.querySelector("#parentCategorySelect");

checkLogin();
addAllElements();
addAllEvents();

// html에 요소를 추가하는 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
async function addAllElements() {
  createNavbar();
  await populateParentCategoriesDropdown();
}

// 여러 개의 addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() {
  submitButton.addEventListener("click", handleSubmit);
  imageInput.addEventListener("change", handleImageInput);
}

// 이미지 정보 수집
function handleImageInput() {
  const file = imageInput.files[0];
  if (file) {
    fileNameSpan.innerText = file.name; // 파일 이름을 표시
  }
}

// 카테고리 추가하기 -  이후 카테고리 정보를 백엔드 db에 저장.
async function handleSubmit(e) {
  e.preventDefault();

  const categoryName  = titleInput.value;
  const content = descriptionInput.value;
  const file = imageInput.files[0];
  const parentId = parentCategoryId.value;

  // 입력 칸이 비어 있으면 진행 불가
  if (!categoryName  || !content) {
    return alert("빈 칸이 없어야 합니다.");
  }

  if (!file) {
    return alert("사진을 선택해 주세요.");
  }

  if (!parentId) {
    return alert("상단 카테고리를 선택하세요.");
  }

  if (file.size > 3e6) {
    return alert("사진은 최대 2.5MB 크기까지 가능합니다.");
  }

  try {
    const imageUrl = await uploadImage(file);

    const data = { categoryName, content, imageUrl, parentId};
    // 백엔드에 POST 요청 보내기
    const response = await fetch('http://localhost:8080/categories', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      throw new Error('Network response was not ok');
    }

    alert(`정상적으로 ${categoryName} 카테고리가 등록되었습니다.`);

    // 폼 초기화
    registerCategoryForm.reset();
    fileNameSpan.innerText = "";
  } catch (err) {
    console.error(err.stack);
    alert(`문제가 발생하였습니다. 확인 후 다시 시도해 주세요: ${err.message}`);
  }
}

// 이미지 업로드 함수
async function uploadImage(file) {
  try {
    const formData = new FormData();
    formData.append('file', file);

    const response = await fetch("http://localhost:8080/categories/upload-image", {
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


// 사용자가 사진을 업로드했을 때, 파일 이름이 화면에 나타나도록 함.
async function handleImageUpload() {
  const imageInput = document.querySelector("#imageInput");
  const file = imageInput.files[0];
  if (file) {
    try {
      console.log("파일 이름:", file.name);
      console.log("파일 크기:", file.size);

      const title = titleInput.value;
      const description = descriptionInput.value;

      if (!title || !description || !themeClass) {
        throw new Error('카테고리 정보를 입력해주세요.');
      }

      const formData = new FormData();
      formData.append('file', file);
      formData.append('title', title);
      formData.append('description', description);
      formData.append('themeClass', themeClass);

      const response = await fetch("http://localhost:8080/categories/upload-image", {
        method: 'POST',
        body: formData
      });

      if (!response.ok) {
        throw new Error('이미지를 업로드하는 중에 문제가 발생했습니다.');
      }

      const imageUrl = await response.text(); // 저장된 이미지의 URL

      console.log("이미지 업로드 성공:", imageUrl);

      // 이미지 업로드에 성공하면 필요한 후속 작업 수행
    } catch (error) {
      console.error("이미지 업로드 중에 오류가 발생했습니다.", error);
      // 오류 처리
    }
  }
}

async function populateParentCategoriesDropdown() {
  try {
    const response = await fetch("http://localhost:8080/categories");
    if (!response.ok) {
      throw new Error("Failed to fetch parent categories");
    }
    const parentCategories = await response.json();

    const parentCategorySelect = document.querySelector("#parentCategorySelect");

    // "없음"을 나타내는 옵션 추가
    const noneOption = document.createElement("option");
    noneOption.value = null; // 값은 빈 문자열
    noneOption.textContent = "없음";
    parentCategorySelect.appendChild(noneOption);

    // parentId가 null인 카테고리를 드롭다운 목록에 추가
    parentCategories.forEach(category => {
      const option = document.createElement("option");
      option.value = category.categoryId;
      option.textContent = category.categoryName;
      parentCategorySelect.appendChild(option);
    });
  } catch (error) {
    console.error("Error populating parent categories dropdown:", error);
  }
}
