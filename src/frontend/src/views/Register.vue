<template>
  <div class="mx-auto margin w-50">
    <a-form
      :model="formState"
      name="basic"
      :label-col="{ span: 8 }"
      :wrapper-col="{ span: 16 }"
      autocomplete="off"
      :validate-messages="validateMessages"
      class="row"
    >
      <a-form-item
        label="Nombre de usuario"
        name="username"
        class="mx-auto"
        :rules="[{ required: true, message: 'Porfavor, ingrese el nombre de usuario' }]"
      >
        <a-input v-model:value="formState.username" />
      </a-form-item>

      <a-form-item
        label="Contraseña"
        name="password"
        class="mx-auto"
        :rules="[{ required: true, message: 'Porfavor, ingrese su contraseña' }]"
      >
        <a-input-password v-model:value="formState.password" />
      </a-form-item>
      <a-form-item
        label="Repetir contraseña"
        name="password"
        class="mx-auto"
        :rules="[{ required: true, message: 'Porfavor, ingrese su contraseña' }]"
      >
        <a-input-password v-model:value="auxPassword" />
      </a-form-item>
      <a-form-item label="Tipo usuario" name="role">
        <a-select
          label="ROL"
          ref="select"
          v-model:value="formState.roleId"
          style="width: 150px"
        >
          <a-select-option v-for="role in roleList" :index="role.id" :value="role.id">
            {{ role.name }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item name="remember" :wrapper-col="{ offset: 8, span: 16 }">
        <a-checkbox v-model:checked="loggeable">Iniciar sesión</a-checkbox>
      </a-form-item>

      <a-form-item :wrapper-col="{ offset: 8, span: 16 }" class="mx-auto">
        <a-button type="primary" v-on:click="registerUser">Registrar</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script setup>
import { reactive, ref, onMounted } from "vue";
import { useUserStore } from "../stores/Users";
import { useRouter } from "vue-router";
import { login, register } from "../composables/Auth";
import { getAllRoles } from "../composables/Role";
import { errorNotification } from "../composables/Notification";

const userStore = useUserStore();

const router = useRouter();

const auxPassword = ref("");
const loggeable = ref(false);

const roleList = ref([]);

const formState = reactive({
  username: "",
  password: "",
  roleId: null,
});

const registerUser = async () => {
  const user = { ...formState };
  try {
    if (user.password === auxPassword.value) {
      await register(user);

      if (loggeable.value === true) {
        console.log(user);
        const result = await login(user);
        const data = await result.json();
        const token = data.accessToken;

        userStore.setJWT(token);
        userStore.setEmail(user.username);

        window.location.reload();
        // or router.push({ name: "home" });
      }
      clearLogin();
    } else {
      errorNotification("Ocurrió un error :(", "Ambas contraseñas no coinciden");
    }
  } catch (e) {
    if (e.response) {
      const data = await e.response.json();
      if (data.errors) {
        errorNotification("Ocurrió un error :(", data.errors[0].defaultMessage);
        return;
      }
      errorNotification("Ocurrió un error al iniciar sesión :(", data.message);
    }
  }
};

const clearLogin = () => {
  formState.username = "";
  formState.password = "";
  auxPassword.value = "";
  loggeable.value = false;
};

const validateMessages = {
  required: "${label} is required!",
  types: {
    email: "${label} no es un email válido!",
    number: "${label} no es un número válido!",
  },
  number: {
    range: "${label} must be between ${min} and ${max}",
  },
};

onMounted(async () => {
  const roles = await getAllRoles();
  const data = await roles.json();
  console.log(data);
  roleList.value = data;
});
</script>
