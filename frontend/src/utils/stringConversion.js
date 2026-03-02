// class-name -> className
export const kebabToCamel = (str) => {
  if (!str) return "";
  const parts = str.split("-").filter(Boolean);
  return (
    parts[0] +
    parts
      .slice(1)
      .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
      .join("")
  );
};

// class-name -> ClassName
export const kebabToPascal = (str) => {
  if (!str) return "";
  return str
    .split("-")
    .filter(Boolean)
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
    .join("");
};
