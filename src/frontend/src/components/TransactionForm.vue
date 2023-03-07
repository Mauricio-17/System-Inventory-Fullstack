<template>
    <a-button type="primary" @click="showDrawer">REGISTRAR</a-button>

    <a-drawer v-model:visible="visible" class="custom-class" style="color: red" title="TRANSACCIÓN" placement="right"
        @after-visible-change="afterVisibleChange">
        <a-form :model="object" name="basic" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }" autocomplete="off"
            :validate-messages="validateMessages">
            <a-form-item label="DESCRIPCIÓN" name="description" >
                <a-textarea v-model:value="object.description" />
            </a-form-item>
            <a-form-item label="EMPLEADO" name="employee">
                <a-select label="Empleado" ref="select" v-model:value="object.employee.id"
                    style="width: 150px">
                    <a-select-option v-for="employee in employees" :index="employee.id" :value="employee.employeeId">
                        {{ employee.name }}
                    </a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="EQUIPO" name="equipment">
                <a-select label="Equipo" ref="select" v-model:value="object.equipment.id"
                    style="width: 150px">
                    <a-select-option v-for="equipment in equipments" :index="equipment.id" :value="equipment.equipmentId">
                        {{ equipment.name }}
                    </a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="PROCEDENCIA" name="source">
                <a-select label="Procedencia" ref="select" v-model:value="object.source.id"
                    style="width: 150px">
                    <a-select-option v-for="location in availableLocations" :index="location.id" :value="location.id">
                        {{ location.row }}
                    </a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="DESTINO" name="destination">
                <a-select label="Destino" ref="select" v-model:value="object.destination.id"
                    style="width: 150px">
                    <a-select-option v-for="location in availableLocations" :index="location.id" :value="location.id">
                        {{ location.row }}
                    </a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item :wrapper-col="{ span: 14, offset: 4 }">
                <a-button type="primary" @click="submitObject">Registrar</a-button>
                <a-button style="margin-left: 10px" @click="hideDrawer">Cancelar</a-button>
            </a-form-item>
        </a-form>

    </a-drawer>
</template>

<script setup>
import { ref, reactive, watch } from 'vue';
import { addNewTransaction } from '../composables/Transaction';
import { getLocationById, getLocationsByShelfId } from '../composables/Location';
import { successNotification, errorNotification } from '../composables/Notification';

const emit = defineEmits(['update-list']);

const props = defineProps({
    employees: { type: Array, required: true },
    equipments: { type: Array, required: true },
});

const validateMessages = {
    required: '${label} is required!',
    types: {
        email: '${label} no es un email válido!',
        number: '${label} is not a valid number!',
    },
    number: {
        range: '${label} must be between ${min} and ${max}',
    },
};

const visible = ref(false);


const object = reactive({
    description: '',
    equipment: { id: null },
    employee: { id: null },
    source: { id: null },
    destination: { id: null }
});

watch(object.equipment, async (newValue, oldValue) => {
    const equipmentId = (object.equipment.id) ? object.equipment.id : 0;
    console.log(equipmentId);
    const equipment = props.equipments.find(item => item.equipmentId == equipmentId);
    if(equipment){
        let res = await getLocationById(equipment.locationId);
        let data = await res.json();
        const shelfId = data.shelf.id;

        res = await getLocationsByShelfId(shelfId);
        data = await res.json();
        availableLocations.value = data;
    }
});

const availableLocations = ref([]);

const submitObject = async () => {
    const item = { ...object }; // this is to prevent reactivity
    try {
        await addNewTransaction(item);
        successNotification("Registro exitoso", `La transacción ha sido registrada.`);
        emit('update-list');
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


const afterVisibleChange = (bool) => {
    console.log('visible', bool);
};
const showDrawer = () => {
    visible.value = true;
};
const hideDrawer = () => {
    visible.value = false;
};
const cleanObject = () => {
    object.description = '';
    object.equipment.id = null;
    object.employee.id = null;
    object.source.id = null;
    object.destination.id = null;
}


</script>

<style scoped>

</style>