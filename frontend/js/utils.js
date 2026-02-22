export function debounce(callback, delay) {
    let timerID;

    return function debounceCallback(...args) {
        clearTimeout(timerID);
        timerID = setTimeout(() => {
            callback.apply(this, args)
        }, delay);
    };
}