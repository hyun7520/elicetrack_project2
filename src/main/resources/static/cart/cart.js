import { getImageUrl } from "../aws-s3.js";
import {
  addCommas,
  convertToNumber,
  navigate,
  compressString,
  createNavbar,
  checkLogin
} from "../useful-functions.js";
import { deleteFromDb, getFromDb, putToDb } from "../indexed-db.js";

// 요소(element), input 혹은 상수
const cartProductsContainer = document.querySelector("#cartProductsContainer");
const allSelectCheckbox = document.querySelector("#allSelectCheckbox");
const partialDeleteLabel = document.querySelector("#partialDeleteLabel");
const productsCountElem = document.querySelector("#productsCount");
const productsTotalElem = document.querySelector("#productsTotal");
const deliveryFeeElem = document.querySelector("#deliveryFee");
const orderTotalElem = document.querySelector("#orderTotal");
const purchaseButton = document.querySelector("#purchaseButton");
const deliveryFeeWarn = document.querySelector("#deliveryFeeWarn");

const sessionUser = sessionStorage.getItem("id");
if (sessionUser == null) {
  window.alert("로그인 해주세요!");
  checkLogin();
}

let checked = [];
let finalPrice = 0;
let finalAmount = 0;

addAllElements();
addAllEvents();

// html에 요소를 추가하는 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllElements() {
  createNavbar();
  insertProductsfromCart();
  // insertOrderSummary();
  // updateAllSelectCheckbox();
}

// addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() {
  purchaseButton.addEventListener("click", navigate("/order"));
}

