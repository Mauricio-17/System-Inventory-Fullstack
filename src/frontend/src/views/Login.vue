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
      <a-form-item label="Email" name="email" class="mx-auto">
        <a-input v-model:value="formState.username" />
      </a-form-item>

      <a-form-item
        label="Password"
        name="password"
        class="mx-auto"
        :rules="[{ required: true, message: 'Porfavor, ingrese su contraseña' }]"
      >
        <a-input-password v-model:value="formState.password" />
      </a-form-item>

      <!-- <a-form-item name="remember" :wrapper-col="{ offset: 8, span: 16 }">
          <a-checkbox v-model:checked="formState.remember">Remember me</a-checkbox>
        </a-form-item> -->

      <a-form-item :wrapper-col="{ offset: 8, span: 16 }" class="mx-auto">
        <a-button type="primary" v-on:click="logIn">Ingresar</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script setup>
import { reactive, ref, onMounted } from "vue";
import { useUserStore } from "../stores/Users";
import { useRouter } from "vue-router";
import { login } from "../composables/Auth";
import { errorNotification } from "../composables/Notification";

const userStore = useUserStore();

const router = useRouter();

const formState = reactive({
  username: "",
  password: "",
});

const logIn = async () => {
  const user = { ...formState };
  try {
    const result = await login(user);
    const data = await result.json();
    const token = data.accessToken;

    router.push({ name: "home" });

    userStore.setJWT(token);
    userStore.setEmail(user.username);
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
  formState.remember = false;
};

const validateMessages = {
  required: "${label} is required!",
  types: {
    email: "${label} no es un email válido!",
    number: "${label} is not a valid number!",
  },
  number: {
    range: "${label} must be between ${min} and ${max}",
  },
};
</script>
