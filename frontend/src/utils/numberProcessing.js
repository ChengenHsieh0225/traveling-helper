export const isNumeric = (val) => {
  return val !== "" && val !== null && Number.isFinite(Number(val));
};
