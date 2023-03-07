<template>
    <a-button v-if=" !objectToUpdate" type="primary" @click="showDrawer">REGISTRAR</a-button>
    <a-button v-else @click="showDrawer" type="primary"> 
        <edit-outlined />
    </a-button>

    <a-drawer v-model:visible="visible" class="custom-class" style="color: red" title="EMPLEADO" placement="right"
        @after-visible-change="afterVisibleChange">

        <a-form :model="object" name="basic" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }" autocomplete="off"
            :validate-messages="validateMessages">
            <a-form-item label="FILA" name="row"
            :rules="[{ required: true, message: 'Porfavor, ingrese su nombre' }]">
                <a-input-number v-model:value="object.row" />
            </a-form-item>
            <a-form-item label="ESTADO" name="status">
                <a-select label="ESTADO" ref="select" v-model:value="object.status" style="width: 150px">
                    <a-select-option value="AVAILABLE">DISPONIBLE</a-select-option>
                    <a-select-option value="UNAVAILABLE">INDISPONIBLE</a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="Estante" name="shelf">
                <a-select label="Estante" ref="select" v-model:value="object.shelf.id" style="width: 150px">
                    <a-select-option v-for="shelf in shelves" :index="shelf.id" :value="shelf.id">
                        {{ shelf.name }}
                    </a-select-option>
                </a-select>
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
import {EditOutlined} from '@ant-design/icons-vue';
import { updateLocation, addNewLocation } from '../composables/Location';
import {successNotification, errorNotification} from '../composables/Notification';

const emit = defineEmits(['update-list']);

const props = defineProps({
    record: {
        type: Object,
        default: null
    },
    shelves: { type: Array, required: true },
});

const validateMessages = {
    required: '${label} es requerido!',
    types: {
        email: '${label} no es un email válido!',
        number: '${label} is not a valid number!',
    },
    number: {
        range: '${label} must be between ${min} and ${max}',
    },
};
const translation = {
  "DISPONIBLE": "AVAILABLE",
  "INDISPONIBLE": "UNAVAILABLE"
};

const visible = ref(false);

const objectToUpdate = ref(props.record);

const object = reactive({
    row: 0,
    status: '',
    shelf: { id: null },
});


const submitObject = async () => {
    const item = { ...object }; // this is to prevent reactivity
    try {
        let result = await addNewLocation(item);
        successNotification("Registro exitoso", `La fila ${item.row} ha sido registrada.`);
        emit('update-list');
    } catch (e) {
        if (e.response) {
            const data = await e.response.json();
            if (data.errors){
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
    item["id"] = objectToUpdate.value.id;
    console.log(item);
    try{
        const result = await updateLocation(item, objectToUpdate.value.id);
        successNotification("Edición exitosa", `Se actualizaron los datos de la fila${item.row}.`);
        cleanObject();
        console.log("edited");
    }
    catch(e){
        if (e.response) {
            const data = await e.response.json();
            if (data.errors){
                errorNotification("Ocurrió un error :(", data.errors[0].defaultMessage);
                return;
            }
            errorNotification("Ocurrió un error al editar :(", data.message);
        }
    }
    finally{
        hideDrawer();
        emit('update-list');
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
const cleanObject = ()=>{
    object.row = '';
    object.status = '';
    object.shelf.id = null;
}

onMounted(
    async () => {
        if (objectToUpdate.value !== null) {
            object.row = objectToUpdate.value.row;
            object.status = translation[objectToUpdate.value.status];
            object.shelf.id = objectToUpdate.value.shelf.id;
        }
    }
)

</script>

<style scoped>

</style>