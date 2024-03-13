import * as Api from "../../api.js";
import { checkLogin, createNavbar } from "../../useful-functions.js";

// 요소(element), input 혹은 상수
const titleInput = document.querySelector("#titleInput");
const descriptionInput = document.querySelector("#descriptionInput");
const themeSelectBox = document.querySelector("#themeSelectBox");
const imageInput = document.querySelector("#imageInput");
const fileNameSpan = document.querySelector("#fileNameSpan");
const submitButton = document.querySelector("#addCategoryButton");
const registerCategoryForm = document.querySelector("#registerCategoryForm");

checkLogin();
addAllElements();
addAllEvents();

// html에 요소를 추가하는 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
async function addAllElements() {
  createNavbar();
}

// 여러 개의 addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() {
  submitButton.addEventListener("click", handleSubmit);
  themeSelectBox.addEventListener("change", handleColorChange);
  imageInput.addEventListener("change", handleImageUpload);
}

// 카테고리 추가하기 - 사진은 AWS S3에 저장, 이후 카테고리 정보를 백엔드 db에 저장.
async function handleSubmit(e) {
  e.preventDefault();

  const title = titleInput.value;
  const description = descriptionInput.value;
  const themeClass = themeSelectBox.value;
  const image = imageInput.files[0];

  // 입력 칸이 비어 있으면 진행 불가
  if (!title || !description) {
    return alert("빈 칸이 없어야 합니다.");
  }

  if (!themeClass) {
    return alert("테마를 선택해 주세요.");
  }

  if (image.size > 3e6) {
    return alert("사진은 최대 2.5MB 크기까지 가능합니다.");
  }

  try {
    const formData = new FormData();
    formData.append('image', image);
    const response = await Api.postFormData("/upload-image", formData); // 이미지를 서버로 업로드
    const imageUrl = response.data; // 저장된 이미지의 URL

    const data = { title, description, themeClass, imageUrl };

    await Api.post("/categories", data);

    alert(`정상적으로 ${title} 카테고리가 등록되었습니다.`);

    // 폼 초기화
    registerCategoryForm.reset();
    fileNameSpan.innerText = "";
    themeSelectBox.style.backgroundColor = "white";
    themeSelectBox.style.color = "black";
  } catch (err) {
    console.error(err.stack);
    alert(`문제가 발생하였습니다. 확인 후 다시 시도해 주세요: ${err.message}`);
  }
}


// 사용자가 사진을 업로드했을 때, 파일 이름이 화면에 나타나도록 함.
async function handleImageUpload() {
  const imageInput = document.querySelector("#imageInput");
  const file = imageInput.files[0];
  if (file) {
    try {
      const formData = new FormData();
      formData.append('image', file);

      const response = await fetch("http://localhost:8080/categories/upload-image", {
        method: 'POST',
        body: formData
      });

      if (!response.ok) {
        throw new Error('이미지를 업로드하는 중에 문제가 발생했습니다.');
      }

      const imageUrl = await response.json(); // 저장된 이미지의 URL

      // 이미지 업로드에 성공하면 필요한 후속 작업 수행
    } catch (error) {
      console.error("이미지 업로드 중에 오류가 발생했습니다.", error);
      // 오류 처리
    }
  }
}


// 색상 선택 시, 선택박스에 해당 색상 반영되게 함.
function handleColorChange() {
  const index = themeSelectBox.selectedIndex;

  themeSelectBox.style.color = themeSelectBox[index].style.color;
  themeSelectBox.style.backgroundColor =
    themeSelectBox[index].style.backgroundColor;
}
