<template>
  <div>
    <a-tag color="blue">Cantidad de empleados</a-tag>
    <a-badge :count="countLocation" />
    <br />
    <br />
    <LocationFormVue :shelves="shelfList"  @update-list="fetchLocations" />
    <hr>
    <a-table bordered :data-source="locationList" :columns="columns" >
      <template #bodyCell="{ column, text, record }">
        <template v-if="column.dataIndex === 'name'">
          <div class="editable-cell">
            <div class="editable-cell-text-wrapper">
              {{ text || ' ' }}
            </div>
          </div>
        </template>
        <template v-else-if="column.dataIndex === 'operation'">
          <a-popconfirm v-if="locationList.length" :title="'¿Está seguro que quiere eliminar a la fila ' + record.row"
            @confirm="onDelete(record.id, record.row)">
            <a-radio-button value="large">
              <delete-outlined />
            </a-radio-button>
          </a-popconfirm>
          <LocationFormVue :record="record" :shelves="shelfList" 
            @update-list="fetchLocations" />
        </template>
      </template>
    </a-table>
  </div>
</template>

<script setup>
import LocationFormVue from '../components/LocationForm.vue';
import { ref, computed, onMounted } from 'vue';
import { DeleteOutlined } from '@ant-design/icons-vue';
import { getAllLocations, removeLocation } from '../composables/Location';
import { getAllShelves } from '../composables/Shelf';
import { successNotification, errorNotification } from '../composables/Notification';

const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
  },
  {
    title: 'Fila',
    dataIndex: 'row',
  },
  {
    title: 'Estado',
    dataIndex: 'status',
  },
  {
    title: 'Nombre Estante',
    dataIndex: 'nameShelf',
  },
  {
    title: 'Operaciones',
    dataIndex: 'operation',
    width: 150
  },
];
const translation = {
  "AVAILABLE": "DISPONIBLE",
  "UNAVAILABLE": "INDISPONIBLE"
};

const shelfList = ref([]);
const locationList = ref([]);

const countLocation = computed(() => locationList.value.length);

const onDelete = async (locationId, row) => {
  try {
    const result = await removeLocation(locationId);
    successNotification("Eliminación exitosa!", `La ubicación ${row} fue eliminado`);
    await fetchLocations();
  }
  catch (e) {
    if (e.response) {
      const data = await e.response.json();
      if (data.errors) {
        errorNotification("Ocurrió un error :(", data.errors[0].defaultMessage);
        return;
      }
      errorNotification("Ocurrió un error al eliminar :(", data.message);
    }
  }
};

const fetchLocations = async () => {
  const result = await getAllLocations();
  const data = await result.json();
  locationList.value = data.map(item => {
    item["nameShelf"] = item.shelf.name;
    item.status = translation[item.status];
    return item;
  });
}

onMounted(async () => {
  await fetchLocations();
  //recibiendo tipos de propietario
  let result = await getAllShelves();
  let data = await result.json();
  shelfList.value = data;

});

</script>


