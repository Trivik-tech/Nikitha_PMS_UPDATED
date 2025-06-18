// src/utils/encryptUtils.js
export const encrypt = (text) => {
  return btoa(text); // base64 encode
};

export const decrypt = (encoded) => {
  try {
    return atob(encoded); // base64 decode
  } catch {
    return "";
  }
};
