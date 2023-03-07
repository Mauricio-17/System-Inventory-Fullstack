<template>
    <a-button v-if="!objectToUpdate" type="primary" @click="showDrawer">Registrar </a-button>
    <a-button v-else @click="showDrawer" type="primary">
        <edit-outlined />
    </a-button>

    <a-drawer v-model:visible="visible" class="custom-class" style="color: red" title="Equipo" placement="right"
        @after-visible-change="afterVisibleChange">
        <a-form :model="object" name="basic" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }" autocomplete="off"
            :validate-messages="validateMessages">
            <a-form-item label="NOMBRE" name="name"
                :rules="[{ required: true, message: 'Porfavor, ingrese el nombre' }]">
                <a-input v-model:value="object.name" />
            </a-form-item>
            <a-form-item label="SKU" name="sku"
            :rules="[{ required: true, message: 'Porfavor, ingrese el SKU' }]">
                <a-input v-model:value="object.sku" />
            </a-form-item>
            <a-form-item label="STOCK" name="stock">
                <a-input-number v-model:value="object.stock" />
            </a-form-item>
            <a-form-item label="ESTADO" name="status">
                <a-select label="ESTADO" ref="select" v-model:value="object.status" style="width: 150px">
                    <a-select-option value="ENABLE">HABILITADO</a-select-option>
                    <a-select-option value="UNABLE">INHABILITADO</a-select-option>
                    <a-select-option value="DECOMPOSED">DESCOMPUESTO</a-select-option>
                    <a-select-option value="REMOVED">REMOVIDO</a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="PROPIETARIO" name="owner">
                <a-select label="PROPIETARIO" ref="select" v-model:value="object.owner.id" style="width: 150px">
                    <a-select-option v-for="owner in owners" :index="owner.id" :value="owner.id">
                        {{ owner.name }}
                    </a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="CATEGORÍA" name="category">
                <a-select label="CATEGORÍA" ref="select" v-model:value="object.category.id" style="width: 150px">
                    <a-select-option v-for="category in categories" :index="category.id" :value="category.id">
                        {{ category.name }}
                    </a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="ESTANTE" name="shelf">
                <a-select label="ESTANTE" ref="select" v-model:value="shelfId" style="width: 150px">
                    <a-select-option v-for="shelf in shelves" :index="shelf.id" :value="shelf.id">
                        {{ shelf.name }}
                    </a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="UBICACIÓN" name="location">
                <a-select label="UBICACIÓN" ref="select" v-model:value="object.location.id" style="width: 100px">
                    <a-select-option v-for="location in availableLocations" :index="location.id" :value="location.id">
                        {{ location.row }}
                    </a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="MARCA" name="brand">
                <a-select label="MARCA" ref="select" v-model:value="object.brand.id" style="width: 150px">
                    <a-select-option v-for="brand in brands" :index="brand.id" :value="brand.id">
                        {{ brand.name }}
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
import { ref, reactive, onMounted, computed, watch } from 'vue';
import { EditOutlined } from '@ant-design/icons-vue';
import { successNotification, errorNotification } from '../composables/Notification';
import { addNewEquipment, updateEquipment } from '../composables/Equipment';
import { getAllShelves } from '../composables/Shelf';
import { getLocationsByShelfId } from '../composables/Location';

const emit = defineEmits(['update-list']);
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
const translation = {
  "HABILITADO": "ENABLE",
  "INHABILITADO": "UNABLE",
  "DESCOMPUESTO" : "DECOMPOSED",
  "REMOVIDO" : "REMOVED"
};

const props = defineProps({
    record: {
        type: Object,
        default: null
    },
    owners: { type: Array, required: true },
    brands: { type: Array, required: true },
    categories: { type: Array, required: true }
});

const object = reactive({
    name: '',
    sku: '',
    stock: null,
    status: '',
    owner: {
        id: null
    },
    category: {
        id: null
    },
    brand: {
        id: null
    },
    location: {
        id: null
    }
});
const shelfId = ref(null);
const shelves = ref([]);
const availableLocations = ref([]);

watch(shelfId, async (newValue, oldValue) => {
    newValue = (newValue) ? newValue : 0;
    const result = await getLocationsByShelfId(newValue);
    const data = await result.json();
    availableLocations.value = data;
});

const objectToUpdate = ref(props.record);
const visible = ref(false);

const submitObject = async () => {
    const item = { ...object }; // this is to prevent reactivity
    try {
        let result = await addNewEquipment(item);
        successNotification("Registro exitoso", `El equipo ${item.name.toUpperCase()} ha sido registrado.`);
        emit('update-list');
    } catch (e) {
        if (e.response) {
            const data = await e.response.json();
            if (data.errors) {
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
        const result = await updateEquipment(item, objectToUpdate.value.equipmentId);
        successNotification("Edición exitosa", `Se actualizaron los datos de ${item.name.toUpperCase()}.`);
        emit('update-list');
    }
    catch (e) {
        if (e.response) {
            const data = await e.response.json();
            if (data.errors) {
                errorNotification("Ocurrió un error :(", data.errors[0].defaultMessage);
                return;
            }
            errorNotification("Ocurrió un error :(", data.message);
        }
    }
    finally {
        hideDrawer();
    }
};
const fetchShelves = async () => {
    const result = await getAllShelves();
    const data = await result.json();
    shelves.value = data;
}

const cleanObject = () => {
    object.name = '';
    object.sku = '';
    object.status = '';
    object.stock = null;
    object.owner.id = null;
    object.category.id = null;
    object.brand.id = null;
    object.location.id = null;
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

onMounted(async () => {
    await fetchShelves();
    if (objectToUpdate.value != null) {
        object.name = objectToUpdate.value.name;
        object.sku = objectToUpdate.value.sku;
        object.status = translation[objectToUpdate.value.status];
        object.stock = objectToUpdate.value.stock;
        object.owner.id = objectToUpdate.value.ownerId;
        object.category.id = objectToUpdate.value.categoryId;
        object.brand.id = objectToUpdate.value.brandId;
        object.location.id = objectToUpdate.value.locationId;
    }
});

</script>