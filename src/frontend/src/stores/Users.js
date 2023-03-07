import { computed, ref } from "vue";
import { defineStore } from "pinia";

export const useUserStore = defineStore("user", () => {
  const JWT = ref(null);

  const setJWT = (item) =>{
    JWT.value = item;
    if(item){
      localStorage.setItem("token", JWT.value);
    }
    else{
      localStorage.removeItem("token");
    }
  }

  const setEmail = (item) =>{
    localStorage.setItem("email", item);
  };

  const getRolePermissions = computed(() => {
    let roles = [];
    if(JWT.value){
      const parts = JWT.value.split('.');
      const payload = JSON.parse(window.atob(parts[1]));
      payload.authorities.forEach(item => {
        roles.push(item.authority);
      });
    }
    return roles;
  });

  const getRoleUsername = computed(() =>{
    if(JWT.value){
      const parts = JWT.value.split('.');
      const payload = JSON.parse(window.atob(parts[1]));
      return payload.sub;
    } 
    return null;
  });


  return { setJWT, getRolePermissions, getRoleUsername, setEmail, JWT };
});
