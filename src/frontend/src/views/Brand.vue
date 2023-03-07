<template>
  <div>
      <a-tag color="blue">Cantidad de Marcas registradas</a-tag>
      <a-badge :count="countBrand" />
      <br />
      <br />
      <BrandFormVue @update-list="fetchBrands" />
      <hr>

      <a-table bordered :data-source="listBrand" :columns="columns">
          <template #bodyCell="{ column, text, record }">
              <template v-if="column.dataIndex === 'name'">
                  <div class="editable-cell">
                      <div class="editable-cell-text-wrapper">
                          {{ text || ' ' }}
                      </div>
                  </div>
              </template>
              <template v-else-if="column.dataIndex === 'actions'">
                  <a-popconfirm v-if="listBrand.length"
                      :title="'¿Está seguro que quiere eliminar la marca ' + record.name" @confirm="onDelete(record.id, record.name)">
                      <a-radio-button value="large">
                          <delete-outlined />
                      </a-radio-button>
                  </a-popconfirm>
                  <BrandFormVue @update-list="fetchBrands" :record="record" />
              </template>
          </template>
      </a-table>

  </div>
</template>
<script setup>
import BrandFormVue from '../components/BrandForm.vue';
import { ref, onMounted, computed } from 'vue';
import { DeleteOutlined } from '@ant-design/icons-vue';
import { getAllBrands, removeBrand } from '../composables/Brand';
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
      title: 'País',
      dataIndex: 'country',
  },
  {
      title: 'Acciones',
      dataIndex: 'actions'
  }
];

const listBrand = ref([]);
const countBrand = computed(() => listBrand.value.length);

const onDelete = async (brandId, name) => {
  try {
      const result = await removeBrand(brandId);
      successNotification("Eliminación exitosa!", `La marca ${name} fue eliminada.`);
      await fetchBrands();
  }
  catch (e) {
      if (e.response) {
          const data = await e.response.json();
          if (data.errors){
              errorNotification("Ocurrió un error :(", data.errors[0].defaultMessage);
              return;
          }
          errorNotification("Ocurrió un error al eliminar la marca :(", data.message);
      }

  }
};

const fetchBrands = async () => {
  const result = await getAllBrands();
  const data = await result.json();
  listBrand.value = data;
};

onMounted(async () => {
  await fetchBrands();
});

</script>

