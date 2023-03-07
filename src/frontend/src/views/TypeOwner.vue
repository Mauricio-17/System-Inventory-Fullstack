<template>
  <div>
      <a-tag color="blue">Cantidad de Marcas registradas</a-tag>
      <a-badge :count="countTypeOwner" />
      <br />
      <br />
      <TypeFormVue @update-list="fetchTypes" />
      <hr>

      <a-table bordered :data-source="listTypeOwner" :columns="columns">
          <template #bodyCell="{ column, text, record }">
              <template v-if="column.dataIndex === 'name'">
                  <div class="editable-cell">
                      <div class="editable-cell-text-wrapper">
                          {{ text || ' ' }}
                      </div>
                  </div>
              </template>
              <template v-else-if="column.dataIndex === 'actions'">
                  <a-popconfirm v-if="listTypeOwner.length"
                      :title="'¿Está seguro que quiere eliminar el estante ' + record.name" @confirm="onDelete(record.id, record.name)">
                      <a-radio-button value="large">
                          <delete-outlined />
                      </a-radio-button>
                  </a-popconfirm>
                  <TypeFormVue @update-list="fetchTypes" :record="record" />
              </template>
          </template>
      </a-table>

  </div>
</template>
<script setup>
import TypeFormVue from '../components/TypeForm.vue';
import { ref, onMounted, computed } from 'vue';
import { DeleteOutlined } from '@ant-design/icons-vue';
import { getAllTypes, removeType } from '../composables/TypeOwner';
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
      title: 'Descripción',
      dataIndex: 'description',
  },
  {
      title: 'Acciones',
      dataIndex: 'actions'
  }
];

const listTypeOwner = ref([]);
const countTypeOwner = computed(() => listTypeOwner.value.length);

const onDelete = async (typeId, name) => {
  try {
      const result = await removeType(typeId);
      successNotification("Eliminación exitosa!", `El Tipo de propietario ${name} fue eliminado.`);
      await fetchTypes();
  }
  catch (e) {
      if (e.response) {
          const data = await e.response.json();
          if (data.errors){
              errorNotification("Ocurrió un error :(", data.errors[0].defaultMessage);
              return;
          }
          errorNotification("Ocurrió un error al eliminar el Tipo de propietario :(", data.message);
      }

  }
};

const fetchTypes = async () => {
  const result = await getAllTypes();
  const data = await result.json();
  listTypeOwner.value = data;
};

onMounted(async () => {
  await fetchTypes();
});

</script>

