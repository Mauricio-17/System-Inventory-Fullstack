<template>
    <a-button v-if="!objectToUpdate" type="primary" @click="showDrawer">Registrar </a-button>
    <a-button v-else @click="showDrawer" type="primary">
        <edit-outlined />
    </a-button>

    <a-drawer v-model:visible="visible" class="custom-class" style="color: red" title="CATEGORÍA" placement="right"
        @after-visible-change="afterVisibleChange">
        <a-form :model="object" name="basic" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }" autocomplete="off"     :validate-messages="validateMessages">
            <a-form-item label="NOMBRE" name="name" :rules="[{ required: true, message: 'Porfavor, ingrese el nombre' }]">
                <a-input v-model:value="object.name" />
            </a-form-item>
            <a-form-item label="DESCRIPCIÓN" name="description">
                <a-textarea v-model:value="object.description" />
            </a-form-item>

            <a-form-item :wrapper-col="{ span: 14, offset: 4 }">
                <a-button v-if="!objectToUpdate" type="primary" @click="submitObject">Registrar</a-button>
                <a-button v-else type="primary" @click="editObject">Editar</a-button>
                <a-button style="margin-left: 10px" @click="hideDrawer">Cancelar</a-button>
            </a-form-item>
        </a-form>

    </a-drawer>

</template>
<script setup>
import { ref, reactive, onMounted } from 'vue';
import { EditOutlined } from '@ant-design/icons-vue';
import { addNewCategory, updateCategory } from '../composables/Category';
import { successNotification, errorNotification } from '../composables/Notification';

const emit = defineEmits(['update-list']);

const visible = ref(false);

const props = defineProps({
    record: {
        type: Object,
        default: null
    },
});

const object = reactive({
    name: '',
    description: ''
});
const objectToUpdate = ref(props.record);

const submitObject = async () => {
    const item = { ...object }; // this is to prevent reactivity
    try {
        let result = await addNewCategory(item);
        successNotification("Registro exitoso", `La categoría ${item.name.toUpperCase()} ha sido registrado.`);
        emit('update-list');
    } catch (e) {
        if (e.response) {
            const data = await e.response.json();
            if (data.errors){
                errorNotification("Ocurrió un error :(", data.errors[0].defaultMessage);
                return;
            }
            errorNotification("Ocurrió un error :(", data.message);
        }
    } finally {
        hideDrawer();
        cleanObject();
    }

};
const editObject = async () => {
    const item = { ...object }; // this is to prevent reactivity
    try {
        const result = await updateCategory(item, objectToUpdate.value.id);
        successNotification("Edición exitosa", `Se actualizaron los datos de ${item.name.toUpperCase()}.`);
        cleanObject();
        emit('update-list');
    } catch (e) {
        if (e.response) {
            const data = await e.response.json();
            if (data.errors){
                errorNotification("Ocurrió un error :(", data.errors[0].defaultMessage);
                return;
            }
            errorNotification("Ocurrió un error :(", data.message);
        }
    } finally {
        hideDrawer();
    }
};

const afterVisibleChange = (bool) => {
    console.log('visible', bool);
};
const showDrawer = () => {
    visible.value = true;
};
const hideDrawer = () => {
    visible.value = false;
};

const validateMessages = {
    required: '${label} is required!',
    types: {
        email: '${label} no es un email válido!',
        number: '${label} no es un número válido!',
    },
    number: {
        range: '${label} must be between ${min} and ${max}',
    },
};

const cleanObject = () => {
    object.name = '';
    object.description = '';
}

onMounted(async () => {
    if (objectToUpdate.value !== null) {
        object.name = objectToUpdate.value.name;
        object.description = objectToUpdate.value.description;
    }
});

</script>

