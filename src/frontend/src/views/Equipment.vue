<template>
  <div>
    <a-tag color="blue">Cantidad de empleados</a-tag>
    <a-badge :count="countEquipment" />
    <br />
    <br />
    <EquipmentFormVue
    :owners="listOwner"
    :brands="listBrand"
    :categories="listCategory"
    @update-list="fetchEquipments"
    />
    <hr>

    <a-table bordered :data-source="listEquipment" :columns="columns" :scroll="{ x: 1500, y: 400 }">
      <template #bodyCell="{ column, text, record }">
        <template v-if="column.dataIndex === 'name'">
          <div class="editable-cell">
            <div class="editable-cell-text-wrapper">
              {{ text || ' ' }}
            </div>
          </div>
        </template>
        <template v-else-if="column.dataIndex === 'operation'">
          <a-popconfirm v-if="listEquipment.length" :title="'¿Está seguro que quiere eliminar a ' + record.name"
            @confirm="onDelete(record.equipmentId, record.name)">
            <a-radio-button value="large">
              <delete-outlined />
            </a-radio-button>
          </a-popconfirm>
          <EquipmentFormVue
          :owners="listOwner"
          :brands="listBrand"
          :categories="listCategory"
          @update-list="fetchEquipments"
          :record="record"
          />
        </template>
      </template>
    </a-table>
  </div>
</template>

<script setup>
import EquipmentFormVue from '../components/EquipmentForm.vue';
import { ref, reactive, computed, onMounted } from 'vue';
import { DeleteOutlined } from '@ant-design/icons-vue';
import { getAllEquipments, removeEquipment } from '../composables/Equipment';
import { successNotification, errorNotification } from '../composables/Notification';
import { getAllOwners } from '../composables/Owner';
import { getAllCategories } from '../composables/Category';
import { getAllBrands } from '../composables/Brand';

const columns = [
  {
    title: 'ID',
    dataIndex: 'equipmentId',
    fixed: 'left',
    width: 50,
    key: 'equipmentId',
  },
  {
    title: 'Nombre',
    dataIndex: 'name',
    fixed: 'left',
    width: 100,
    key: 'name'
  },
  {
    title: 'SKU',
    dataIndex: 'sku',
    fixed: 'left',
    width: 100,
    key: 'sku'
  },
  {
    title: 'Estado',
    dataIndex: 'status',
    width: 100,
    key: '1'
  },
  {
    title: 'Stock',
    dataIndex: 'stock',
    width: 80,
    key: '2'
  },
  {
    title: 'Marca',
    dataIndex: 'brandName',
    width: 100,
    key: '3'
  },
  {
    title: 'Categoría',
    dataIndex: 'categoryName',
    width: 100,
    key: '4'
  },
  {
    title: 'Fila',
    dataIndex: 'shelfRow',
    width: 60,
    key: '5',
  },
  {
    title: 'Propietario',
    dataIndex: 'ownerName',
    width: 100,
    key: '6',
  },
  {
    title: 'Creado en',
    dataIndex: 'createdAt',
    width: 100,
    key: '7',
  },
  {
    title: 'Actualizado en',
    dataIndex: 'updatedAt',
    width: 100,
    key: '8',
  },
  {
    title: 'Operaciones',
    dataIndex: 'operation',
    fixed: 'right',
    width: 100,
    key: 'operation'
  },
];
const translation = {
  "ENABLE": 'HABILITADO',
  "UNABLE": 'INHABILITADO',
  "DECOMPOSED": 'DESCOMPUESTO',
  "REMOVED": 'REMOVIDO'
};

const countEquipment = computed(() => listEquipment.value.length);

const listEquipment = ref([]);
const listCategory = ref([]);
const listOwner = ref([]);
const listBrand = ref([]);

const onDelete = async (employeeId, name) => {
  try {
    const result = await removeEquipment(employeeId);
    successNotification("Eliminación exitosa!", `El equipo ${name} fue eliminado.`);
    await fetchEmployees();
  }
  catch (e) {
    if (e.response) {
      const data = await e.response.json();
      if (data.errors){
                errorNotification("Ocurrió un error :(", data.errors[0].defaultMessage);
                return;
            }
            errorNotification("Ocurrió un error :(", data.message);
    }
  }
};

const fetchEquipments = async () => {
  const result = await getAllEquipments();
  const {content} = await result.json();
  listEquipment.value = content.map(item => {
    item.status = translation[item.status];
    return item;
  });
}

onMounted(async () => {
  await fetchEquipments();

  let result = await getAllCategories();
  let data = await result.json();
  listCategory.value = data;

  result = await getAllOwners();
  data = await result.json();
  listOwner.value = data;

  result = await getAllBrands();
  data = await result.json();
  listBrand.value = data;

});

</script>
