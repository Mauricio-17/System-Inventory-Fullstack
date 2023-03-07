<template>
  <a-button v-if="!objectToUpdate" type="primary" @click="showDrawer">{{
    name
  }}</a-button>
  <a-button v-else @click="showDrawer" type="primary">
    <edit-outlined />
  </a-button>

  <a-drawer
    v-model:visible="visible"
    class="custom-class"
    style="color: red"
    title="EMPLEADO"
    placement="right"
    @after-visible-change="afterVisibleChange"
  >
    <a-form
      :model="object"
      name="basic"
      :label-col="{ span: 8 }"
      :wrapper-col="{ span: 16 }"
      autocomplete="off"
      :validate-messages="validateMessages"
    >
      <a-form-item
        label="NOMBRE"
        name="name"
        :rules="[{ required: true, message: 'Porfavor, ingrese su nombre' }]"
      >
        <a-input v-model:value="object.name" />
      </a-form-item>
      <a-form-item label="APELLIDO" name="lastName">
        <a-input v-model:value="object.lastName" />
      </a-form-item>
      <a-form-item name="email" label="EMAIL" :rules="[{ type: 'email' }]">
        <a-input :disabled="objectToUpdate ? true : false" v-model:value="object.email" />
      </a-form-item>

      <a-form-item label="ESTADO" name="status">
        <a-select
          label="ESTADO"
          ref="select"
          v-model:value="object.status"
          style="width: 150px"
        >
          <a-select-option value="ENABLE">HABILITADO</a-select-option>
          <a-select-option value="UNABLE">INHABILITADO</a-select-option>
          <a-select-option value="UNDEFINED">INDEFINIDO</a-select-option>
          <a-select-option value="FIRED">DESPEDIDO</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="ROL" name="role">
        <a-select
          label="ROL"
          ref="select"
          v-model:value="object.role.id"
          style="width: 150px"
        >
          <a-select-option v-for="role in roles" :index="role.id" :value="role.id">
            {{ role.name }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item :wrapper-col="{ span: 14, offset: 4 }">
        <a-button v-if="!objectToUpdate" type="primary" @click="submitObject"
          >Registrar</a-button
        >
        <a-button v-else type="primary" @click="editObject">Editar</a-button>
        <a-button style="margin-left: 10px" @click="hideDrawer">Cancelar</a-button>
      </a-form-item>
    </a-form>
  </a-drawer>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import { EditOutlined } from "@ant-design/icons-vue";
import { updateEmployee, addNewEmployee } from "../composables/Employee";
import { successNotification, errorNotification } from "../composables/Notification";

const emit = defineEmits(["update-list"]);

const props = defineProps({
  record: {
    type: Object,
    default: null,
  },
  name: { type: String },
});

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
const translation = {
  HABILITADO: "ENABLE",
  INHABILITADO: "UNABLE",
  INDEFINIDO: "UNDEFINED",
  DESPEDIDO: "FIRED",
};

const visible = ref(false);

const objectToUpdate = ref(props.record);
// Aparentemente no funciona con arrays
const roleList = ref(props.roles);

const object = reactive({
  name: "",
  lastName: "",
  status: "",
  email: "",
});

const submitObject = async () => {
  const item = { ...object }; // this is to prevent reactivity
  try {
    await addNewEmployee(item);
    successNotification(
      "Registro exitoso",
      `El empleado ${item.name.toUpperCase()} ha sido registrado.`
    );
    emit("update-list");
  } catch (e) {
    if (e.response) {
      const data = await e.response.json();
      if (data.errors) {
        errorNotification("Ocurrió un error :(", data.errors[0].defaultMessage);
        return;
      }
      errorNotification("Ocurrió un error al registrar:(", data.message);
    }
  } finally {
    hideDrawer();
    cleanObject();
  }
};

const editObject = async () => {
  const item = { ...object }; // this is to prevent reactivity
  try {
    await updateEmployee(item, objectToUpdate.value.employeeId);
    successNotification(
      "Edición exitosa",
      `Se actualizaron los datos de ${item.name.toUpperCase()}.`
    );
  } catch (e) {
    if (e.response) {
      const data = await e.response.json();
      if (data.errors) {
        errorNotification("Ocurrió un error al editar:(", data.errors[0].defaultMessage);
        return;
      }
      errorNotification("Ocurrió un error :(", data.message);
    }
  } finally {
    hideDrawer();
    emit("update-list");
  }
};

const afterVisibleChange = (bool) => {
  console.log("visible", bool);
};
const showDrawer = () => {
  visible.value = true;
};
const hideDrawer = () => {
  visible.value = false;
};
const cleanObject = () => {
  object.name = "";
  object.lastName = "";
  object.status = "";
  object.email = "";
  object.password = "";
  object.role.id = null;
};

onMounted(async () => {
  if (objectToUpdate.value !== null) {
    object.name = objectToUpdate.value.name;
    object.lastName = objectToUpdate.value.lastName;
    object.status = translation[objectToUpdate.value.status];
    object.email = objectToUpdate.value.email;
    object.role.id = objectToUpdate.value.roleId;
  }
});
</script>

<style scoped></style>