// indexedDB의 cart와 order에서 필요한 정보를 가져온 후
// 요소(컴포넌트)를 만들어 html에 삽입함.
async function insertProductsfromCart() {
  const response = await fetch(`http://34.64.249.228:8080/carts/user/${sessionUser}/items`);
  const products = await response.json();

  let i = 0;
  let finalAmount = 0;
  let finalPrice = 0

  if (products.length == 0) {
    window.confirm("장바구니가 비어있습니다!");
    window.location.href = '/home';
  }

  while (i < products.length) {

    const list = [];
    const product = products[i]

    // 객체 destructuring
    const { id, productName, price, brandName, productId } = product;
    let { amount } = product;
    // const imageUrl = await getImageUrl(imageKey);
    // const isSelected = selectedIds.includes(_id);

    cartProductsContainer.insertAdjacentHTML(
      "beforeend",
      `
        <div class="cart-product-item" id="productItem-${id}">
          <label class="checkbox">
            
          </label>
          <figure class="image is-96x96">
            <img
              id="image-${id}"
              alt="product-image"
            />
          </figure>
          <div class="content">
            <p id="title-${id}">${productName}</p>
            <div class="quantity">
              <button 
                class="button is-rounded" 
                id="minus-${id}" 
                ${amount <= 1 ? "disabled" : ""}
                
              >
                <span class="icon is-small">
                  <i class="fas fa-thin fa-minus"></i>
                </span>
              </button>
              <input
                class="input"
                id="quantityInput-${id}"
                type="number"
                min="1"
                max="99"
                value="${amount}"
                
              />
              <button 
                class="button is-rounded" 
                id="plus-${id}"
                ${amount >= 99 ? "disabled" : ""}
                
              >
                <span class="icon">
                  <i class="fas fa-lg fa-plus"></i>
                </span>
              </button>
            </div>
          </div>
          <div class="calculation">
            <p id="unitPrice-${id}">${addCommas(price)}원</p>
            <p>
              <span class="icon">
                <i class="fas fa-thin fa-xmark"></i>
              </span>
            </p>
            <p id="quantity-${id}">${amount}</p>
            <p>
              <span class="icon">
                <i class="fas fa-thin fa-equals"></i>
              </span>
            </p>
            <p id="total-${id}">${addCommas(amount * price)}원</p>
          </div>
        </div>
      `
    );

    // <input type="checkbox" class="product-checkbox" />

    const itemCheck = document.createElement('input');
    itemCheck.setAttribute('type', 'checkbox');
    itemCheck.setAttribute('value', id);
    itemCheck.setAttribute('name', 'item');
    itemCheck.setAttribute('class', 'product-checkbox');
    itemCheck.addEventListener("click", function () {
      if (this.checked) {
        checked.push(id);
      } else {
        checked = checked.filter(toDelete => toDelete != id)
      }
    })

    // < button class="delete-button" id = "delete" >
    //   <span class="icon">
    //     <i class="fas fa-trash-can"></i>
    //   </span>
    //     </button >

    const itemCheckbox = document.querySelector(`#productItem-${id}`);
    itemCheckbox.insertBefore(itemCheck, itemCheckbox.firstChild);

    const deleteOneButton = document.createElement('button');
    deleteOneButton.setAttribute('class', 'delete-button');
    deleteOneButton.setAttribute('id', 'delete');
    const deletespan = document.createElement('span');
    deletespan.setAttribute('class', 'icon');
    const deleteicon = document.createElement('i');
    deleteicon.setAttribute('class', 'fas fa-trash-can');

    const plusqButton = document.querySelector(`#plus-${id}`);
    const minusqButton = document.querySelector(`#minus-${id}`);
    let amountInput = document.querySelector(`#quantityInput-${id}`);
    let amountCalculate = document.querySelector(`#quantity-${id}`);
    let totalCal = document.querySelector(`#total-${id}`);
    finalAmount += convertToNumber(amountInput.value);
    finalPrice += convertToNumber(totalCal.innerHTML);

    const unit = "원";
    const deleiveryFee = "3000원"

    plusqButton.addEventListener('click', async function () {
      if (amount < 99) {
        if (minusqButton.hasAttribute("disabled")) {
          minusqButton.removeAttribute("disabled");
        }
        amount += 1;
        await updateQuantity(id, amount);
        amountInput.value = amountCalculate.innerHTML = amount;
        totalCal.innerHTML = `${addCommas(amount * price)}` + unit;
        finalAmount += 1;
        finalPrice += price;
        productsCountElem.innerText = finalAmount;
        productsTotalElem.innerText = `${addCommas(finalPrice)}` + unit;
        if (finalPrice < 50000) {
          deliveryFeeElem.innerText = deleiveryFee;
          deliveryFeeWarn.innerText = '50,000원 이상은 무료배송!';
        } else {
          deliveryFeeElem.innerText = "0원";
          deliveryFeeWarn.innerText = '';
        }
      }
      if (amount == 99) {
        plusqButton.setAttribute("disabled", "");
      }
    })

    minusqButton.addEventListener('click', async function () {
      if (amount >= 2) {
        if (plusqButton.hasAttribute("disabled")) {
          plusqButton.removeAttribute("disabled");
        }
        amount -= 1;
        await updateQuantity(id, amount);
        amountInput.value = amountCalculate.innerHTML = amount;
        totalCal.innerHTML = `${addCommas(amount * price)}` + unit;
        finalAmount -= 1;
        finalPrice -= price;
        productsCountElem.innerText = finalAmount;
        productsTotalElem.innerText = `${addCommas(finalPrice)}` + unit;
        if (finalPrice < 50000) {
          deliveryFeeElem.innerText = deleiveryFee;
          deliveryFeeWarn.innerText = '50,000원 이상은 무료배송!';
        } else {
          deliveryFeeElem.innerText = "0원";
          deliveryFeeWarn.innerText = '';
        }
      }
      if (amount == 1) {
        minusqButton.setAttribute("disabled", "");
      }
    })

    deletespan.appendChild(deleteicon);
    deleteOneButton.appendChild(deletespan);

    deleteOneButton.addEventListener("click", async function () {
      if (window.confirm("해당 제품을 장바구니에서 삭제할까요?")) {
        try {
          list.push(id);
          await deleteData(list);
          location.reload();
        } catch (error) {
          window.alert("다음 오류가 발생했습니다. : ", error)
        }
      }

    })

    itemCheckbox.insertBefore(deleteOneButton, itemCheckbox.lastChild);

    i++;
    // 각종 이벤트 추가
    // document
    //   .querySelector(`#delete`)
    //   .addEventListener("click", () => deleteItem(_id));

    // document
    //   .querySelector(`#checkbox-${_id}`)
    //   .addEventListener("change", () => toggleItem(_id));

    // document
    //   .querySelector(`#image-${_id}`)
    //   .addEventListener("click", navigate(`/product/detail?id=${_id}`));

    // document
    //   .querySelector(`#title-${_id}`)
    //   .addEventListener("click", navigate(`/product/detail?id=${_id}`));

    // document
    //   .querySelector(`#plus-${id}`)
    //   .addEventListener("click", () => increaseItemQuantity(id));

    // document
    //   .querySelector(`#minus-${_id}`)
    //   .addEventListener("click", () => decreaseItemQuantity(_id));

    // document
    //   .querySelector(`#quantityInput-${_id}`)
    //   .addEventListener("change", () => handleQuantityInput(_id));
  };

  productsCountElem.innerText = finalAmount;
  productsTotalElem.innerText = `${addCommas(finalPrice)}` + '원';

  if (finalPrice < 50000) {
    deliveryFeeElem.innerText = deleiveryFee;
  } else {
    deliveryFeeElem.innerText = "0원";
  }


  allSelectCheckbox.addEventListener('click', function selectAll() {
    let itemboxes = document.getElementsByName('item');
    if (allSelectCheckbox.checked) {
      for (let i = 0; i < itemboxes.length; i++) {
        itemboxes[i].checked = true;
        checked.push(itemboxes[i].value)
      }
    } else {
      for (let i = 0; i < itemboxes.length; i++) {
        itemboxes[i].checked = false;
      }
      checked.splice(0, checked.length);
    }
  })

  updateOrderSummary();

  document.querySelector('#partialDeleteLabel').addEventListener("click", deleteSelectedData);
}

