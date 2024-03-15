import * as Api from "../api.js";
import {
  addCommas,
  convertToNumber,
  navigate,
  randomPick,
  createNavbar,
  checkLogin,
} from "../useful-functions.js";
import { deleteFromDb, getFromDb, putToDb } from "../indexed-db.js";

// 요소(element), input 혹은 상수
const subtitleCart = document.querySelector("#subtitleCart");
const receiverNameInput = document.querySelector("#receiverName");
const receiverPhoneNumberInput = document.querySelector("#receiverPhoneNumber");
const postalCodeInput = document.querySelector("#postalCode");
const searchAddressButton = document.querySelector("#searchAddressButton");
const address1Input = document.querySelector("#address1");
const address2Input = document.querySelector("#address2");
const requestSelectBox = document.querySelector("#requestSelectBox");
const customRequestContainer = document.querySelector(
  "#customRequestContainer"
);
const customRequestInput = document.querySelector("#customRequest");
const productsTitleElem = document.querySelector("#productsTitle");
const productsTotalElem = document.querySelector("#productsTotal");
const deliveryFeeElem = document.querySelector("#deliveryFee");
const orderTotalElem = document.querySelector("#orderTotal");
const checkoutButton = document.querySelector("#checkoutButton");
const deliveryInfo = document.querySelector('#delivery-info');

const result = [];
const sessionUser = sessionStorage.getItem("id");
if (sessionUser == null) {
  window.alert("로그인 해주세요!");
  checkLogin();
}


const requestOption = {
  1: "직접 수령하겠습니다.",
  2: "배송 전 연락바랍니다.",
  3: "부재 시 경비실에 맡겨주세요.",
  4: "부재 시 문 앞에 놓아주세요.",
  5: "부재 시 택배함에 넣어주세요.",
  6: "직접 입력",
};

addAllElements();
addAllEvents();

// html에 요소를 추가하는 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllElements() {
  deleteFunctions();
  createNavbar();
  insertOrderSummary();
}

// addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() {
  subtitleCart.addEventListener("click", navigate("/cart"));
  searchAddressButton.addEventListener("click", searchAddress);
  requestSelectBox.addEventListener("change", handleRequestChange);
  checkoutButton.addEventListener("click", doCheckout);
}

// 데이터 가져오기
async function fetchData() {
  try {
    const response = await fetch(`http://34.64.249.228:8080/carts/user/${sessionUser}/items`);
    const data = await response.json();
    return data;
  } catch (error) {
    console.log(error);
  }
}

// 장바구니 데이터 출력 및 삭제 기능 추가
async function deleteFunctions() {
  const orderList = document.querySelector('.order-list');
  const data = await fetchData();

  if (data.length == 0) {
    window.confirm("장바구니가 비어있습니다!");
    window.location.href = '/home';
  }

  let i = 0;

  while (i < data.length) {

    const list = [];
    const item = data[i];

    const card = document.createElement('div');
    const title = document.createElement('h5');
    const price = document.createElement('p');
    const checkBox = document.createElement('input');
    const deleteButton = document.createElement('button');
    deleteButton.innerText = "제품 삭제";

    title.textContent = item.productName;
    price.textContent = item.price;
    checkBox.setAttribute('type', 'checkbox')
    checkBox.setAttribute('name', 'to-delete')
    checkBox.setAttribute('value', item.id);
    checkBox.setAttribute('class', "check");
    checkBox.addEventListener('change', function () {
      if (this.checked) {
        result.push(item.id)
      } else {
        result.splice(result.indexOf(item.id), 1);
      }
    })

    deleteButton.addEventListener("click", async function () {
      if (window.confirm("제품을 구매 리스트에서 삭제할까요?")) {
        try {
          list.push(item.id);
          await deleteData(list);
        } catch (error) {
          window.alert("다음 오류가 발생했습니다. :", error);
        }
        location.reload();
      }

    });

    card.appendChild(title);
    card.appendChild(price);
    card.appendChild(checkBox);
    card.appendChild(deleteButton);

    orderList.appendChild(card);

    i++;
  }

  document.querySelector('#delete-all').addEventListener("click", deleteSelectedData);
}

// 선택된 제품들 모두 삭제
async function deleteSelectedData() {
  try {
    if (window.confirm("선택된 제품들을 모두 삭제할까요?") == true) {
      await deleteData(result);
    }
  } catch (error) {
    window.alert("다음 오류가 발생했습니다. :", error);
  }
  location.reload();
}

// 제품 삭제 기능
async function deleteData(list) {
  await fetch(`http://34.64.249.228:8080/carts/user/${sessionUser}/items`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(list)
  })
}


