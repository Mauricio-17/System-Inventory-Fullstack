<template>
  <a-button v-if="!objectToUpdate" type="primary" @click="showDrawer">REGISTRAR</a-button>
  <a-button v-else @click="showDrawer" type="primary">
    <edit-outlined />
  </a-button>

  <a-drawer
    v-model:visible="visible"
    class="custom-class"
    style="color: red"
    title="PROPIETARIO"
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
      <a-form-item label="Nº Celular" name="cellphoneNumber">
        <a-input v-model:value="object.cellphoneNumber" />
      </a-form-item>
      <a-form-item
        label="DNI"
        name="dni"
        :rules="[{ required: true, message: 'Porfavor, ingrese su Nº de DNI' }]"
      >
        <a-input v-model:value="object.dni" />
      </a-form-item>
      <a-form-item label="Tipo propietario" name="typeOwner">
        <a-select
          label="Tipo propietario"
          ref="select"
          v-model:value="object.typeOwner.id"
          style="width: 150px"
        >
          <a-select-option v-for="type in types" :index="type.id" :value="type.id">
            {{ type.name }}
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
import { updateOwner, addNewOwner } from "../composables/Owner";
import { successNotification, errorNotification } from "../composables/Notification";

const emit = defineEmits(["update-list"]);

const props = defineProps({
  record: {
    type: Object,
    default: null,
  },
  types: { type: Array, required: true },
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

const visible = ref(false);

const objectToUpdate = ref(props.record);

const object = reactive({
  name: "",
  lastName: "",
  email: "",
  cellphoneNumber: "",
  dni: "",
  typeOwner: { id: null },
});

const submitObject = async () => {
  const item = { ...object }; // this is to prevent reactivity
  try {
    let result = await addNewOwner(item);
    successNotification(
      "Registro exitoso",
      `El propietario ${item.name.toUpperCase()} ha sido registrado.`
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
    const result = await updateOwner(item, objectToUpdate.value.id);
    successNotification(
      "Edición exitosa",
      `Se actualizaron los datos de ${item.name.toUpperCase()}.`
    );
    cleanObject();
  } catch (e) {
    if (e.response) {
      const data = await e.response.json();
      if (data.errors) {
        errorNotification("Ocurrió un error :(", data.errors[0].defaultMessage);
        return;
      }
      errorNotification("Ocurrió un error al editar :(", data.message);
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
  object.email = "";
  object.cellphoneNumber = "";
  object.dni = "";
  object.typeOwner.id = null;
};

onMounted(async () => {
  if (objectToUpdate.value !== null) {
    object.name = objectToUpdate.value.name;
    object.lastName = objectToUpdate.value.lastName;
    object.email = objectToUpdate.value.email;
    object.cellphoneNumber = objectToUpdate.value.cellphoneNumber;
    object.dni = objectToUpdate.value.dni;
    object.typeOwner.id = objectToUpdate.value.typeOwner.id;
  }
});
</script>

<style scoped></style>