// 선택된 제품들 모두 삭제
async function deleteSelectedData() {
  if (checked.length > 0) {
    try {
      if (window.confirm("선택된 제품들을 모두 삭제할까요?") == true) {
        await deleteData(checked);
        location.reload();
      }
    } catch (error) {
      window.alert("다음 오류가 발생했습니다. :", error);
    }
  }
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

async function updateQuantity(cartId, amount) {
  const response = await fetch(`http:/34.64.249.228:8080/carts/user/${sessionUser}/items/${cartId}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ "quantity": amount })
  });
}

async function toggleItem(id) {
  const itemCheckbox = document.querySelector(`#checkbox-${id}`);
  const isChecked = itemCheckbox.checked;

  // 결제정보 업데이트 및, 체크 상태에서는 수량을 수정 가능 (언체크는 불가능)으로 함
  if (isChecked) {
    await updateOrderSummary(id, "add-checkbox");
    setQuantityBox(id, "able");
  } else {
    await updateOrderSummary(id, "removeTemp-checkbox");
    setQuantityBox(id, "disable");
  }
}

async function toggleAll(e) {
  // 전체 체크냐 전체 체크 해제이냐로 true 혹은 false
  const isCheckAll = e.target.checked;
  const { ids } = await getFromDb("order", "summary");

  ids.forEach(async (id) => {
    const itemCheckbox = document.querySelector(`#checkbox-${id}`);
    const isItemCurrentlyChecked = itemCheckbox.checked;

    // 일단 아이템(제품) 체크박스에 전체 체크 혹은 언체크 여부를 반영함.
    itemCheckbox.checked = isCheckAll;

    // 결제정보 업데이트 필요 여부 확인
    const isAddRequired = isCheckAll && !isItemCurrentlyChecked;
    const isRemoveRequired = !isCheckAll && isItemCurrentlyChecked;

    // 결제정보 업데이트 및, 체크 상태에서는 수정 가능으로 함
    if (isAddRequired) {
      updateOrderSummary(id, "add-checkbox");
      setQuantityBox(id, "able");
    }

    // 결제정보 업데이트 및, 언체크 상태에서는 수정 불가능으로 함
    if (isRemoveRequired) {
      updateOrderSummary(id, "removeTemp-checkbox");
      setQuantityBox(id, "disable");
    }
  });
}

async function increaseItemQuantity(id) {
  // 결제정보카드 업데이트
  await updateOrderSummary(id, "add-plusButton");

  // 제품아이템카드 업데이트
  await updateProductItem(id, "increase");

  // indexedDB의 cart 데이터 업데이트
  await putToDb("cart", id, (data) => {
    data.quantity = data.quantity + 1;
  });

  // 수량 변경박스(-버튼, 입력칸, +버튼) 상태 업데이트
  setQuantityBox(id, "plus");
}

async function decreaseItemQuantity(id) {
  // 결제정보카드 업데이트
  await updateOrderSummary(id, "minusButton");

  // 제품아이템카드 업데이트
  await updateProductItem(id, "decrease");

  // indexedDB의 cart 데이터 업데이트
  await putToDb("cart", id, (data) => {
    data.quantity = data.quantity - 1;
  });

  // 수량 변경박스(-버튼, 입력칸, +버튼) 상태 업데이트
  setQuantityBox(id, "minus");
}

async function handleQuantityInput(id) {
  // 우선 입력값이 범위 1~99 인지 확인
  const inputElem = document.querySelector(`#quantityInput-${id}`);
  const quantity = parseInt(inputElem.value);

  if (quantity < 1 || quantity > 99) {
    return alert("수량은 1~99 사이가 가능합니다.");
  }

  // 결제정보카드 업데이트
  await updateOrderSummary(id, "add-input");

  // 제품아이템카드 업데이트
  await updateProductItem(id, "input");

  // indexedDB의 cart 데이터 업데이트
  await putToDb("cart", id, (data) => {
    data.quantity = quantity;
  });

  // 수량 변경박스(-버튼, 입력칸, +버튼) 상태 업데이트
  setQuantityBox(id, "input");
}

// -버튼, 숫자입력칸, +버튼 활성화 여부 및 값을 세팅함.
// function setQuantityBox(id, type) {
//   // 세팅 방식 결정을 위한 변수들
//   const isPlus = type.includes("plus");
//   const isMinus = type.includes("minus");
//   const isInput = type.includes("input");
//   const isDisableAll = type.includes("disable");

//   // 세팅을 위한 요소들
//   const minusButton = document.querySelector(`#minus-${id}`);
//   const quantityInput = document.querySelector(`#quantityInput-${id}`);
//   const plusButton = document.querySelector(`#plus-${id}`);

//   // 우선 기본적으로 활성화시킴
//   minusButton.removeAttribute("disabled");
//   quantityInput.removeAttribute("disabled");
//   plusButton.removeAttribute("disabled");

//   // 전체 비활성화 시키는 타입일 경우 (제품 체크를 해제했을 때 등)
//   if (isDisableAll) {
//     minusButton.setAttribute("disabled", "");
//     quantityInput.setAttribute("disabled", "");
//     plusButton.setAttribute("disabled", "");
//     return;
//   }

//   // input칸 값을 업데이트하기 위한 변수 설정
//   let quantityUpdate;
//   if (isPlus) {
//     quantityUpdate = +1;
//   } else if (isMinus) {
//     quantityUpdate = -1;
//   } else if (isInput) {
//     quantityUpdate = 0;
//   } else {
//     quantityUpdate = 0;
//   }

//   // input칸 값 업데이트
//   const currentQuantity = parseInt(quantityInput.value);
//   const newQuantity = currentQuantity + quantityUpdate;
//   quantityInput.value = newQuantity;

//   // 숫자는 1~99만 가능
//   const isMin = newQuantity === 1;
//   const isMax = newQuantity === 99;

//   if (isMin) {
//     minusButton.setAttribute("disabled", "");
//   }

//   if (isMax) {
//     plusButton.setAttribute("disabled", "");
//   }
// }

// // async function deleteSelectedItems() {
// //   const { selectedIds } = await getFromDb("order", "summary");

// //   selectedIds.forEach((id) => deleteItem(id));
// // }

// // 전체선택 체크박스를, 현재 상황에 맞추어
// // 체크 또는 언체크 상태로 만듦
// async function updateAllSelectCheckbox() {
//   const { ids, selectedIds } = await getFromDb("order", "summary");

//   const isOrderEmpty = ids.length === 0;
//   const isAllItemSelected = ids.length === selectedIds.length;

//   // 장바구니 아이템(제품) 수가 0이 아니고,
//   // 또 전체 아이템들이 선택된 상태라면 체크함.
//   if (!isOrderEmpty && isAllItemSelected) {
//     allSelectCheckbox.checked = true;
//   } else {
//     allSelectCheckbox.checked = false;
//   }
// }

async function deleteItem(id) {
  // indexedDB의 cart 목록에서 id를 key로 가지는 데이터를 삭제함.
  await deleteFromDb("cart", id);

  // 결제정보를 업데이트함.
  await updateOrderSummary(id, "removePermanent-deleteButton");

  // 제품 요소(컴포넌트)를 페이지에서 제거함
  document.querySelector(`#productItem-${id}`).remove();

  // 전체선택 체크박스를 업데이트함
  updateAllSelectCheckbox();
}

// 결제정보 카드 업데이트 및, indexedDB 업데이트를 진행함.
async function updateOrderSummary() {
  // 업데이트 방식 결정을 위한 변수들
  // const isCheckbox = type.includes("checkbox");
  // const isInput = type.includes("input");
  // const isDeleteButton = type.includes("deleteButton");
  // const isMinusButton = type.includes("minusButton");
  // const isPlusButton = type.includes("plusButton");
  // const isAdd = type.includes("add");
  // const isRemoveTemp = type.includes("removeTemp");
  // const isRemovePermanent = type.includes("removePermanent");
  // const isRemove = isRemoveTemp || isRemovePermanent;
  // // const isItemChecked = document.querySelector(`#checkbox-${id}`).checked;
  // const isDeleteWithoutChecked = isDeleteButton && !isItemChecked;


  // 체크박스 혹은 삭제 버튼 클릭으로 인한 업데이트임.
  // if (isCheckbox || isDeleteButton) {
  //   const priceElem = document.querySelector(`#total-${id}`);
  //   price = convertToNumber(priceElem.innerText);

  //   quantity = 1;
  // }

  // // - + 버튼 클릭으로 인한 업데이트임.
  // if (isMinusButton || isPlusButton) {
  //   const unitPriceElem = document.querySelector(`#unitPrice-${id}`);
  //   price = convertToNumber(unitPriceElem.innerText);

  //   quantity = 0;
  // }

  // // input 박스 입력으로 인한 업데이트임
  // if (isInput) {
  //   const unitPriceElem = document.querySelector(`#unitPrice-${id}`);
  //   const unitPrice = convertToNumber(unitPriceElem.innerText);

  //   const inputElem = document.querySelector(`#quantityInput-${id}`);
  //   const inputQuantity = convertToNumber(inputElem.value);

  //   const quantityElem = document.querySelector(`#quantity-${id}`);
  //   const currentQuantity = convertToNumber(quantityElem.innerText);

  //   price = unitPrice * (inputQuantity - currentQuantity);

  //   quantity = 0;
  // }

  // // 업데이트 방식
  // const priceUpdate = isAdd ? +price : -price;
  // const countUpdate = isAdd ? +quantity : -quantity;

  // // 현재 결제정보의 값들을 가져오고 숫자로 바꿈.
  // const currentCount = convertToNumber(productsCountElem.innerText);
  // const currentProductsTotal = convertToNumber(productsTotalElem.innerText);
  // const currentFee = convertToNumber(deliveryFeeElem.innerText);
  // const currentOrderTotal = convertToNumber(orderTotalElem.innerText);

  // // 결제정보 관련 요소들 업데이트
  // if (!isDeleteWithoutChecked) {
  //   productsCountElem.innerText = `${currentCount + countUpdate}개`;
  //   productsTotalElem.innerText = `${addCommas(
  //     currentProductsTotal + priceUpdate
  //   )}원`;
  // }

  // // 기존 결제정보가 비어있었어서, 배송비 또한 0인 상태였던 경우
  // const isFeeAddRequired = isAdd && currentFee === 0;

  // if (isFeeAddRequired) {
  //   deliveryFeeElem.innerText = `3000원`;
  //   orderTotalElem.innerText = `${addCommas(
  //     currentOrderTotal + priceUpdate + 3000
  //   )}원`;
  // }

  // if (!isFeeAddRequired && !isDeleteWithoutChecked) {
  //   orderTotalElem.innerText = `${addCommas(
  //     currentOrderTotal + priceUpdate
  //   )}원`;
  // }

  // // 이 업데이트로 인해 결제정보가 비게 되는 경우
  // const isCartNowEmpty = currentCount === 1 && isRemove;

  // if (!isDeleteWithoutChecked && isCartNowEmpty) {
  //   deliveryFeeElem.innerText = `0원`;

  //   // 다시 한 번, 현재 값을 가져와서 3000을 빼 줌
  //   const currentOrderTotal = convertToNumber(orderTotalElem.innerText);
  //   orderTotalElem.innerText = `${addCommas(currentOrderTotal - 3000)}원`;

  //   // 전체선택도 언체크되도록 함.
  //   updateAllSelectCheckbox();
  // }

  // // indexedDB의 order.summary 업데이트
  // await putToDb("order", "summary", (data) => {
  //   const hasId = data.selectedIds.includes(id);

  //   if (isAdd && !hasId) {
  //     data.selectedIds.push(id);
  //   }

  //   if (isRemoveTemp) {
  //     data.selectedIds = data.selectedIds.filter((_id) => _id !== id);
  //   }

  //   if (isRemovePermanent) {
  //     data.ids = data.ids.filter((_id) => _id !== id);
  //     data.selectedIds = data.selectedIds.filter((_id) => _id !== id);
  //   }

  //   if (!isDeleteWithoutChecked) {
  //     data.productsCount += countUpdate;
  //     data.productsTotal += priceUpdate;
  //   }
  // });

  // // 전체선택 체크박스 업데이트
  // updateAllSelectCheckbox();
}

// 아이템(제품)카드의 수량, 금액 등을 업데이트함
async function updateProductItem(id, type) {
  // 업데이트 방식을 결정하는 변수들
  const isInput = type.includes("input");
  const isIncrease = type.includes("increase");

  // 업데이트에 필요한 요소 및 값들을 가져오고 숫자로 바꿈.
  const unitPriceElem = document.querySelector(`#unitPrice-${id}`);
  const unitPrice = convertToNumber(unitPriceElem.innerText);

  const quantityElem = document.querySelector(`#quantity-${id}`);
  const currentQuantity = convertToNumber(quantityElem.innerText);

  const totalElem = document.querySelector(`#total-${id}`);
  const currentTotal = convertToNumber(totalElem.innerText);

  const inputElem = document.querySelector(`#quantityInput-${id}`);
  const inputQuantity = convertToNumber(inputElem.value);

  // 업데이트 진행
  if (isInput) {
    quantityElem.innerText = `${inputQuantity}개`;
    totalElem.innerText = `${addCommas(unitPrice * inputQuantity)}원`;
    return;
  }

  const quantityUpdate = isIncrease ? +1 : -1;
  const priceUpdate = isIncrease ? +unitPrice : -unitPrice;

  quantityElem.innerText = `${currentQuantity + quantityUpdate}개`;
  totalElem.innerText = `${addCommas(currentTotal + priceUpdate)}원`;
}

// 페이지 로드 시 실행되며, 결제정보 카드에 값을 삽입함.
async function insertOrderSummary() {
  const { productsCount, productsTotal } = await getFromDb("order", "summary");

  const hasItems = productsCount !== 0;

  productsCountElem.innerText = `${productsCount}개`;
  productsTotalElem.innerText = `${addCommas(productsTotal)}원`;

  if (hasItems) {
    deliveryFeeElem.innerText = `3,000원`;
    orderTotalElem.innerText = `${addCommas(productsTotal + 3000)}원`;
  } else {
    deliveryFeeElem.innerText = `0원`;
    orderTotalElem.innerText = `0원`;
  }
}