// Daum 주소 API (사용 설명 https://postcode.map.daum.net/guide)
function searchAddress() {
  new daum.Postcode({
    oncomplete: function (data) {
      let addr = "";
      let extraAddr = "";

      if (data.userSelectedType === "R") {
        addr = data.roadAddress;
      } else {
        addr = data.jibunAddress;
      }

      if (data.userSelectedType === "R") {
        if (data.bname !== "" && /[동|로|가]$/g.test(data.bname)) {
          extraAddr += data.bname;
        }
        if (data.buildingName !== "" && data.apartment === "Y") {
          extraAddr +=
            extraAddr !== "" ? ", " + data.buildingName : data.buildingName;
        }
        if (extraAddr !== "") {
          extraAddr = " (" + extraAddr + ")";
        }
      } else {
      }

      postalCodeInput.value = data.zonecode;
      address1Input.value = `${addr} ${extraAddr}`;
      address2Input.placeholder = "상세 주소를 입력해 주세요.";
      address2Input.focus();
    },
  }).open();
}

// 페이지 로드 시 실행되며, 결제정보 카드에 값을 삽입함.
async function insertOrderSummary() {
  const data = await fetchData();

  let productsTitle = '';
  let productsTotal = 0;

  data.forEach(item => {
    productsTitle += (item.productName + "\n");
    productsTotal += (item.price * item.amount);
    console.log(productsTotal);
  });

  productsTitleElem.innerText = productsTitle;
  productsTotalElem.innerText = `${addCommas(productsTotal)}원`;

  if (productsTotal < 50000) {
    deliveryFeeElem.innerText = `3,000원`;
    orderTotalElem.innerText = `${addCommas(productsTotal + 3000)}원`;
    deliveryInfo.innerText = '50,000원 이상은 무료배송!';
  } else {
    deliveryFeeElem.innerText = `0원`;
    orderTotalElem.innerText = `${addCommas(productsTotal)}원`;
  }

  receiverNameInput.focus();
}

async function insertUserData() {
  const userData = await Api.get("/user");
  const { fullName, phoneNumber, address } = userData;

  // 만약 db에 데이터 값이 있었다면, 배송지정보에 삽입
  if (fullName) {
    receiverNameInput.value = fullName;
  }

  if (phoneNumber) {
    receiverPhoneNumberInput.value = phoneNumber;
  }

  if (address) {
    postalCode.value = address.postalCode;
    address1Input.value = address.address1;
    address2Input.value = address.address2;
  }
}

// "직접 입력" 선택 시 input칸 보이게 함
// default값(배송 시 요청사항을 선택해 주세여) 이외를 선택 시 글자가 진해지도록 함
function handleRequestChange(e) {
  const type = e.target.value;

  if (type === "6") {
    customRequestContainer.style.display = "flex";
    customRequestInput.focus();
  } else {
    customRequestContainer.style.display = "none";
  }

  if (type === "0") {
    requestSelectBox.style.color = "rgba(0, 0, 0, 0.3)";
  } else {
    requestSelectBox.style.color = "rgba(0, 0, 0, 1)";
  }
}

// 결제 진행
async function doCheckout() {
  const receiverName = receiverNameInput.value;
  const receiverPhoneNumber = receiverPhoneNumberInput.value;
  const postalCode = postalCodeInput.value;
  const address1 = address1Input.value;
  const address2 = address2Input.value;
  const requestType = requestSelectBox.value;
  const customRequest = customRequestInput.value;
  // const summaryTitle = productsTitleElem.innerText;
  const totalPrice = convertToNumber(orderTotalElem.innerText);
  const selectedItems = await fetchData(`http://34.64.249.228:8080/carts/user/${sessionUser}/items`);

  if (!receiverName || !receiverPhoneNumber || !postalCode || !address2) {
    return alert("배송지 정보를 모두 입력해 주세요.");
  }

  // 요청사항의 종류에 따라 request 문구가 달라짐
  let request;
  if (requestType === "0") {
    request = "요청사항 없음.";
  } else if (requestType === "6") {
    if (!customRequest) {
      return alert("요청사항을 작성해 주세요.");
    }
    request = customRequest;
  } else {
    request = requestOption[requestType];
  }

  const address = address1 + " " + address2;

  try {
    // 전체 주문을 등록함
    const order = await fetch("http://34.64.249.228:8080/orders", {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        "userId": 1,
        "receiver": receiverName,
        "address": address,
        "request": request,
        "postalCode": postalCode,
        "totalCost": totalPrice
      })
    });

    order.json().then(async function (result) {
      for (const product of selectedItems) {

        const { productId, productName, amount, price } = product;

        console.log(productId, amount, price);

        await fetch(`http://34.64.249.228:8080/orders/${result.id}/details`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            "productId": productId,
            "productName": productName,
            "quantity": amount,
            "price": totalPrice
          })
        });
      }
    })

    await fetch(`http://34.64.249.228:8080/carts/user/${sessionUser}`, {
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' }
    })

    alert("결제 및 주문이 정상적으로 완료되었습니다.\n감사합니다.");
    document.location.href = "/order-complete";
  } catch (err) {
    console.log(err);
    alert(`결제 중 문제가 발생하였습니다: ${err.message}`);
  }
}
