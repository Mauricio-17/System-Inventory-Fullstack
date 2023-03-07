<template>
  <div>
    <a-tag color="blue">Cantidad de empleados</a-tag>
    <a-badge :count="countOwner" />
    <br />
    <br />
    <OwnerFormVue :types="typeList"  @update-list="fetchOwners" />
    <hr>

    <a-table bordered :data-source="ownerList" :columns="columns" :scroll="{ x: 1500, y: 400 }">
      <template #bodyCell="{ column, text, record }">
        <template v-if="column.dataIndex === 'name'">
          <div class="editable-cell">
            <div class="editable-cell-text-wrapper">
              {{ text || ' ' }}
            </div>
          </div>
        </template>
        <template v-else-if="column.dataIndex === 'operation'">
          <a-popconfirm v-if="ownerList.length" :title="'¿Está seguro que quiere eliminar a ' + record.name"
            @confirm="onDelete(record.id, record.name)">
            <a-radio-button value="large">
              <delete-outlined />
            </a-radio-button>
          </a-popconfirm>
          <OwnerFormVue :record="record" :types="typeList" 
            @update-list="fetchOwners" />
        </template>
      </template>
    </a-table>
  </div>
</template>

<script setup>
import OwnerFormVue from '../components/OwnerForm.vue';
import { ref, computed, onMounted } from 'vue';
import { DeleteOutlined } from '@ant-design/icons-vue';
import { getAllOwners, removeOwner } from '../composables/Owner';
import { getAllTypes } from '../composables/TypeOwner';
import { successNotification, errorNotification } from '../composables/Notification';

const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    fixed: 'left',
    width: 50,
    key: 'id',
  },
  {
    title: 'Nombre',
    dataIndex: 'name',
    fixed: 'left',
    width: 80,
    key: 'name'
  },
  {
    title: 'Apellido',
    dataIndex: 'lastName',
    fixed: 'left',
    width: 80,
    key: 'lastName'
  },
  {
    title: 'Nº de celular',
    dataIndex: 'cellphoneNumber',
    width: 100,
    key: '1'
  },
  {
    title: 'Email',
    dataIndex: 'email',
    width: 100,
    key: '2'
  },
  {
    title: 'DNI',
    dataIndex: 'dni',
    width: 100,
    key: '3'
  },
  {
    title: 'Tipo propietario',
    dataIndex: 'nameType',
    width: 100,
    key: '4'
  },
  {
    title: 'Operaciones',
    dataIndex: 'operation',
    fixed: 'right',
    width: 80,
    key: 'operation'
  },
];



const typeList = ref([]);
const ownerList = ref([]);

const countOwner = computed(() => ownerList.value.length);

const onDelete = async (ownerId, name) => {
  try {
    const result = await removeOwner(ownerId);
    successNotification("Eliminación exitosa!", `El propietario ${name} fue eliminado`);
    await fetchOwners();
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

const fetchOwners = async () => {
  const result = await getAllOwners();
  const data = await result.json();
  ownerList.value = data.map(item => {
    item["nameType"] = item.typeOwner.name;
    return item;
  });
}

onMounted(async () => {
  await fetchOwners();
  //recibiendo tipos de propietario
  let result = await getAllTypes();
  let data = await result.json();
  typeList.value = data;

});

</script>


