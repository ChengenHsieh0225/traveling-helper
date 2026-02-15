import { debounce } from "../utils.js";

const API_BASE_URL = window.location.hostname === "localhost" || window.location.hostname === "127.0.0.1"
    ? "http://127.0.0.1:8000"
    : "https://traveling-helper.onrender.com";

export const CurrencyPageHandler = {

    elements: {},

    debouncedCompute: null,

    get debouncedCompute() {
        delete this.debouncedCompute;
        return this.debouncedCompute = debounce(this.compute.bind(this), 300);
    },

    init() {
        this.elements.inputAmount = document.getElementById('input-amount');
        this.elements.selectFromCcy = document.getElementById('select-from-ccy');
        this.elements.selectToCcy = document.getElementById('select-to-ccy');
        this.elements.pResult = document.getElementById('p-result');
    },

    _handleEvent(e) {
        const action = e.target.closest('[data-action]')?.dataset.action;
        if (action && this[action]) {
            this[action]();
        }
    },

    handleClick(e) { this._handleEvent(e); },
    handleChange(e) { this._handleEvent(e); },
    handleInput(e) { this._handleEvent(e); },

    exchange() {
        const { selectFromCcy, selectToCcy } = this.elements;

        const temp = selectFromCcy.selectedIndex;
        selectFromCcy.selectedIndex = selectToCcy.selectedIndex;
        selectToCcy.selectedIndex = temp;

        this.debouncedCompute();
    },

    compute() {
        const { inputAmount, selectFromCcy, selectToCcy, pResult } = this.elements;
        const amount = Number(inputAmount.value);
        const fromCcy = selectFromCcy.value;
        const toCcy = selectToCcy.value;

        const url = `${API_BASE_URL}/currency-exchange/latest?from_ccy=${fromCcy}&to_ccy=${toCcy}&amount=${amount}`

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error('fetch failed!!');
                }
                return response.json();
            })
            .then(data => {
                const convertedAmount = data.amount;
                pResult.innerText = convertedAmount;
                // console.log('rate: ', data.exchange_rate);
                // console.log('amount: ', data.amount);
            })
            .catch(error => {
                console.error('error: ', error);
            });
    },
}
