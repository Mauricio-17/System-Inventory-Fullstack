import fetch from "unfetch";

const URL = "/api/admin/type";

const getHeaders = () => {
  return {
    Accept: "application/json",
    "Content-Type": "application/json",
    Authorization: `Bearer ${localStorage.getItem("token")}`
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

export const getAllTypes = () =>
  fetch(`/api/type`, {
    headers: getHeaders(),
    method: "GET",
  }).then(checkStatus);

export const addNewType = (type) =>
  fetch(URL, {
    headers: getHeaders(),
    method: "POST",
    body: JSON.stringify(type),
  }).then(checkStatus);

export const updateType = (type, typeId) =>
  fetch(`${URL}/${typeId}`, {
    headers: getHeaders(),
    method: "PUT",
    body: JSON.stringify(type),
  }).then(checkStatus);

export const removeType = (roleId) =>
  fetch(`${URL}/${roleId}`, {
    headers: getHeaders(),
    method: "DELETE",
  }).then(checkStatus);
