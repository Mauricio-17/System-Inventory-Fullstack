<template>
  <div>
      <a-tag color="blue">Cantidad de Marcas registradas</a-tag>
      <a-badge :count="countShelf" />
      <br />
      <br />
      <ShelfFormVue @update-list="fetchShelves" />
      <hr>

      <a-table bordered :data-source="listShelf" :columns="columns">
          <template #bodyCell="{ column, text, record }">
              <template v-if="column.dataIndex === 'name'">
                  <div class="editable-cell">
                      <div class="editable-cell-text-wrapper">
                          {{ text || ' ' }}
                      </div>
                  </div>
              </template>
              <template v-else-if="column.dataIndex === 'actions'">
                  <a-popconfirm v-if="listShelf.length"
                      :title="'¿Está seguro que quiere eliminar el estante ' + record.name" @confirm="onDelete(record.id, record.name)">
                      <a-radio-button value="large">
                          <delete-outlined />
                      </a-radio-button>
                  </a-popconfirm>
                  <ShelfFormVue @update-list="fetchShelves" :record="record" />
              </template>
          </template>
      </a-table>

  </div>
</template>
<script setup>
import ShelfFormVue from '../components/ShelfForm.vue';
import { ref, onMounted, computed } from 'vue';
import { DeleteOutlined } from '@ant-design/icons-vue';
import { getAllShelves, removeShelf } from '../composables/Shelf';
import { successNotification, errorNotification } from '../composables/Notification';

const columns = [
  {
      title: 'ID',
      dataIndex: 'id',
  },
  {
      title: 'Nombre',
      dataIndex: 'name',
  },
  {
      title: 'Nro Serie',
      dataIndex: 'serial',
  },
  {
      title: 'Descripción',
      dataIndex: 'description',
  },
  {
      title: 'Acciones',
      dataIndex: 'actions'
  }
];

const listShelf = ref([]);
const countShelf = computed(() => listShelf.value.length);

const onDelete = async (shelfId, name) => {
  try {
      const result = await removeShelf(shelfId);
      successNotification("Eliminación exitosa!", `El estante ${name} fue eliminado.`);
      await fetchShelves();
  }
  catch (e) {
      if (e.response) {
          const data = await e.response.json();
          if (data.errors){
              errorNotification("Ocurrió un error :(", data.errors[0].defaultMessage);
              return;
          }
          errorNotification("Ocurrió un error al eliminar el estante :(", data.message);
      }

  }
};

const fetchShelves = async () => {
  const result = await getAllShelves();
  const data = await result.json();
  listShelf.value = data;
};

onMounted(async () => {
  await fetchShelves();
});

</script>

