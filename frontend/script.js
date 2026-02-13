// 1. 選取元素
const input_amount = document.getElementById('input-amount');
const p_result = document.getElementById('p-result');
const select_from_ccy = document.getElementById('select-from-ccy')
const select_to_ccy = document.getElementById('select-to-ccy');
const btn_exchange = document.getElementById('btn-exchange');

const API_BASE_URL = window.location.hostname === "localhost" || window.location.hostname === "127.0.0.1"
    ? "http://127.0.0.1:8000"
    : "https://traveling-helper.onrender.com";

// 2. 監聽點擊事件
btn_exchange.addEventListener('click', () => {
    // 3. 改變頁面互動

    const temp = select_from_ccy.selectedIndex;
    select_from_ccy.selectedIndex = select_to_ccy.selectedIndex;
    select_to_ccy.selectedIndex = temp;

    debouncedCompute();
});

select_from_ccy.addEventListener('change', () => {
    debouncedCompute();
});

select_to_ccy.addEventListener('change', () => {
    debouncedCompute();
});

const debouncedCompute = debounce(compute, 300);

input_amount.addEventListener('input', () => {
    debouncedCompute();
});

function compute() {
    const amount = Number(input_amount.value);
    const from_ccy = select_from_ccy.value;
    const to_ccy = select_to_ccy.value;

    const url = `${API_BASE_URL}/currency-exchange/latest?from_ccy=${from_ccy}&to_ccy=${to_ccy}&amount=${amount}`

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('fetch failed!!');
            }
            return response.json();
        })
        .then(data => {
            const converted_amount = data.amount;
            p_result.innerText = converted_amount;
            // console.log('rate: ', data.exchange_rate);
            // console.log('amount: ', data.amount);
        })
        .catch(error => {
            console.error('error: ', error);
        });
}

function debounce(callback, delay) {
    let timerID;

    return function debounceCallback(...args) {
        clearTimeout(timerID);
        timerID = setTimeout(() => {
            callback.apply(this, args)
        }, delay);
    };
}