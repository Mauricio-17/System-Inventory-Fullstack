import fetch from "unfetch";

const URL = "/api/employee";

const getHeaders = ()=> {
  return {
    'Accept': "application/json",
    "Content-Type": "application/json",
    'Authorization': `Bearer ${localStorage.getItem("token")}`
  }
}

const checkStatus = (response) => {
  if (response.ok) {
    return response;
  }
  // convert non-2xx HTTP responses into errors:
  const error = new Error(response.statusText);
  error.response = response;
  return Promise.reject(error);
};

export const getAllEmployees = () => fetch(`${URL}`,{
  headers: getHeaders(),
  method: 'GET'
}).then(checkStatus);

export const addNewEmployee = (employee) =>
  fetch(URL, {
    headers: getHeaders(),
    method: "POST",
    body: JSON.stringify(employee),
  }).then(checkStatus);

export const updateEmployee = (employee, id) =>
  fetch(`${URL}/${id}`, {
    headers: getHeaders(),
    method: "PUT",
    body: JSON.stringify(employee),
  }).then(checkStatus);

export const removeEmployee = (employeeId) =>
  fetch(`${URL}/${employeeId}`, {
    headers: getHeaders(),
    method: "DELETE",
  }).then(checkStatus);
