import fetch from "unfetch";

const URL = "/api/brand";

const getHeaders = () => {
  return {
    Accept: "application/json",
    "Content-Type": "application/json",
    Authorization: `Bearer ${localStorage.getItem("token")}`,
  };
};

const checkStatus = (response) => {
  if (response.ok) {
    return response;
  }
  // convert non-2xx HTTP responses into errors:
  const error = new Error(response.statusText);
  error.response = response;
  return Promise.reject(error);
};

export const getAllBrands = () =>
  fetch(`${URL}`, {
    headers: getHeaders(),
    method: "GET",
  }).then(checkStatus);

export const addNewBrand = (brand) =>
  fetch(URL, {
    headers: getHeaders(),
    method: "POST",
    body: JSON.stringify(brand),
  }).then(checkStatus);

export const updateBrand = (brand, id) =>
  fetch(`${URL}/${id}`, {
    headers: getHeaders(),
    method: "PUT",
    body: JSON.stringify(brand),
  }).then(checkStatus);

export const removeBrand = (brandId) =>
  fetch(`${URL}/${brandId}`, {
    headers: getHeaders(),
    method: "DELETE",
  }).then(checkStatus);