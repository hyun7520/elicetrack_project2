import { checkLogin, navigate, createNavbar } from "../../useful-functions.js";

// 요소(element), input 혹은 상수
const orderDetailButton = document.querySelector("#orderDetailButton");
const shoppingButton = document.querySelector("#shoppingButton");

checkLogin();
addAllElements();
addAllEvents();

const isAdmin = sessionStorage.getItem("isAdmin");

// html에 요소를 추가하는 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllElements() {
  createNavbar();
}

// addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() {
  orderDetailButton.addEventListener("click", function () {
    if (isAdmin) {
      document.location.href = '/admin-orders'
    }
    document.location.href = '/account-orders'
  });
  shoppingButton.addEventListener("click", function () { document.location.href = "/home" });
}
